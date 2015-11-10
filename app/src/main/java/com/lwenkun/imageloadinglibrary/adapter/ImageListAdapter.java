package com.lwenkun.imageloadinglibrary.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lwenkun.imageloadinglibrary.R;
import com.lwenkun.imageloadinglibrary.provider.Images;
import com.lwenkun.imageloadinglibrary.utils.BitmapWorker;

/**
 * Created by 15119 on 2015/11/9.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {

    private final Context context;

    private final int DEFAULT_TYPE = 0;

    private int bitmapSize = Images.imageUrls.length;

    private BitmapWorker bitmapWorker;

    //自定义构造方法将图片传入
    public ImageListAdapter(Context context, Resources res) {
        this.context = context;
        bitmapWorker = new BitmapWorker(context, res);
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

        View v;
        //根据类型找到找到相应的view，这里只有一种view
        switch (viewType) {
            case DEFAULT_TYPE:
               v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.image_list_item, viewGroup, false);
                break;
            default:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.image_list_item, viewGroup, false);
                break;
       }

        //返回一个封装了view的ViewHolder
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        ImageView imageView = viewHolder.getImageView();

        bitmapWorker.loadImage(Images.imageUrls[position], imageView);

    }

    @Override
    public int getItemCount() {
        return bitmapSize;
    }

}
