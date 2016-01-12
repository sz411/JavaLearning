package com.fang.auctionclient.fragments;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fang.auctionclient.R;
import com.fang.auctionclient.adapter.ListAdapter;
import com.fang.auctionclient.bean.ContentBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentTest extends android.support.v4.app.Fragment {

    ListView mListView;
    ListAdapter mListAdapter;
    List<ContentBean> mList;
    static final String URLNAME="http://www.wanfen.me/api/getindex";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,null);
        mListView= (ListView)view.findViewById(R.id.lv);
        new wfAsyncTask().execute(URLNAME);
        return view;
    }

    class wfAsyncTask extends AsyncTask<String,Void,List<ContentBean>> {

        @Override
        protected List<ContentBean> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<ContentBean> ContentBeans) {
            super.onPostExecute(ContentBeans);
//            mListAdapter=new ListAdapter(getActivity(),mList,mListView);
//            mListView.setAdapter(mListAdapter);
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
                        + URLEncoder.encode("pagesize", "utf-8")
                        + "="
                        + URLEncoder.encode("10", "utf-8");
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
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                //一级分类data的信息
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                }
                //首页的二级分类scene的信息
                JSONObject object = (JSONObject) jsonArray.get(0);
                JSONArray scene=object.getJSONArray("scene");
                for(int i=0;i<scene.length();i++){
                    JSONObject mObject= (JSONObject) scene.get(i);
                    ContentBean wfBean=new ContentBean();
                    wfBean.setTitle(mObject.getString("scene_title"));
                    wfBean.setImageurl(mObject.getString("scene_img"));
                    wfBean.setLoveNum(mObject.getString("lovenum"));
                    mList.add(wfBean);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mList;
        }
    }
}
