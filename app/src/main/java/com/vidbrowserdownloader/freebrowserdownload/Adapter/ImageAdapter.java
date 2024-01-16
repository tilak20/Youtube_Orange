package com.vidbrowserdownloader.freebrowserdownload.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import com.vidbrowserdownloader.freebrowserdownload.Model.ImageModel;
import com.vidbrowserdownloader.freebrowserdownload.databinding.ItemImageBinding;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.viewHolder> {

    Context context;
    ArrayList<ImageModel> imageList;
    public OnItemClickListener onItemClickListener;

    public ImageAdapter(Context context, ArrayList<ImageModel> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new viewHolder(ItemImageBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(final viewHolder holder, int pos) {

        ImageModel img = imageList.get(pos);

        Glide.with(context).load(Uri.fromFile(new File(img.getImgPrev()))).into(holder.binding.image);
        holder.binding.tvName.setText(img.getImgName());
        holder.binding.tvName.setSelected(true);

        holder.binding.tvResolution.setText(img.getImgResolution());
        holder.binding.tvDate.setText(img.getImgDate());

        float f = Float.parseFloat(img.getImgSize()) / (1024 * 1024);

        holder.binding.tvSize.setText(String.format("%.2f MB", f));

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ItemImageBinding binding;

        public viewHolder(ItemImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> onItemClickListener.onItemClick(getAdapterPosition(), v));
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }
}
