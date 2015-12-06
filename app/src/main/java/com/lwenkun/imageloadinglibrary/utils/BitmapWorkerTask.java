package com.lwenkun.imageloadinglibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.lwenkun.imageloadinglibrary.cache.DiskCache;
import com.lwenkun.imageloadinglibrary.cache.ImageLruCache;

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

    private DiskCache diskCache;

    private ImageLruCache imageLruCache;

    public final String CACHE_DIR_NAME = "bitmap";

    private String sUrl;

    private String cacheFileName;


    public BitmapWorkerTask(Context context, ImageView imageView) {

        imageViewWeakReference = new WeakReference<>(imageView);
        diskCache = DiskCache.getInstance(context, CACHE_DIR_NAME);
        imageLruCache = ImageLruCache.getInstance();
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
            cacheFileName = MD5.hashKeyForDisk(sUrl);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(diskCache.getCacheFilePath(cacheFileName), options);

            if (bitmap != null)
                imageLruCache.put(sUrl, bitmap);
            else
                bitmap = downloadBitmap(sUrl);
            if (bitmap != null) {
                imageLruCache.put(sUrl, bitmap);
            }

        return bitmap;
    }

    //从is中获取指定大小的bitmap
    public Bitmap decodeSampleFromBytes(byte[] bytes) {

        BitmapFactory.Options options = new BitmapFactory.Options();

//       // 计算出bitmap的实际大小
//
//        options.inJustDecodeBounds = true;
//
//        BitmapFactory.decodeStream(is, null, options);
//
//        //计算缩小倍数
//        Log.d("size", "width" + options.outWidth + "height" + options.outHeight);
//        options.inSampleSize = calInSampleSize(options, 100, 100);
//        Log.d("inSampleSize", "" + options.inSampleSize);
//
//       // 获取bitmap

        options.inSampleSize = 8;
        options.inJustDecodeBounds = false;

//        try {
//            is = conn.getInputStream();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        if(bitmap == null) {
            Log.d("bitmap", "为空");
        }
        return bitmap;

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

            ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

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
            byte bytes[] = new byte[1024];

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int length;
            while((length = bos.read(bytes)) != -1) {
                baos.write(bytes, 0, length);
            }

            byte[] bytes1 = baos.toByteArray();
            Log.d("bytes", length+"");

            if(!isCancelled()) {
                String cacheFileName = MD5.hashKeyForDisk(sUrl);
                diskCache.save(cacheFileName, bytes1);
            }
            bitmap = decodeSampleFromBytes(bytes1);

           // conn = (HttpURLConnection) url.openConnection();

        } catch (Exception e) {
            Log.d("BitmapTask", "failed");
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
