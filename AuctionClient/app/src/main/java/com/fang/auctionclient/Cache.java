package com.fang.auctionclient;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

/**
 * Created by Administrator on 2016/1/1.
 */
public class Cache {
    private static LruCache<String,Bitmap> mCache;

    public Cache(){
        setmCache();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static void setmCache() {
        //获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //每次存入缓存的大小
                return value.getByteCount();
            }
        };
    }

    public static LruCache<String, Bitmap> getmCache() {
        return mCache;
    }
}
