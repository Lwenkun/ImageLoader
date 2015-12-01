package com.lwenkun.imageloadinglibrary.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by 15119 on 2015/11/10.
 */
public class AsyncDrawable extends BitmapDrawable {
    private WeakReference<BitmapWorkerTask> bitmapWorkerTaskWeakReference;

    public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask task) {
        super(res, bitmap);
        bitmapWorkerTaskWeakReference = new WeakReference<>(task);
    }

    public BitmapWorkerTask getBitmapWorkerTask() {
        if(bitmapWorkerTaskWeakReference != null)
            return bitmapWorkerTaskWeakReference.get();
        return null;
    }

}