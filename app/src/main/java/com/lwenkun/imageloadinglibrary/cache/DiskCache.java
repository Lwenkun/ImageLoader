package com.lwenkun.imageloadinglibrary.cache;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by 15119 on 2015/11/10.
 */
public class DiskCache {

    private static DiskCache diskCache;

    private Context context;

    private DiskCache(Context context) {
        this.context = context;
    }

    public static DiskCache getInstance(Context context) {

        if (diskCache == null || ! context.equals(diskCache.context)) {

            diskCache = new DiskCache(context);

        }

        return diskCache;
    }


    /**
     *
     * @param cacheDirName 要保存的文件目录的名称
     * @param cacheFileName 要保存的文件名称
     * @param is 要保存的内容的InputStream
     */
    public void save(String cacheDirName, String cacheFileName, InputStream is) {

        FileOutputStream fileOutputStream = null;

        File cacheFile = new File(getCacheDir(cacheDirName), cacheFileName);

        try {
            //创建缓存文件并创建文件输出流
            if (cacheFile.createNewFile()) {
                fileOutputStream = new FileOutputStream(cacheFile);
            } else {
                Toast.makeText(context, "缓存文件创建失败，请检查磁盘空间是否充足", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("bug", "文件创建失败");
        }

        BufferedOutputStream bos = null;

        if (fileOutputStream != null) {

             bos = new BufferedOutputStream(fileOutputStream);
        }

        BufferedInputStream bis = new BufferedInputStream(is);
        try {

            int b;

            if (bos != null)
            while ((b = bis.read()) != -1) {

                bos.write(b);
            } else {
                Toast.makeText(context, "缓存文件创建失败，请检查磁盘空间是否充足", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
                e.printStackTrace();
            Log.d("bug", "文件写入错误");
            }

    }


    private File getCacheDir(String cacheDirName) {

        File cacheDir;

        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cacheDir = new File(context.getExternalCacheDir(), cacheDirName);
        } else {
            cacheDir = new File(context.getCacheDir(), cacheDirName);
        }

        return cacheDir;
    }

    public boolean clearCacheDir(String cacheDirName) {

        File fileToDelete = getCacheDir(cacheDirName);

        return fileToDelete.delete();
    }

    public FileOutputStream get(String cacheDirName, String cacheFileName) {

        File file = new File(getCacheDir(cacheDirName), cacheFileName);
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("bug", "无法获取缓存文件");
        }

        return out;
    }

}
