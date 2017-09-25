package com.cdhxqh.inventorymovement.wight;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cdhxqh.inventorymovement.R;
import com.lzy.imagepicker.loader.ImageLoader;


public class GlideImageLoader implements ImageLoader {
    private static final String TAG="GlideImageLoader";
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Log.i(TAG,"path="+path);
        Glide.with(activity)                             //配置上下文
                .load(path)      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .error(R.mipmap.default_image)           //设置错误图片
                .placeholder(R.mipmap.default_image)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
