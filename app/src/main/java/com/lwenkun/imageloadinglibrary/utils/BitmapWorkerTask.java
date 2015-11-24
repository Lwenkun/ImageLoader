package com.lwenkun.imageloadinglibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.lwenkun.imageloadinglibrary.cache.DiskCache;
import com.lwenkun.imageloadinglibrary.provider.Images;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 15119 on 2015/11/9.
 */
public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

    private ImageView imageView;

    private DiskCache diskCache = DiskCache.getInstance();

    public BitmapWorkerTask(ImageView imageView) {
        this.imageView = imageView;
    }

    private int index;

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

    }

    @Override
    protected void onProgressUpdate(Void...values) {

        super.onProgressUpdate(values);

    }

    //从网络获取bitmap
    @Override
    protected Bitmap doInBackground(Integer... params) {

        index = params[0];

        Bitmap bitmap = Common.imageLruCache.get(index);

        if( bitmap == null) bitmap = BitmapFactory.decodeStream(diskCache.get(index));

        if(bitmap == null) {

            try {
                URL url = new URL(Images.imageUrls[index]);
                Log.d("url", Images.imageUrls[index]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setReadTimeout(8000);
                conn.setConnectTimeout(8000);
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                diskCache.save("bitmap", MD5.);
                bitmap = decodeBitmapFromInputStream(is);
                Log.d("test", "here");

            } catch (Exception e) {
                Log.d("BitmapTask", "i am failed");
                e.printStackTrace();
            }

        }

        return bitmap;
    }

    //从is中获取指定大小的bitmap
    public Bitmap decodeBitmapFromInputStream(InputStream is) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //计算出bitmap的实际大小
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        //计算缩小倍数
        Log.d("width", "width" + options.outWidth + "height" + options.outHeight );
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

        if(bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.d("hashcode", "hashcode" + imageView.hashCode());
            saveBitmapToCache(bitmap);
        } else {
            Log.d("test", "bitmap is null");

        }
    }

    //分别保存到磁盘和内存
    public void saveBitmapToCache(Bitmap bitmap) {
        if(diskCache.get(index) == null) {
            diskCache.save(index, bitmap);
        }
        imageLruCache.put(index, bitmap);
    }

    public int getIndex() {
        return index;
    }

}
