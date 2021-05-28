package com.workbzw.imgloader;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author bzw [workbzw@outlook.com]
 * @date 2021/5/23 7:57 PM
 * @desc
 */
public class ImageLoadGlide extends BaseImageLoadActionImpl {

    private RequestOptions options;

    public synchronized RequestOptions getOptions() {
        if (options == null) {
            options = new RequestOptions()
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .priority(Priority.HIGH);
        }
        return options;
    }

    @Override
    public void loadImage(ImageView imageView, String url) {
        Glide.with(imageView)
                .asBitmap()
                .load(url)
                .apply(getOptions())
                .into(imageView);
    }
}
