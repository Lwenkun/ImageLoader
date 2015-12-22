package com.lwenkun.imageloader.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.lwenkun.imageloader.cache.DiskCache;
import com.lwenkun.imageloader.cache.ImageLruCache;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 15119 on 2015/11/9.
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    private WeakReference<ImageView> imageViewWeakReference;

    private BitmapWorker bitmapWorker;

    private DiskCache diskCache;

    private ImageLruCache imageLruCache;

    public final String CACHE_DIR_NAME = "bitmap";

    private String sUrl;


    public BitmapWorkerTask(BitmapWorker bitmapWorker, Context context, ImageView imageView) {

        this.imageViewWeakReference = new WeakReference<>(imageView);
        this.diskCache = DiskCache.getInstance(context, CACHE_DIR_NAME);
        this.imageLruCache = ImageLruCache.getInstance();
        this.bitmapWorker = bitmapWorker;
    }


    @Override
    protected void onPreExecute() {

        super.onPreExecute();

    }

    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);

    }

    //从网络获取bitmap
    @Override
    protected Bitmap doInBackground(String... params) {

        sUrl = params[0];
        String cacheFileName = MD5.hashKeyForDisk(sUrl);

        //从磁盘缓存中获取文件
        Bitmap bitmap = getBitmapFromDiskCache(cacheFileName);

        if (bitmap == null) bitmap = downloadBitmap(sUrl);

        return bitmap;
    }

    public Bitmap getBitmapFromDiskCache(String cacheFileName) {

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(diskCache.getStream(cacheFileName), null, options);

        options.inSampleSize = calInSampleSize(options, 100, 100);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(diskCache.getStream(cacheFileName), null, options);
    }

    //从is中获取指定大小的bitmap
    public Bitmap decodeSampleFromBytes(byte[] bytes) {

        BitmapFactory.Options options = new BitmapFactory.Options();

       // 计算出bitmap的实际大小
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        //计算缩小倍数
        options.inSampleSize = calInSampleSize(options, 100, 100);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    //计算合适的缩小倍数
        public int calInSampleSize(BitmapFactory.Options options, int requestWidth, int requestHeight) {

        int inSampleSize = 1;

        final int width = options.outWidth;
        final int height = options.outHeight;

        if (height > requestHeight || width > requestWidth) {

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

        if (isCancelled()) {
            bitmap = null;
        }

        if (bitmap != null && imageViewWeakReference != null) {

            //存入内存缓存
            imageLruCache.put(sUrl, bitmap);

            ImageView imageView = imageViewWeakReference.get();
            BitmapWorkerTask bitmapWorkerTask = bitmapWorker.getBitmapWorkTask(imageView);
            if (imageView != null && bitmapWorkerTask == this) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    //将图片下载下来并且保存到磁盘缓存
    public Bitmap downloadBitmap(String sUrl) {

        Bitmap bitmap = null;
        HttpURLConnection conn = null;

        try {
            URL url = new URL(sUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setReadTimeout(8000);
            conn.setConnectTimeout(8000);
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();

            BufferedInputStream bos = new BufferedInputStream(is, 1024);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int length;
            byte[] bytes = new byte[1024];
            while((length = bos.read(bytes)) != -1) {
                baos.write(bytes, 0, length);
            }

            byte[] byteArray = baos.toByteArray();

            String cacheFileName = MD5.hashKeyForDisk(sUrl);
            diskCache.save(cacheFileName, byteArray);

            bitmap = decodeSampleFromBytes(byteArray);

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }

        return bitmap;
    }


    public String getUrl() {
        return sUrl;
    }

}
