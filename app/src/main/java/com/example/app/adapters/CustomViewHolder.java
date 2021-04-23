package com.example.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.app.utils.AppConfig;
import com.ms.banner.holder.BannerViewHolder;

public class CustomViewHolder implements BannerViewHolder {
    private static RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));//图片圆角为30

    @Override
    public View createView(Context context, int position, Object data) {
        // 返回mImageView页面布局
        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(data).apply(AppConfig.options).into(imageView);
       // Log.e("TAG", "createView: ---------------------------------" );
        return imageView;
    }
}
