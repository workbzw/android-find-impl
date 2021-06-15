package com.workbzw.service.image;

import android.util.Log;

import com.workbzw.lib.base.annotation.Service;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/6/10 8:27 PM
 * @desc
 */
@Service
public class ImageLoaderGlide implements ImageLoadService {
    private static final String TAG = "ImageLoaderGlide";

    @Override
    public void loadImage() {
        Log.i(TAG, "loadImage: " + "我是ImageLoadService的Glide实现类的loadImage()方法！！");
    }
}
