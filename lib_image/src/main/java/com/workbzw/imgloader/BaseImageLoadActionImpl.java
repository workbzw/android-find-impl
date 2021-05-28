package com.workbzw.imgloader;

/**
 * @Author bzw [workbzw@outlook.com]
 * @CreateDate: 2021/5/23 7:48 PM
 * @Description:
 */
public abstract class BaseImageLoadActionImpl implements ImageLoadAction {
    @Override
    public String name() {
        return "ImageLoaderService";
    }

}
