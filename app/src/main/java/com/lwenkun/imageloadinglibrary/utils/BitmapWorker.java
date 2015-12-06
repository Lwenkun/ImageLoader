package com.lwenkun.imageloadinglibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.lwenkun.imageloadinglibrary.R;
import com.lwenkun.imageloadinglibrary.cache.ImageLruCache;
import com.lwenkun.imageloadinglibrary.ui.AsyncDrawable;

/**
 * Created by 15119 on 2015/11/9.
 */
public class BitmapWorker {

    private final Bitmap defaultBitmap;

    private final Resources res;

    private final ImageLruCache imageLruCache;

    private Context context;

    public BitmapWorker(Context context, Resources res) {
        this.context = context;
        this.res = res;
        imageLruCache = ImageLruCache.getInstance();
        defaultBitmap = BitmapFactory.decodeResource(res, R.drawable.placehold);
    }

    public void loadImage(String imageUrl, ImageView imageView) {

        Bitmap bitmap = imageLruCache.get(imageUrl);

        if (bitmap != null) {

            imageView.setImageBitmap(bitmap);

        } else if(cancelBitmapTask(imageUrl, imageView)) {

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

            String imageUrl = bitmapWorkerTask.getUrl();
            // 该Task绑定的url与当前相同，即该ImageView绑定的的task是当前请求的task，不取消
            if (imageUrl == null || !imageUrl.equals(key)) {

                bitmapWorkerTask.cancel(true);
                Log.d("BitmapWorker", "任务不同");

            } else {
                Log.d("BitmapWorker", "任务相同");

                //如果不相同，即该ImageView绑定的task不是当前所请求的task，将其取消
                return false;
            }
        }

        //该任务还没有设置url或者该任务和当前任务不相同或者该ImageView绑定的任务不存在
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

