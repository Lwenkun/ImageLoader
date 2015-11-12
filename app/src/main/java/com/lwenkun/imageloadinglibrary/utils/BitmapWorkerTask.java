package com.lwenkun.imageloadinglibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.lwenkun.imageloadinglibrary.cache.DiskCache;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 15119 on 2015/11/9.
 */
public class BitmapWorkerTask extends AsyncTask<String, Integer, Bitmap> {

    private ImageView imageView;

    private final LruCache<String, Bitmap> imageLruCache;

    private final DiskCache diskCache;

    public BitmapWorkerTask(ImageView imageView, LruCache<String, Bitmap> imageLruCache, DiskCache diskCache) {
        this.imageView = imageView;
        this.imageLruCache = imageLruCache;
        this.diskCache = diskCache;
    }

    private String data;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    //从网络获取bitmap
    @Override
    protected Bitmap doInBackground(String... params) {
        data = params[0];

        Bitmap bitmap;
        bitmap = diskCache.getBitmapFromDiskCache(data);

        if( bitmap == null)
        {
                bitmap = imageLruCache.get(data);
        }

        if(bitmap == null) {
            try {
                URL url = new URL(params[0]);
                Log.d("url", "error");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.getDoInput();
                conn.setReadTimeout(1000);
                conn.setConnectTimeout(1000);
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                return decodeBitmapFromInputStream(is);

            } catch (Exception e) {
                Log.d("BitmapTask", "i am failed");
                e.printStackTrace();
            }
        }

        return null;
    }

    //从is中获取指定大小的bitmap
    public Bitmap decodeBitmapFromInputStream(InputStream is) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //计算出bitmap的实际大小
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        //计算缩小倍数
        options.inSampleSize = calInSampleSize(options, 100, 100);
        //获取bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }

    //计算合适的缩小倍数
    public int calInSampleSize(BitmapFactory.Options options, int requestWidth, int requestHeight) {

        int inSampleSize = 1;

        final int width = options.outWidth;
        final int height = options.outHeight;

        if(height > requestHeight || width > requestWidth) {

            int halfHeight = height / 2;
            int halfWidth = width / 2;

            while (halfHeight / inSampleSize > requestHeight || halfWidth / inSampleSize > requestWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    //将获得的bitmap先保存到Cache,再传给ImageView
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        saveBitmapToCache(bitmap);

        imageView.setImageBitmap(bitmap);
    }

    //分别保存到磁盘和内存
    public void saveBitmapToCache(Bitmap bitmap) {
        diskCache.save(data, bitmap);
        imageLruCache.put(data, bitmap);
    }

    public String getData() {
        return data;
    }

}
