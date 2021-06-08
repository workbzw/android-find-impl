package com.workbzw.imgloader;

import android.widget.ImageView;

import com.workbzw.baseframe.IService;
import com.workbzw.baseframe.annotation.Service;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/5/19 8:44 AM
 * @desc
 */
public interface ServiceImageLoad extends IService {
    void loadImage(ImageView imageView, String url);
}
