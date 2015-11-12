package com.lwenkun.imageloadinglibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.LruCache;
import android.widget.ImageView;

import com.lwenkun.imageloadinglibrary.R;
import com.lwenkun.imageloadinglibrary.cache.DiskCache;

/**
 * Created by 15119 on 2015/11/9.
 */
public class BitmapWorker {

    private static final String DISK_CACHE_FILE_NAME = "cache";

    private final Bitmap defaultBitmap;

    private final Resources res;

    private static DiskCache diskCache;

    private static LruCache<String, Bitmap> imageLruCache;

    public BitmapWorker(Context context, Resources res) {
        this.res = res;
        int maxMemory = (int) Runtime.getRuntime().maxMemory() / 8;
        imageLruCache = new LruCache<>(maxMemory);
        diskCache = new DiskCache(context, DISK_CACHE_FILE_NAME);
        defaultBitmap = BitmapFactory.decodeResource(res, R.drawable.placehold);
    }

    public void loadImage(String imageUrls, ImageView imageView) {

        if(cancelBitmapTask(imageUrls, imageView)) {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView, imageLruCache, diskCache);
            AsyncDrawable  asyncDrawable = new AsyncDrawable(res, defaultBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(imageUrls);
        }
    }

    public boolean cancelBitmapTask(String imageUrls, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkTask(imageView);

        if(bitmapWorkerTask != null) {
            if (imageUrls.equals(bitmapWorkerTask.getData())) {
                return false;
            } else {
                bitmapWorkerTask.cancel(true);
            }
        }

        return true;
    }

    public BitmapWorkerTask getBitmapWorkTask(ImageView imageView) {
        if(imageView != null) {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

            if(drawable instanceof AsyncDrawable) {

                return ((AsyncDrawable) drawable).getBitmapWorkerTask();
            }
        }
        return null;
    }


}

