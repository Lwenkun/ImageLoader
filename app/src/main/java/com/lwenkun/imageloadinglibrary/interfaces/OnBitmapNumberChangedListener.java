package com.lwenkun.imageloadinglibrary.interfaces;

import android.graphics.Bitmap;

/**
 * Created by 15119 on 2015/11/9.
 */
public interface OnBitmapNumberChangedListener {

    void onAddItem(int position, Bitmap bitmap);

    void onDeleteItem(int positon);

    int onGetBitmapNum();
}
