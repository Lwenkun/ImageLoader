package com.lwenkun.imageloadinglibrary.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by 15119 on 2015/11/17.
 */
public class Common {

    public static String cacheDirName = "bitmap";

    static int cacheSize = (int) Runtime.getRuntime().maxMemory() / 8;

    public  static LruCache<String, Bitmap> imageLruCache = new LruCache<>(cacheSize);
}
