package com.lwenkun.imageloadinglibrary.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lwenkun.imageloadinglibrary.R;
import com.lwenkun.imageloadinglibrary.provider.Images;
import com.lwenkun.imageloadinglibrary.utils.BitmapWorker;

/**
 * Created by 15119 on 2015/11/9.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {

    private final Context context;

    private BitmapWorker bitmapWorker;

    //自定义构造方法将图片传入
    public ImageListAdapter(Context context, Resources res) {

        this.context = context;
        bitmapWorker = new BitmapWorker(context, res);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        //保存view，给view注册监听事件，找到view中的控件
        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "hello", Toast.LENGTH_SHORT).show();
                }
            });
            imageView = (ImageView) v.findViewById(R.id.image_view);

        }

        public ImageView getImageView() {
            return imageView;
        }


    }

    //函数调用后获得Viewholder应该和viewtype绑定在一起；以后直接调用onBindViewHolder()就能根据type获得相应的viewholder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_list_item, viewGroup, false);

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
        return Images.imageUrls.length;
    }


}