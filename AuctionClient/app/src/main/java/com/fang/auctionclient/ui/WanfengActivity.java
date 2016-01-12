package com.fang.auctionclient.ui;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.fang.auctionclient.R;
import com.fang.auctionclient.adapter.PagerAdapter;
import com.fang.auctionclient.bean.One_level;
import com.fang.auctionclient.ui_base.BaseActivity;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/25.
 */
public class WanfengActivity extends BaseActivity{
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TabLayout mTabLayout;
    private Button drawer_pop;
    private Button select_viewpager;
    private PopupWindow popupWindow;
    private ImageView pull_down;
    private Button search;
    private static final String URLNAME="http://www.wanfen.me/api/getindex";
    private List<One_level> mList;
    private final static int SETPAGERLIMIT = 5;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanfeng);
        closeActionBar();
        findViewById();
        initView();
        new wfAsyncTask().execute(URLNAME);
    }

    @Override
    protected void findViewById() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.wanfeng);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        drawer_pop = (Button) findViewById(R.id.drawer_pop);
        select_viewpager = (Button) findViewById(R.id.select_viewpager);
        pull_down = (ImageView) findViewById(R.id.pull_down);
        search = (Button) findViewById(R.id.search);
    }

    @Override
    protected void initView() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SearchActivity.class);
            }
        });
        //抽屉拉出的点击效果设定
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.loveNum:
                        openActivity(MyLoveActivity.class);
                        break;
                    case R.id.order:
                        break;
                    case R.id.friend:
                        openActivity(FriendAcitivity.class);
                        break;
                    case R.id.setting:
                        break;
                    case R.id.exit:
                        break;
                    default:
                        break;
                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        drawer_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        select_viewpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("aaa", "被点击");
                popupWindow.showAsDropDown(v);
//                popupWindow.showAtLocation(findViewById(R.id.search),
//                        Gravity.NO_GRAVITY,0,0);
                mTabLayout.setVisibility(View.GONE);
                rotate(true);
            }
        });
    }

    private void initPopuptWindow(){
        View view = this.getLayoutInflater().inflate(R.layout.select_pop,null);
        GridView gridView = (GridView) view.findViewById(R.id.pop_gridView);
        final List<String> list = new ArrayList<>();
        for(int i = 0;i < mList.size();i++){
            list.add(mList.get(i).getCat_name());
        }
        BaseAdapter baseAdapter=new BaseAdapter() {

            @Override
            public int getCount() {
                return list.size();
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
                convertView=LayoutInflater.from(WanfengActivity.this).inflate(R.layout.select_pop_item,null);
                TextView textView = (TextView) convertView.findViewById(R.id.select_pop_tv);
                textView.setText(list.get(position));
                return convertView;
            }
        };
        gridView.setAdapter(baseAdapter);
        gridView.setNumColumns(4);

        popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,false);
        popupWindow.setBackgroundDrawable(new ColorDrawable());//点击窗口外消失
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.update();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("aaa", "点了" + position);
                mViewPager.setCurrentItem(position);
                popupWindow.dismiss();
            }
        });
        //下拉窗口消失的监听事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTabLayout.setVisibility(View.VISIBLE);
                rotate(false);
            }
        });
    }

    private void rotate(boolean mode){
        float pivotX = pull_down.getWidth() / 2f;
        float pivoY = pull_down.getHeight() / 2f;
        float fromdegree = 0f;
        float todegree = 0f;
        if(mode){
            fromdegree = 0f;
            todegree = 180f;
        }else {
            fromdegree = 180f;
            todegree = 0f;
        }
        RotateAnimation rotateAnimation = new RotateAnimation(fromdegree,todegree,
                pivotX,pivoY);
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(true);
        pull_down.startAnimation(rotateAnimation);
    }

    class wfAsyncTask extends AsyncTask<String,Void,List<One_level>> {

        @Override
        protected List<One_level> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<One_level> list) {
            super.onPostExecute(list);
            mPagerAdapter = new PagerAdapter(getSupportFragmentManager(),mList);
            mViewPager.setAdapter(mPagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            for(int i=0;i<mList.size();i++){
                mTabLayout.getTabAt(i).setText(mList.get(i).getCat_name());
            }
            //设置viewPager缓存的个数
            mViewPager.setOffscreenPageLimit(mList.size());
            if(popupWindow == null){
                initPopuptWindow();
            }
        }

        private List<One_level> getJsonData(String urlName){
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
//                        + "&"
//                        + URLEncoder.encode("pagesize", "utf-8")
//                        + "="
//                        + URLEncoder.encode("10", "utf-8")
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
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                //一级分类data的信息
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    One_level one_level = new One_level();
                    one_level.setCat_name(object.getString("cat_name"));
                    one_level.setCategory_id(object.getInt("category_id"));
                    mList.add(one_level);
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
