package com.lwenkun.imageloader.cache;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by 15119 on 2015/11/10.
 */
public class DiskCache {

    private static DiskCache diskCache;

    private Context context;

    final byte[] LOCK_WRITE = new byte[0];

    static final byte[] LOCK_INIT = new byte[0];

    final byte[] LOCK_DELETE = new byte[0];

    private File root;

    private DiskCache(Context context, String cacheDirName) {

        this.context = context;
        root = getCacheDir(cacheDirName);
        root.mkdir();
    }

    /**
     * @param context      context
     * @param cacheDirName 缓存目录名
     * @return 磁盘缓存实例
     */
    public static DiskCache getInstance(Context context, String cacheDirName) {

        if (diskCache == null) {

            synchronized (LOCK_INIT) {
                if (diskCache == null) {

                    diskCache = new DiskCache(context, cacheDirName);
                }
            }
        }
        return diskCache;

    }

    /**
     * @param cacheFileName 要保存的文件名称
     * @param bytes         要保存的内容的InputStream
     */
    public void save(String cacheFileName, byte[] bytes) {

        synchronized (LOCK_WRITE) {

            File cacheFile = new File(root, cacheFileName);

            try {
                if (cacheFile.exists()) {
                    delete(cacheFile);
                }
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cacheFile));

                bos.write(bytes, 0, bytes.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param cacheDirName 缓存文件名
     * @return 缓存目录的File对象
     */
    public File getCacheDir(String cacheDirName) {

        File cacheDir;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cacheDir = new File(context.getExternalCacheDir(), cacheDirName);
        } else {
            cacheDir = new File(context.getCacheDir(), cacheDirName);
        }

        return cacheDir;
    }


    public InputStream getStream(String cacheFileName) {

        File file = new File(root, cacheFileName);

        BufferedInputStream bis = null;
        FileInputStream fis;

        try {
            if (!file.exists()) return null;

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);

        } catch (Exception e) {

            e.printStackTrace();
            Log.d("bug", "无法获取缓存文件");
        }

        return bis;
    }


    public void delete(File file) {

        synchronized (LOCK_DELETE) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

}
