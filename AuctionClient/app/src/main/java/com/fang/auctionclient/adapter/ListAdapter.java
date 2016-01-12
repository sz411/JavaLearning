package com.fang.auctionclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fang.auctionclient.R;
import com.fang.auctionclient.bean.ContentBean;
import com.fang.auctionclient.netutil.ImageDownLoader;
import com.fang.auctionclient.netutil.ImageLoader;
import com.fang.auctionclient.ui.WebActivity;
import com.fang.auctionclient.util.RefreshableView;

import java.util.List;

/**
 * Created by Administrator on 2015/12/24.
 */
public class ListAdapter extends BaseAdapter implements AbsListView.OnScrollListener,AdapterView.OnItemClickListener{

    private Context context;
    private LayoutInflater mInflater;
    public List<ContentBean> mList;
//    public ImageLoader mImageLoader;
    private ImageDownLoader mImageDownLoader;
    private int mFirstVisibleItem, mVisibleItemCount;
    private boolean mFirstIn;
    private ViewHolder viewHolder;
    private int lastItemIndex;//当前ListView中最后一个Item的索引
    private onLoad mOnLoad;
    public boolean isOnload = false;
    public boolean isNoMore = false;
    private View footer;
    private ListView mListView;

    public ListAdapter(Context context,List<ContentBean> mList,ListView listView){
        this.context = context;
        this.mList = mList;
        mInflater = LayoutInflater.from(context);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
        this.mListView = listView;
//        mImageLoader = new ImageLoader(listView,this);
        mImageDownLoader = new ImageDownLoader(context);
        mFirstIn = true;
//        footer = mInflater.inflate(R.layout.pull_to_load,null);
//        LinearLayout linearLayout = new LinearLayout(context);
//        linearLayout.addView(footer);
        RelativeLayout relativeLayout = (RelativeLayout) mInflater.inflate(R.layout.pull_to_load,null);
        footer = relativeLayout.findViewById(R.id.pull_to_refresh_head_inside);
        listView.addFooterView(relativeLayout);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.listitem_wf,null);
            viewHolder=new ViewHolder();
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_list);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_list);
            viewHolder.ib = (Button) convertView.findViewById(R.id.ib_list);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.iv.setImageResource(R.mipmap.ic_launcher);
        /**
         * 绑定图片和图片的URL，tag
         */
        String url=mList.get(position).getImageurl();
        viewHolder.iv.setTag(url);
//        mImageLoader.showImageByAsyncTask(viewHolder.iv, url);

        Bitmap bitmap = mImageDownLoader.showCacheBitmap(mList.get(position).getImageurl().replaceAll("[^\\w]", ""));
        if(bitmap != null){
            viewHolder.iv.setImageBitmap(bitmap);
        }else{
//            mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_empty));
        }

        viewHolder.tv.setText(mList.get(position).getTitle());
        viewHolder.ib.setText(mList.get(position).getLoveNum());
        viewHolder.ib.setOnClickListener(new lvButtonListener(position));
        return convertView;
    }

    public String getUrl(int i){
        String url = "";
        url = mList.get(i).getImageurl();
        return url;
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE){
            //加载可见项
//            mImageLoader.loadImages(mFirstVisibleItem, mVisibleItemCount);
            showImage(mFirstVisibleItem, mVisibleItemCount);
        }else{
            //停止任务
//            mImageLoader.cancelAllTasks();
            mImageDownLoader.cancelTask();
        }
        if (scrollState == SCROLL_STATE_IDLE
                && lastItemIndex == this.getCount() - 1) {
            //加载数据代码
            if(isNoMore){
                footer.setVisibility(View.GONE);
                Toast.makeText(context,"没有更多数据了",Toast.LENGTH_SHORT).show();
            }else {
                if (!isOnload) {
                    mOnLoad.onLoadingMore();
                    isOnload = true;
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        if(mFirstIn && visibleItemCount > 0){
            showImage(mFirstVisibleItem, mVisibleItemCount);
//            mImageLoader.loadImages(mFirstVisibleItem, mVisibleItemCount);
            mFirstIn = false;
        }
        lastItemIndex = firstVisibleItem + visibleItemCount - 1 -1;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(context,WebActivity.class);
        intent.putExtra("url",mList.get(position).getUrl());
        context.startActivity(intent);
    }

    class lvButtonListener implements View.OnClickListener{

        private int position ;

        lvButtonListener(int pos){
            position = pos;
        }

        @Override
        public void onClick(View v) {
            int vid= v. getId ( ) ;
            if(vid == viewHolder.ib.getId()){
                Log.v("aaa","点击了第几个赞"+position);
            }
        }
    }

    public class ViewHolder{
        public ImageView iv;
        public TextView tv;
        public Button ib;
    }

    public void setOnLoadListener(onLoad onLoad){
        this.mOnLoad = onLoad;
    }

    public interface onLoad{
        void onLoadingMore();
    }

    /**
     * 显示当前屏幕的图片，先会去查找LruCache，LruCache没有就去sd卡或者手机目录查找，在没有就开启线程去下载
     * @param firstVisibleItem
     * @param visibleItemCount
     */
    private void showImage(int firstVisibleItem, int visibleItemCount){
        Bitmap bitmap = null;
        //减去1是减掉footer的1
        for(int i=firstVisibleItem; i<firstVisibleItem + visibleItemCount-1; i++){
            String mImageUrl = mList.get(i).getImageurl();
            final ImageView mImageView = (ImageView) mListView.findViewWithTag(mImageUrl);
            bitmap = mImageDownLoader.downloadImage(mImageUrl, new ImageDownLoader.onImageLoaderListener() {

                @Override
                public void onImageLoader(Bitmap bitmap, String url) {
                    if(mImageView != null && bitmap != null){
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            });

            if(bitmap != null){
                mImageView.setImageBitmap(bitmap);
            }else{
//                mImageView.setImageDrawable(R.mipmap.ic_launcher);
            }
        }
    }
}
