package com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity

import com.vidbrowserdownloader.freebrowserdownload.Activity.YTactivity.Application.Companion.log
import com.vidbrowserdownloader.freebrowserdownload.Model.IconModel
import com.vidbrowserdownloader.freebrowserdownload.R
import com.vidbrowserdownloader.freebrowserdownload.databinding.ActivityVideoPlayBinding
import com.vidbrowserdownloader.freebrowserdownload.other.OnSwipeTouchListener
import com.vidbrowserdownloader.freebrowserdownload.utils.gon
import com.vidbrowserdownloader.freebrowserdownload.utils.loadImg
import com.vidbrowserdownloader.freebrowserdownload.utils.visible
import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ads.sdk.AdsManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.SimpleExoPlayer
import com.vidbrowserdownloader.freebrowserdownload.Adapter.PlaybackIconsAdapter
import com.vidbrowserdownloader.freebrowserdownload.Model.SuggestVideoModel.ResultsItem
import kotlin.math.abs
import kotlin.math.ceil

class VideoPlayAct : BaseAct<ActivityVideoPlayBinding>() {

    private lateinit var wakeLock: PowerManager.WakeLock
    var videoUrl: String = ""
    var videoTitle: String = ""

    var videoThumbnail: String = ""
    var videoTime: String = ""
    var newList: ArrayList<ResultsItem> = arrayListOf()
    lateinit var statusUri: Uri
    lateinit var videoFrom: String
    lateinit var player: SimpleExoPlayer
    var position = 0
    lateinit var title: TextView
    lateinit var controlsMode: ControlsMode

    enum class ControlsMode {
        LOCK, FULLSCREEN
    }

    lateinit var videoBack: ImageView
    lateinit var lock: ImageView
    lateinit var unlock: ImageView
    lateinit var scaling: ImageView
    lateinit var videoMore: ImageView
    lateinit var root: RelativeLayout
    val iconModelArrayList: ArrayList<IconModel> = ArrayList()
    lateinit var vol_icon: ImageView
    lateinit var brt_icon: ImageView

    lateinit var vol_text: TextView
    lateinit var brt_text: android.widget.TextView
    lateinit var total_duration: android.widget.TextView
    lateinit var vol_progress: ProgressBar
    lateinit var brt_progress: ProgressBar


    //horizontal recyclerview variables
    lateinit var playbackIconsAdapter: PlaybackIconsAdapter
    lateinit var recyclerViewIcons: RecyclerView
    var expand = false
    lateinit var nightMode: View
    var dark = false
    var mute = false
    lateinit var parameters: PlaybackParameters
    var speed = 0f
    lateinit var pictureInPicture: PictureInPictureParams.Builder
    lateinit var eqContainer: FrameLayout
    private var device_height = 0 //swipe and zoom variables
    private var device_width = 0  //swipe and zoom variables
    private var brightness = 0  //swipe and zoom variables
    private var media_volume = 0
    var start = false
    var left = false
    var right: Boolean = false
    private var baseX = 0f
    private var baseY: Float = 0f
    var swipe_move = false
    private var diffX: Long = 0
    private var diffY: Long = 0
    val MINIMUM_DISTANCE = 100
    var success = false

    lateinit var audioManager: AudioManager
    private lateinit var window: Window
    var singleTap = false

    companion object {

        lateinit var zoomLayout: RelativeLayout
        private var scale_factor = 1.0f
        lateinit var zoom_perc: TextView
        lateinit var zoomContainer: RelativeLayout

        lateinit var vol_progress_container: LinearLayout
        lateinit var vol_text_container: LinearLayout
        lateinit var brt_progress_container: LinearLayout
        lateinit var brt_text_container: LinearLayout
    }

    lateinit var scaleGestureDetector: ScaleGestureDetector
    var double_tap = false
    lateinit var double_tap_playpause: RelativeLayout


    override fun getActivityBinding(inflater: LayoutInflater) =
        ActivityVideoPlayBinding.inflate(layoutInflater)

    override fun initUI(savedInstanceState: Bundle?) {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ON_AFTER_RELEASE,
            "VideoPlayerActivity:wakeLock"
        )


        videoUrl = intent.getStringExtra("videoPath").toString()
        videoTitle = intent.getStringExtra("videoTitle").toString()
        window = getWindow()

        player = SimpleExoPlayer.Builder(this@VideoPlayAct).build()
        initViews()
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        device_width = displayMetrics.widthPixels
        device_height = displayMetrics.heightPixels

        val decorView = getWindow().decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions


        binding.apply {
            if (videoUrl.endsWith("png") || videoUrl.endsWith("jpg")) {
                exoplayerView.gon()
                imgView.visible()
                toolBar.visible()
                imgView.loadImg(videoUrl)
            } else {
                imgView.gon()
                toolBar.gon()
                exoplayerView.visible()
                createPlayer(videoUrl)
            }

            imgback.setOnClickListener {
                finish()
            }
        }


        "width == $device_width | | height == $device_height".log()

        horizontalIconList()
        initClick()

    }


    fun createPlayer(videoUrl: String) {
        binding.apply {
            player = SimpleExoPlayer.Builder(this@VideoPlayAct).build()
            exoplayerView.player = player
            val mediaItem = MediaItem.fromUri(videoUrl)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        }
    }

    private fun initClick() {

        lock.setOnClickListener {
            controlsMode = ControlsMode.FULLSCREEN
            root.visibility = View.VISIBLE
            lock.visibility = View.INVISIBLE
            Toast.makeText(this@VideoPlayAct, "unLocked", Toast.LENGTH_SHORT).show()
        }

        unlock.setOnClickListener {
            controlsMode = ControlsMode.LOCK
            root.visibility = View.INVISIBLE
            lock.visibility = View.VISIBLE
            Toast.makeText(this@VideoPlayAct, "Locked", Toast.LENGTH_SHORT).show()
        }

        binding.apply {

            videoBack.setOnClickListener {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

                } else {
                    player.release()
                    finish()
                }
            }

            scaling.setOnClickListener {
//                changeAspectRatio()
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                    val layoutParams = binding.exoplayerView.layoutParams
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    binding.exoplayerView.layoutParams = layoutParams

                    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)


                } else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

                }
            }

            exoplayerView.setOnTouchListener(object : OnSwipeTouchListener(this@VideoPlayAct) {
                @RequiresApi(Build.VERSION_CODES.M)
                override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {
                            exoplayerView.showController()
                            start = true
                            if (motionEvent.x < device_width / 2) {
                                left = true
                                right = false
                            } else if (motionEvent.x > device_width / 2) {
                                left = false
                                right = true
                            }
                            baseX = motionEvent.x
                            baseY = motionEvent.y
                        }

                        MotionEvent.ACTION_MOVE -> {
                            swipe_move = true
                            diffX = ceil((motionEvent.x - baseX).toDouble()).toLong()
                            diffY = ceil((motionEvent.y - baseY).toDouble()).toLong()
                            val brightnessSpeed = 0.01
                            if (abs(diffY) > MINIMUM_DISTANCE) {
                                start = true
                                if (abs(diffY) > abs(diffX)) {
                                    val value: Boolean
                                    value = Settings.System.canWrite(applicationContext)
                                    if (value) {
                                        if (left) {
                                            try {
                                                Settings.System.putInt(
                                                    contentResolver,
                                                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                                                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
                                                )
                                                brightness = Settings.System.getInt(
                                                    contentResolver,
                                                    Settings.System.SCREEN_BRIGHTNESS
                                                )
                                            } catch (e: Settings.SettingNotFoundException) {
                                                e.printStackTrace()
                                            }
                                            var new_brightness: Int =
                                                (brightness - diffY * brightnessSpeed).toInt()
                                            if (new_brightness > 250) {
                                                new_brightness = 250
                                            } else if (new_brightness < 1) {
                                                new_brightness = 1
                                            }
                                            val brt_percentage =
                                                ceil(new_brightness.toDouble() / 250.0 * 100.0)
                                            brt_progress_container.visibility = View.VISIBLE
                                            brt_text_container.visibility = View.VISIBLE
                                            brt_progress.progress = brt_percentage.toInt()
                                            if (brt_percentage < 30) {
                                                brt_icon.setImageResource(R.drawable.ic_brightness_low)
                                            } else if (brt_percentage > 30 && brt_percentage < 80) {
                                                brt_icon.setImageResource(R.drawable.ic_brightness_moderate)
                                            } else if (brt_percentage > 80) {
                                                brt_icon.setImageResource(R.drawable.ic_brightness)
                                            }
                                            brt_text.text = " " + brt_percentage.toInt() + "%"
                                            Settings.System.putInt(
                                                contentResolver,
                                                Settings.System.SCREEN_BRIGHTNESS,
                                                new_brightness
                                            )
                                            val layoutParams = window.attributes
                                            layoutParams.screenBrightness = brightness / 255f
                                            window.attributes = layoutParams
                                        } else if (right) {
                                            vol_text_container.visibility = View.VISIBLE
                                            media_volume =
                                                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                                            val maxVol: Int =
                                                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                                            val cal: Double =
                                                diffY.toDouble() * (maxVol.toDouble() / ((device_height * 2).toDouble() - brightnessSpeed))
                                            var newMediaVolume: Int = media_volume - cal.toInt()
                                            if (newMediaVolume > maxVol) {
                                                newMediaVolume = maxVol
                                            } else if (newMediaVolume < 1) {
                                                newMediaVolume = 0
                                            }
                                            audioManager.setStreamVolume(
                                                AudioManager.STREAM_MUSIC,
                                                newMediaVolume,
                                                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
                                            )
                                            val volPer =
                                                ceil(newMediaVolume.toDouble() / maxVol.toDouble() * 100.0)
                                            vol_text.text = " " + volPer.toInt() + "%"
                                            if (volPer < 1) {
                                                vol_icon.setImageResource(R.drawable.ic_volume_off)
                                                vol_text.visibility = View.VISIBLE
                                                vol_text.text = "Off"
                                            } else if (volPer >= 1) {
                                                vol_icon.setImageResource(R.drawable.ic_volume)
                                                vol_text.visibility = View.VISIBLE
                                            }
                                            vol_progress_container.visibility = View.VISIBLE
                                            vol_progress.progress = volPer.toInt()
                                        }
                                        success = true
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Allow write settings for swipe controls",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                                        intent.data = Uri.parse("package:$packageName")
                                        startActivityForResult(intent, 111)
                                    }
                                }
                            }
                        }

                        MotionEvent.ACTION_UP -> {
                            swipe_move = false
                            start = false
                            vol_progress_container.visibility = View.GONE
                            brt_progress_container.visibility = View.GONE
                            vol_text_container.visibility = View.GONE
                            brt_text_container.visibility = View.GONE
                        }
                    }
                    scaleGestureDetector.onTouchEvent(motionEvent)
                    return super.onTouch(view, motionEvent)
                }

                override fun onDoubleTouch() {
                    super.onDoubleTouch()
                    if (double_tap) {
                        player.playWhenReady = true
                        double_tap_playpause.visibility = View.GONE
                        double_tap = false
                    } else {
                        player.playWhenReady = false
                        double_tap_playpause.visibility = View.VISIBLE
                        double_tap = true
                    }
                }

                override fun onSingleTouch() {
                    super.onSingleTouch()
//                    toggleSystemUIVisibility()
//                    val decorView = getWindow().decorView
//                    val uiOptions =
//                        (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
//                        decorView.systemUiVisibility = uiOptions
                    singleTap = if (singleTap) {
                        binding.exoplayerView.showController()
                        false
                    } else {
                        binding.exoplayerView.hideController()
                        true
                    }
//                    singleTap1 = if (singleTap1) {
//                        val decorView = getWindow().decorView
//                        val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
//                        decorView.systemUiVisibility = uiOptions
//                        false
//                    } else {
//
//                        val decorView = getWindow().decorView
//                        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
//                        decorView.systemUiVisibility = uiOptions
//                        true
//                    }
                    if (double_tap_playpause.visibility == View.VISIBLE) {
                        double_tap_playpause.visibility = View.GONE
                    }
                }
            })

        }
    }

    private fun toggleSystemUIVisibility() {
        val decorView = window.decorView
        // Check if the system UI (navigation and status bars) is currently visible.
        val isSystemUiVisible =
            (decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0
        if (isSystemUiVisible) {
            // If the system UI is visible, hide it.
            hideSystemUI()
        } else {
            // If the system UI is hidden, show it.
            showSystemUI()
        }
    }

    private fun hideSystemUI() {
        val decorView = window.decorView
        val uiOptions =
            (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = uiOptions
    }

    private fun showSystemUI() {
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
        decorView.systemUiVisibility = uiOptions
    }

    @SuppressLint("NotifyDataSetChanged")
    fun horizontalIconList() {
        iconModelArrayList.add(IconModel(R.drawable.ic_night_mode, "Night"))
//        iconModelArrayList.add(IconModel(R.drawable.ic_rotate, "Rotate"))
        iconModelArrayList.add(IconModel(R.drawable.ic_volume_off, "Mute"))
        iconModelArrayList.add(IconModel(R.drawable.ic_speed, "Speed"))
        playbackIconsAdapter = PlaybackIconsAdapter(iconModelArrayList, this) { position ->
            if (position == 0) {
                //night mode
                if (dark) {
                    nightMode.visibility = View.GONE
                    iconModelArrayList[position] = IconModel(R.drawable.ic_night_mode, "Night")
                    playbackIconsAdapter.notifyDataSetChanged()
                    dark = false
                } else {
                    nightMode.visibility = View.VISIBLE
                    iconModelArrayList[position] = IconModel(R.drawable.ic_night_mode, "Day")
                    playbackIconsAdapter.notifyDataSetChanged()
                    dark = true
                }
            }
            if (position == 1) {
                //mute
                if (mute) {
                    player.volume = 100f
                    iconModelArrayList[position] = IconModel(R.drawable.ic_volume_off, "Mute")
                    playbackIconsAdapter.notifyDataSetChanged()
                    mute = false
                } else {
                    player.volume = 0f
                    iconModelArrayList[position] = IconModel(R.drawable.ic_volume, "unMute")
                    playbackIconsAdapter.notifyDataSetChanged()
                    mute = true
                }
            }
            if (position == 2) {
                //speed
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Select PLayback Speed").setPositiveButton("OK", null)
                val items = arrayOf("0.5x", "1x Normal Speed", "1.25x", "1.5x", "2x")
                val checkedItem = -1
                alertDialog.setSingleChoiceItems(
                    items, checkedItem
                ) { dialog, which ->
                    when (which) {
                        0 -> {
                            speed = 0.5f
                            parameters = PlaybackParameters(speed)
                            player.playbackParameters = parameters
                        }

                        1 -> {
                            speed = 1f
                            parameters = PlaybackParameters(speed)
                            player.playbackParameters = parameters
                        }

                        2 -> {
                            speed = 1.25f
                            parameters = PlaybackParameters(speed)
                            player.playbackParameters = parameters
                        }

                        3 -> {
                            speed = 1.5f
                            parameters = PlaybackParameters(speed)
                            player.playbackParameters = parameters
                        }

                        4 -> {
                            speed = 2f
                            parameters = PlaybackParameters(speed)
                            player.playbackParameters = parameters
                        }

                        else -> {}
                    }
                }
                val alert = alertDialog.create()
                alert.show()
            }
        }
        val layoutManager = LinearLayoutManager(
            this, RecyclerView.HORIZONTAL, true
        )
        recyclerViewIcons.layoutManager = layoutManager
        recyclerViewIcons.adapter = playbackIconsAdapter
        playbackIconsAdapter.notifyDataSetChanged()

    }

    fun initViews() {
        title = findViewById(R.id.video_title)
        videoBack = findViewById(R.id.video_back)
        lock = findViewById(R.id.lock)
        unlock = findViewById(R.id.unlock)
        scaling = findViewById(R.id.scaling)
        root = findViewById(R.id.root_layout)
        nightMode = findViewById(R.id.night_mode)
        recyclerViewIcons = findViewById(R.id.recyclerview_icon)
        eqContainer = findViewById(R.id.eqFrame)
        zoomLayout = findViewById(R.id.zoom_layout)
        zoom_perc = findViewById(R.id.zoom_percentage)
        zoomContainer = findViewById(R.id.zoom_container)
        double_tap_playpause = findViewById(R.id.double_tap_play_pause)
        vol_icon = findViewById(R.id.vol_icon)
        vol_text = findViewById(R.id.vol_text)
        brt_text = findViewById(R.id.brt_text)
        vol_progress = findViewById(R.id.vol_progress)
        brt_progress = findViewById(R.id.brt_progress)
        vol_progress_container = findViewById(R.id.vol_progress_container)
        brt_progress_container = findViewById(R.id.brt_progress_container)
        vol_text_container = findViewById(R.id.vol_text_container)
        brt_text_container = findViewById(R.id.brt_text_container)
        brt_icon = findViewById(R.id.brt_icon)

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        title.text = videoTitle

        scaleGestureDetector = ScaleGestureDetector(
            this, ScaleDetector()
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPicture = PictureInPictureParams.Builder()
        }
    }

    class ScaleDetector : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @SuppressLint("SetTextI18n")
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scale_factor *= detector.scaleFactor
            scale_factor = 0.5f.coerceAtLeast(scale_factor.coerceAtMost(6.0f))
            zoomLayout.scaleX = scale_factor
            zoomLayout.scaleY = scale_factor
            val percentage: Int = (scale_factor * 100).toInt()
            zoom_perc.text = " $percentage%"
            zoomContainer.visibility = View.VISIBLE
            brt_text_container.visibility = View.GONE
            vol_text_container.visibility = View.GONE
            brt_progress_container.visibility = View.GONE
            vol_progress_container.visibility = View.GONE
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            zoomContainer.visibility = View.GONE
            super.onScaleEnd(detector)
        }
    }

    override fun onStart() {
        super.onStart()
        wakeLock.acquire()
    }

    override fun onStop() {
        super.onStop()
        if (wakeLock != null && wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        } else {
            AdsManager.getInstance().showOnbackPressAdExtra(this@VideoPlayAct) {
                finish()
            }
        }
    }
}