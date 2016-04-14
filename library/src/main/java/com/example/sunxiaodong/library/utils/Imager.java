package com.example.sunxiaodong.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.io.File;
import java.io.IOException;

/**
 * 本地图片加载器
 * Created by sunxiaodong on 2015/12/21.
 */
public class Imager {

    private static final String TAG = Imager.class.getSimpleName();

    private static final int RES_NO = -1;//没有资源
    private static final int ROUND_NO = 0;//没有圆角

    private static Imager sInstance = null;
    private Context mContext;
    private Handler mMainHandler;
    private String mDiskCacheDir;

    private Imager(Context context, String diskCacheDir) {
        mContext = context;
        mDiskCacheDir = diskCacheDir;
        initData();
    }

    public synchronized static Imager getInstance() {
        if (sInstance == null) {
            throw new ExceptionInInitializerError("Imager not init!");
        }
        return sInstance;
    }

    public synchronized static void init(Context context, String diskCacheDir) {
        if (sInstance == null) {
            sInstance = new Imager(context, diskCacheDir);
        }
    }

    private void initData() {
        mMainHandler = new Handler(Looper.getMainLooper());
        initImageLoader();
    }

    private void initImageLoader() {
        final int THREAD_POOL_SIZE = 4;
        final int MEM_CACHE_SIZE = 5 * 1024 * 1024; //5M
        final int DISK_CACHE_SIZE = 100 * 1024 * 1024;//100M
        final int DISK_CACHE_FILE_SIZE = 1000;//sd卡上缓存文件数
        final int IMG_DOWNLOAD_CONNECT_TIME_OUT = 5 * 1000;//5s
        final int IMG_DOWNLOAD_READ_TIME_OUT = 30 * 1000;//30s
        File diskCacheDirFile = new File(mDiskCacheDir);

        ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(mContext);

        configBuilder.threadPoolSize(THREAD_POOL_SIZE)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .memoryCacheSize(MEM_CACHE_SIZE)
//                    .memoryCache(new LruMemoryCache(MEM_CACHE_SIZE))
                .memoryCache(new WeakMemoryCache()).imageDownloader(
                new BaseImageDownloader(mContext,
                        IMG_DOWNLOAD_CONNECT_TIME_OUT,
                        IMG_DOWNLOAD_READ_TIME_OUT))
                .defaultDisplayImageOptions(
                        DisplayImageOptions.createSimple())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs();
        try {
            configBuilder.diskCache(
                    new LruDiskCache(diskCacheDirFile, null,
                            new FileNameGenerator() {
                                @Override
                                public String generate(String imageUri) {
                                    return getFileName(imageUri);
                                }
                            }, DISK_CACHE_SIZE, DISK_CACHE_FILE_SIZE));
        } catch (IOException e) {
            e.printStackTrace();
            configBuilder.diskCache(new UnlimitedDiskCache(diskCacheDirFile));
        }

        ImageLoader.getInstance().init(configBuilder.build());//全局初始化此配置
    }

    /**
     * <br>功能简述:SD卡缓存文件名生成
     *
     * @param imgUrl
     */
    public String getFileName(String imgUrl) {
        String imgName = MD5Util.md5(imgUrl);
        if (LocalTextUtil.isNoBlank(imgName)) {
            return imgName;
        }
        return Integer.toString(imgUrl.hashCode());
    }

    private DisplayImageOptions getOptions(boolean memCache, boolean diskCache, int errorRes, int loadingRes, final int roundSize) {
        DisplayImageOptions options = null;
        if (loadingRes == RES_NO && errorRes == RES_NO) {
            options = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(memCache)
                    .cacheOnDisk(diskCache).bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new FadeInBitmapDisplayer(100)) // 展现方式：渐现
                    .preProcessor(new BitmapProcessor() {

                        @Override
                        public Bitmap process(Bitmap bitmap) {
                            return getBitmap(bitmap, roundSize);
                        }
                    }).build();//构建完成
        } else if (loadingRes == RES_NO) {
            options = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .showImageForEmptyUri(errorRes)
                    .showImageOnFail(errorRes).cacheInMemory(memCache)
                    .cacheOnDisk(diskCache).bitmapConfig(Bitmap.Config.RGB_565)
                    .preProcessor(new BitmapProcessor() {

                        @Override
                        public Bitmap process(Bitmap bitmap) {
                            return getBitmap(bitmap, roundSize);
                        }
                    }).build();//构建完成
        } else if (errorRes == RES_NO) {
            options = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .showImageOnLoading(loadingRes)
                    .cacheInMemory(memCache)
                    .cacheOnDisk(diskCache).bitmapConfig(Bitmap.Config.RGB_565)
                    .preProcessor(new BitmapProcessor() {

                        @Override
                        public Bitmap process(Bitmap bitmap) {
                            return getBitmap(bitmap, roundSize);
                        }
                    }).build();//构建完成
        } else {
            options = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .showImageOnLoading(loadingRes)
                    .showImageForEmptyUri(errorRes)
                    .showImageOnFail(errorRes).cacheInMemory(memCache)
                    .cacheOnDisk(diskCache).bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new SimpleBitmapDisplayer()) // 展现方式：渐现
                    .preProcessor(new BitmapProcessor() {

                        @Override
                        public Bitmap process(Bitmap bitmap) {
                            return getBitmap(bitmap, roundSize);
                        }
                    }).build();//构建完成
        }
        return options;
    }

    private Bitmap getBitmap(Bitmap bitmap, int roundSize) {
        try {
            return BitmapUtil.toRoundCorner(bitmap, roundSize);//把ByteArrayInputStream数据生成图片
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public void loadImage(ImageView imgView, String imgUrl, int loadingRes, int errorRes, final int roundSize, boolean cache, ImageLoadingListenerAdapter imageLoadListener) {
        //判断本次加载是网络加载还是本地加载
        boolean isUrl = RegexFormatUtil.isUrl(imgUrl);
        if (LocalTextUtil.isNoBlank(imgUrl) && !isUrl && !imgUrl.startsWith("file://")) {
            //本地加载，附加前缀
            imgUrl = "file://" + imgUrl;
        }
        DisplayImageOptions options = getOptions(cache, cache, errorRes, loadingRes, roundSize);
        ImageLoader.getInstance().displayImage(imgUrl, imgView, options, imageLoadListener);
    }

    //==============================================================全部使用缓存方法==================================================start

    public void loadImage(ImageView imgView, String imgUrl) {
        loadImage(imgView, imgUrl, RES_NO, RES_NO, ROUND_NO, true, null);
    }

    public void loadCornerImage(ImageView imgView, String imgUrl, int roundSize) {
        loadImage(imgView, imgUrl, RES_NO, RES_NO, roundSize, true, null);
    }

    public void loadImage(ImageView imgView, String imgUrl, int errorRes) {
        loadImage(imgView, imgUrl, RES_NO, errorRes, ROUND_NO, true, null);
    }

    public void loadImage(ImageView imgView, String imgUrl, int errorRes, ImageLoadingListenerAdapter imageLoadingListener) {
        loadImage(imgView, imgUrl, RES_NO, errorRes, ROUND_NO, true, imageLoadingListener);
    }

    public void loadCornerImage(ImageView imgView, String imgUrl, int errorRes, int roundSize) {
        loadImage(imgView, imgUrl, RES_NO, errorRes, roundSize, true, null);
    }

    public void loadImage(ImageView imgView, String imgUrl, int loadingRes, int errorRes) {
        loadImage(imgView, imgUrl, loadingRes, errorRes, ROUND_NO, true, null);
    }

    public void loadCornerImage(ImageView imgView, String imgUrl, int loadingRes, int errorRes, int roundSize) {
        loadImage(imgView, imgUrl, loadingRes, errorRes, roundSize, true, null);
    }

    public void loadCornerImage(ImageView imgView, String imgUrl, int errorRes, int roundSize, ImageLoadingListenerAdapter imageLoadingListener) {
        loadImage(imgView, imgUrl, RES_NO, errorRes, roundSize, true, imageLoadingListener);
    }

    public void loadImage(ImageView imgView, String imgUrl, int loadingRes, int errorRes, ImageLoadingListenerAdapter imageLoadingListener) {
        loadImage(imgView, imgUrl, loadingRes, errorRes, ROUND_NO, true, imageLoadingListener);
    }

    //==============================================================全部使用缓存方法==================================================end
    //==============================================================缓存可设置方法====================================================start

    public void loadImage(ImageView imgView, String imgUrl, int errorRes, boolean cache) {
        loadImage(imgView, imgUrl, RES_NO, errorRes, ROUND_NO, cache, null);
    }

    public void loadImage(ImageView imgView, String imgUrl, int errorRes, boolean cache, ImageLoadingListenerAdapter imageLoadingListener) {
        loadImage(imgView, imgUrl, RES_NO, errorRes, ROUND_NO, cache, imageLoadingListener);
    }

    /**
     * <br>功能简述:提交一个Runable到主线程队列
     */
    private void runOnMainThread(Runnable r) {
        mMainHandler.post(r);
    }

    /**
     * <br>类描述:图片加载结果回调
     *
     * @author xiaodong
     * @date [2015-7-5]
     */
    public interface ImgLoadListener {
        void onSuccess(String imgPath, Bitmap bitmap);

        void onFailed(String imgPath);
    }

    /**
     * <br>类描述:图片加载回调runnable
     *
     * @author xiaodong
     * @date [2015-7-11]
     */
    public class ImageLoadCallBackRunnable implements Runnable {
        private Bitmap mBitmap = null;
        private ImgLoadListener mCallBack = null;
        private boolean mIsSuccess;
        private String mImgPath;

        ImageLoadCallBackRunnable(boolean isSuccess, String imgPath, Bitmap bitmap, ImgLoadListener callBack) {
            mBitmap = bitmap;
            mCallBack = callBack;
            mIsSuccess = isSuccess;
            mImgPath = imgPath;
        }

        @Override
        public void run() {
            if (mCallBack != null) {
                if (mIsSuccess) {
                    mCallBack.onSuccess(mImgPath, mBitmap);
                } else {
                    mCallBack.onFailed(mImgPath);
                }
            }
        }
    }

}
