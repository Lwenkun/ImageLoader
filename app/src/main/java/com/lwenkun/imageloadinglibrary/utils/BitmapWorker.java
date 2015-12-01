package com.lwenkun.imageloadinglibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.lwenkun.imageloadinglibrary.R;

/**
 * Created by 15119 on 2015/11/9.
 */
public class BitmapWorker {

    private final Bitmap defaultBitmap;

    private final Resources res;

    private Context context;

    public BitmapWorker(Context context, Resources res) {
        this.context = context;
        this.res = res;
        defaultBitmap = BitmapFactory.decodeResource(res, R.drawable.placehold);
    }

    public void loadImage(String imageUrl, ImageView imageView) {

        String key = MD5.hashKeyForDisk(imageUrl);

        if(cancelBitmapTask(key, imageView)) {
            BitmapWorkerTask task = new BitmapWorkerTask(context, imageView);
            AsyncDrawable asyncDrawable = new AsyncDrawable(res, defaultBitmap, task);
            //显示占位图
            imageView.setImageDrawable(asyncDrawable);
            task.execute(imageUrl);
        }
    }

    public boolean cancelBitmapTask(String key, ImageView imageView) {
        //获得当前ImageView绑定的Task
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkTask(imageView);

        //如果task不为空
        if(bitmapWorkerTask != null) {

            // 该Task的索引值与目前的索引值相同，即该ImageView绑定的的task是当前请求的task，不取消
            if (key.equals(bitmapWorkerTask.getData())) {

                return false;

            } else {

                //如果索引值不相同，即该ImageView绑定的task不是当前所请求的task，将其取消
                bitmapWorkerTask.cancel(true);
            }
        }

        return true;
    }

    public BitmapWorkerTask getBitmapWorkTask(ImageView imageView) {

        if(imageView != null) {

            //获取Imageview的drawable对象
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

            //如果该drawable是AsyncDrawable的一个实例，则ImageView显示的还是占位图且正在获取图片中
            if(drawable instanceof AsyncDrawable) {

                return ((AsyncDrawable) drawable).getBitmapWorkerTask();
            }
        }

        //如果ImageView为空或者显示的不是占位图，则task为空
        return null;
    }


}

