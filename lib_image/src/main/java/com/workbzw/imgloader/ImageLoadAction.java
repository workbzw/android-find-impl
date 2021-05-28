package com.workbzw.imgloader;

import android.widget.ImageView;

import com.workbzw.baseannotation.Action;
import com.workbzw.componentframe.IAction;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/5/19 8:44 AM
 * @desc
 */
@Action(actor = ImageLoadGlide.class, name = "ImageLoad", note = "图片加载")
public interface ImageLoadAction extends IAction {
    void loadImage(ImageView imageView, String url);
}
