package com.fang.auctionclient.netutil;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.fang.auctionclient.Cache;
import com.fang.auctionclient.CustomApplication;
import com.fang.auctionclient.R;
import com.fang.auctionclient.adapter.ListAdapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/12/25.
 */
public class ImageLoader {

    private ImageView mImageView;
    private String mUrl;
    private ListView mListView;
    private Set<MyAsyncTask> mTask;
    private ListAdapter listAdapter;
    //创建Cache，本质是一个map
    private final static LruCache<String,Bitmap> mCache =
            new Cache().getmCache();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public ImageLoader(ListView listView,ListAdapter listAdapter){
        this.listAdapter = listAdapter;
        this.mListView = listView;
        mTask = new HashSet<>();
        //获取最大可用内存
//        int maxMemory = (int) Runtime.getRuntime().maxMemory();
//        int cacheSize = maxMemory / 4;
//        mCache = new LruCache<String,Bitmap>(cacheSize){
//            @Override
//            protected int sizeOf(String key, Bitmap value) {
//                //每次存入缓存的大小
//                return value.getByteCount();
//            }
//        };
    }

    // 增加到缓存
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void addBitmapToCache(String url, Bitmap bitmap)
    {
        if (getBitmapFromCache(url) == null)
        {
            mCache.put(url, bitmap);
        }
    }

    //从缓存中获取数据
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public Bitmap getBitmapFromCache(String url)
    {
        return mCache.get(url);
    }

    //利用AsyncTask来下载图片
    public void showImageByAsyncTask(ImageView imageView,String url){
        Bitmap bitmap = getBitmapFromCache(url);
        if(bitmap == null){
//            new MyAsyncTask(url).execute(url);
            //取消这里的加载图片任务，只有在不滑动的状态才加载图片
            imageView.setImageResource(R.mipmap.ic_launcher);
        }else {
            imageView.setImageBitmap(bitmap);
        }
    }

    public void loadImages(int firstVisibleItem,int visibleItemCount){
        for(int i = firstVisibleItem;i < firstVisibleItem + visibleItemCount-1;i++){
            String url = listAdapter.getUrl(i);
            Bitmap bitmap = getBitmapFromCache(url);
            if(bitmap == null){
                MyAsyncTask task = new MyAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else {
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                if(imageView != null){
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    public void cancelAllTasks(){
        if(mTask != null){
            for(MyAsyncTask task : mTask){
                task.cancel(false);
            }
        }
    }

    private class MyAsyncTask extends AsyncTask<String ,Void ,Bitmap>{

//        private ImageView mImageView;
        private String mUrl;

        MyAsyncTask(String url){
//            this.mImageView = imageView;
            this.mUrl = url;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bitmap = getBitmapFromUrl(url);
            if(bitmap != null){
                addBitmapToCache(url,bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            if(mImageView.getTag().equals(mUrl)){
//                mImageView.setImageBitmap(bitmap);
//            }
            ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
            if(imageView != null && bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
            mTask.remove(this);
        }
    }

    private Bitmap getBitmapFromUrl(String urlName) {
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            URL url=new URL(urlName);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5*1000);
            conn.setReadTimeout(10 * 1000);
            is=new BufferedInputStream(conn.getInputStream());
            bitmap= BitmapFactory.decodeStream(is);
            conn.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
