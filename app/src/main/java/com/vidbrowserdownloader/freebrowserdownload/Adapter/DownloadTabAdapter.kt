package com.vidbrowserdownloader.freebrowserdownload.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DownloadTabAdapter (
    fm: FragmentActivity,
) : FragmentStateAdapter(fm) {
    var listfragment: ArrayList<Fragment> = arrayListOf()
    var listname: ArrayList<String> = arrayListOf()
    override fun getItemCount() = listfragment.size

    fun addFragment(fragment: Fragment, name: String) {
        listfragment.add(fragment)
        listname.add(name)
    }

    override fun createFragment(position: Int) = listfragment[position]

}