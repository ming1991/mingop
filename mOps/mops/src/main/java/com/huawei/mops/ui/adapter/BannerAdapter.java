package com.huawei.mops.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.huawei.mops.R;
import com.huawei.mops.util.Constants;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;
import java.util.List;

/**
 * Created by Long
 * on 2016/9/21.
 */

public class BannerAdapter extends PagerAdapter {

    private final DisplayImageOptions options;
    private List<String> imageUrls;
    private Context mContext;
    private int lastPosition;

    public BannerAdapter(Context context, List<String> imageUrls) {
        this.mContext = context;
        this.imageUrls = imageUrls;
        File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils
                .getOwnCacheDirectory(context,
                        Constants.IMAGE_CACHE_PATH);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.a3)
                .showImageForEmptyUri(R.drawable.a3)
                .showImageOnFail(R.drawable.a3)
                .resetViewBeforeLoading(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
                .discCache(new UnlimitedDiscCache(cacheDir))
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.loading);
        //如果下标是0,
//        if (position == 0) {
//            //将下标指向图五
//            position = imageUrls.size() - 1;
//            //如果是最后一个下标
//        } else if (position == getCount() - 1) {
//            //将下标指向图一
//            position = 0;
//        } else {
//            //上面两步将左右两边的下标指向了图一和图五
//            //我们正常的图片从下标一开始,所以需要-1
//            position -= 1;
//        }
        ImageLoader.getInstance().displayImage(imageUrls.get(position), imageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "Input/Output error";
                        break;
                    case DECODING_ERROR:
                        message = "Image can't be decoded";
                        break;
                    case NETWORK_DENIED:
                        message = "Downloads are denied";
                        break;
                    case OUT_OF_MEMORY:
                        message = "Out Of Memory error";
                        break;
                    case UNKNOWN:
                        message = "Unknown error";
                        break;
                }
                Log.e("huawei", "图片加载失败 failReason : " + message);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                super.onLoadingCancelled(imageUri, view);
                spinner.setVisibility(View.GONE);
            }
        });
        container.addView(view);
        return view;
    }
}
