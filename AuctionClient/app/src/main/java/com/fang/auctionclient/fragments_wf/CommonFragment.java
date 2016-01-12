package com.fang.auctionclient.fragments_wf;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fang.auctionclient.R;
import com.fang.auctionclient.adapter.ListAdapter;
import com.fang.auctionclient.bean.ContentBean;
import com.fang.auctionclient.util.RefreshableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2015/12/16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CommonFragment extends android.support.v4.app.Fragment{

    private ListView mListView;
    private ListAdapter mListAdapter;
    private List<ContentBean> mList;
    private RefreshableView mRefreshableView;
    private static final String URLNAME="http://www.wanfen.me/api/getscenebycateid";
    private int category_id;
    private int PAGESIZE = 10;    //首次访问加载多少页
    private int PAGE = 1;     //从第几页开始访问,作为随机刷新的依据
    public int LARGE_PAGESIZE = 0;
    //取消预加载
    private boolean mIsVisibleToUser ;
    private boolean haveLoad = false;
    private View view;

    public CommonFragment(int id){
        this.category_id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list,null);
        mListView = (ListView)view.findViewById(R.id.lv);
        //下拉刷新控件
        mRefreshableView = (RefreshableView) view.findViewById(R.id.refresh_view);
        mRefreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                new JsonAsyncTask().execute(URLNAME);
            }
        }, 0);
        //可见就加载，为首页加载
        if(mIsVisibleToUser){
            new JsonAsyncTask().execute(URLNAME);
            haveLoad = true;
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        //没加载就检测view是否为空，为空则等onCreateView自动加载，否则自己加载
        if(!haveLoad) {
            if (view != null) {
                new JsonAsyncTask().execute(URLNAME);
                haveLoad = true;
            }
        }
    }

    class JsonAsyncTask extends AsyncTask<String,Void,List<ContentBean>> {

        @Override
        protected List<ContentBean> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<ContentBean> ContentBeans) {
            super.onPostExecute(ContentBeans);
            if(mListAdapter == null){
                mListAdapter=new ListAdapter(getActivity(),mList,mListView);
                mListAdapter.setOnLoadListener(new ListAdapter.onLoad() {
                    @Override
                    public void onLoadingMore() {
                        if((PAGESIZE += 10) >= LARGE_PAGESIZE){
                            PAGESIZE = LARGE_PAGESIZE;
                            mListAdapter.isNoMore = true;
                        }
                        new JsonAsyncTask().execute(URLNAME);
                    }
                });
                mListView.setAdapter(mListAdapter);
            }
            mListAdapter.isOnload = false;
            mListAdapter.mList = mList;
            mListAdapter.notifyDataSetChanged();
            mRefreshableView.finishRefreshing();
        }

        private List<ContentBean> getJsonData(String urlName){
            mList=new ArrayList<>();
            try {
                HttpURLConnection conn= (HttpURLConnection) new URL(urlName).openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.connect();
                //为POST设置参数
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                String postContent = URLEncoder.encode("ak", "utf-8")
                        + "="
                        + URLEncoder.encode("PjsuLkjs679as87wgPOisaspoIjkalsg", "utf-8")
                        + "&"
                        + URLEncoder.encode("cateid","utf-8")
                        + "="
                        + URLEncoder.encode(""+category_id,"utf-8")
                        + "&"
                        + URLEncoder.encode("page","utf-8")
                        + "="
                        + URLEncoder.encode(""+PAGE,"utf-8")
                        + "&"
                        + URLEncoder.encode("pagesize", "utf-8")
                        + "="
                        + URLEncoder.encode(""+PAGESIZE, "utf-8")
                        ;
                dos.write(postContent.getBytes());
                dos.flush();
                dos.close();
                if (conn.getResponseCode() != 200) {
                    Log.v("aaa", "连接失败");
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                //获取JSON需要的数据
                JSONObject jsonObject = new JSONObject(sb.toString());
                if(LARGE_PAGESIZE == 0){
                    LARGE_PAGESIZE = jsonObject.getInt("totlenums");
                }
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject mObject= (JSONObject) jsonArray.get(i);
                    ContentBean wfBean=new ContentBean();
                    wfBean.setTitle(mObject.getString("scene_title"));
                    wfBean.setImageurl(mObject.getString("scene_img"));
                    wfBean.setLoveNum(mObject.getString("lovenum"));
                    wfBean.setUrl(mObject.getString("url"));
                    mList.add(wfBean);
                }
                bufferedReader.close();
                dos.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {

            }
            return mList;
        }
    }
}
