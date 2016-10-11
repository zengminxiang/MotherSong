package com.fanstech.mothersong.video.util;

/**
 * Created by Administrator on 2016/2/2 0002.
 * 搜索本地视频
 */

import android.graphics.Bitmap;

public class LoadedImage {
    Bitmap mBitmap;

    public LoadedImage(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}