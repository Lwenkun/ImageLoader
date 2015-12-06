package com.lwenkun.imageloadinglibrary.cache;


import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by 15119 on 2015/12/4.
 */
public class ImageLruCache{

    private static ImageLruCache imageLruCache = null;

    private static LruCache<String, Bitmap> lruCache;

    private ImageLruCache() {

        final int MAX_MEMORY =  (int) Runtime.getRuntime().maxMemory() / 8;
        lruCache = new LruCache<>(MAX_MEMORY);
    }

    public static ImageLruCache getInstance() {

        if(imageLruCache == null) {
            imageLruCache = new ImageLruCache();
        }

        return imageLruCache;
    }

    public void put(String key, Bitmap bitmap) {
        lruCache.put(key, bitmap);
    }

    public Bitmap get(String key) {
        return lruCache.get(key);
    }

}
