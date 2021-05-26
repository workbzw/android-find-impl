package com.workbzw.imgloader;

import android.widget.ImageView;

import com.workbzw.componentframe.IService;

/**
 * @Author bzw [workbzw@outlook.com]
 * @CreateDate: 2021/5/19 8:44 AM
 * @Description:
 */

public interface ImageLoadService extends IService {
    void loadImage(ImageView imageView, String url);
}
