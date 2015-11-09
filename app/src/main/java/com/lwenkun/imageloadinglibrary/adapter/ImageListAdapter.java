package com.lwenkun.imageloadinglibrary.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lwenkun.imageloadinglibrary.R;

import java.util.ArrayList;

/**
 * Created by 15119 on 2015/11/9.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {

    ArrayList<Bitmap> bitmaps;

    //自定义构造方法将图片传入
    public ImageListAdapter(ArrayList<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        //保存view，给view注册监听事件，找到view中的控件
        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            imageView = (ImageView) v.findViewById(R.id.image_list);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //根据类型找到找到相应的view，这里只有一种view，所以不进行判断
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_list_item, viewGroup, false);

        //返回一个封装了view的ViewHolder
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        //对ViewHolder中的控件进行内容的更新
        viewHolder.getImageView().setImageBitmap(bitmaps.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public void addItem(Bitmap bitmap, int position) {
        bitmaps.add(position, bitmap);
        notifyItemInserted(position);
    }

    public void delete(Bitmap bitmap, int position) {
        bitmaps.remove(position);
        notifyItemInserted(position);
    }
}
