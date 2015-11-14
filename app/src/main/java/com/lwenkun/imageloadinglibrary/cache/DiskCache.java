package com.lwenkun.imageloadinglibrary.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 15119 on 2015/11/10.
 */
public class DiskCache {

    final String TAG = "DiskCacheTask";

    private final Context context;

    private final String cacheFileDirName;

    public DiskCache(Context context, String cacheFileDirName) {
        this.context = context;
        this.cacheFileDirName = cacheFileDirName;
    }

    public File getCacheFileDir(String cacheFileDirName) {

        String state = Environment.getExternalStorageState();

        File cacheFileDir;

        if(Environment.MEDIA_MOUNTED.equals(state)) {
            cacheFileDir = new File(Environment.getExternalStorageDirectory(), cacheFileDirName);
        } else {
            cacheFileDir = new File(context.getFilesDir(), cacheFileDirName);
        }

        return cacheFileDir;
    }

    public void save(int index, Bitmap bitmap) {
        //在缓存目录下创建缓存文件
        File cacheFile = new File(getCacheFileDir(cacheFileDirName), String.valueOf(index));
        //创建图片缓存文件
        Log.d("testFile", "successful");
        if(!cacheFile.exists()) {
            try {
                if(cacheFile.createNewFile()) {
                    Log.d(TAG, "文件创建成功！");
                    FileOutputStream out = new FileOutputStream(cacheFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public Bitmap getBitmapFromDiskCache(int index) {

        Bitmap bitmap;

        File cacheFile = new File(getCacheFileDir(cacheFileDirName), String.valueOf(index));

        try {
            FileInputStream fileInputStream = new FileInputStream(cacheFile);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return bitmap;
    }
}
