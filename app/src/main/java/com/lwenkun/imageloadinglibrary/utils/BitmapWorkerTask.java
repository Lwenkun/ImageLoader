package com.lwenkun.imageloadinglibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

    @Override
    protected Bitmap doInBackground(String... params) {
        data = params[0];

        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.getDoInput();
            conn.setReadTimeout(8000);
            conn.setConnectTimeout(8000);
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            return BitmapFactory.decodeStream(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        diskCache.save(data, bitmap);
        imageLruCache.put(data, bitmap);
        imageView.setImageBitmap(bitmap);
    }

    public String getData() {
        return data;
    }

}
