package com.lwenkun.imageloadinglibrary.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by 15119 on 2015/11/10.
 */
public class DiskCache {
    final String TAG = "DiskCache";

    private final Context context;

    public DiskCache(Context context, String cacheFileName) {
        this.context = context;
        mkCacheDir(cacheFileName);
    }

    public void mkCacheDir(String cacheFileName) {

        String state = Environment.getExternalStorageState();

        File cacheFile;

        if(Environment.MEDIA_MOUNTED.equals(state)) {
            cacheFile = new File(Environment.getExternalStorageDirectory(), cacheFileName);
        } else {
            cacheFile = new File(context.getFilesDir(), cacheFileName);
        }

        if(!cacheFile.exists()) {
            try {
                if(!cacheFile.createNewFile()) {
                    Log.d(TAG, "文件创建失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(String fileName, Bitmap bitmap) {

    }
}
