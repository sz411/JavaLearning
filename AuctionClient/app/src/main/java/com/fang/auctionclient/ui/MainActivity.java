package com.fang.auctionclient.ui;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import com.fang.auctionclient.fragments.FragmentTest;
import com.fang.auctionclient.fragments.FragmentTest2;
import com.fang.auctionclient.fragments.FragmentTest3;
import com.fang.auctionclient.fragments.FragmentTest4;
import com.fang.auctionclient.R;
import com.fang.auctionclient.fragments.FragmentTest5;
import com.fang.auctionclient.ui_base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends BaseActivity {
    private android.support.v4.app.FragmentManager mFragmentManager;
    android.support.v4.app.FragmentTransaction mFragmentTransaction;
    RadioGroup mRadioGroup;
    android.support.v4.app.Fragment f1, f2, f3, f4, f5;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        closeActionBar();
        mFragmentManager = getSupportFragmentManager();
        findViewById();
        f1 = new FragmentTest();
        f2 = new FragmentTest2();
        f3 = new FragmentTest3();
        f4 = new FragmentTest4();
        f5 = new FragmentTest5();
        initView();
        getJsonData();
    }

    @Override
    protected void findViewById() {
        mRadioGroup = (RadioGroup) findViewById(R.id.radio);
    }

    private void getJsonData() {
        try {
            InputStreamReader fiis = new InputStreamReader(
                    getResources().openRawResource(R.raw.feedcomment));
            BufferedReader bufferedReader = new BufferedReader(fiis);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            fiis.close();
            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("aaa");
            JSONObject object = jsonArray.getJSONObject(0);
            String a = jsonObject.getString("name");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void initView() {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.container, f1).commit();
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mFragmentTransaction = mFragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.rb1:
                        mFragmentTransaction.replace(R.id.container, f1);
                        break;
                    case R.id.rb2:
                        mFragmentTransaction.replace(R.id.container, f2);
                        break;
                    case R.id.rb3:
                        mFragmentTransaction.replace(R.id.container, f3);
                        break;
                    case R.id.rb4:
                        mFragmentTransaction.replace(R.id.container, f4);
                        getJsonData();
                        break;
                    case R.id.rb5:
                        mFragmentTransaction.replace(R.id.container, f5);
                        getJsonData();
                        break;
                    default:
                        break;
                }
                mFragmentTransaction.commit();
            }
        });
    }
}

