package com.lwenkun.imageloadinglibrary.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by 15119 on 2015/11/17.
 */
public class Common {

    static int cacheSize = (int) Runtime.getRuntime().maxMemory() / 8;

    public  static LruCache<Integer, Bitmap> imageLruCache = new LruCache<>(cacheSize);
}
