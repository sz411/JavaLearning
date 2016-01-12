package com.fang.auctionclient.ui_base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Administrator on 2015/12/18.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        closeActionBar();
    }

    protected void closeActionBar(){
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if(bar!=null){
            bar.hide();
        }else {

        }
    }

    /**
     * 统一管理绑定控件id
     */
    protected abstract void findViewById();
    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 1.1通过类名另一个Activity
     */
    protected void openActivity(Class<?> pClass){
        openActivity(pClass,null);
    }

    /**
     * 1.2通过类名启动Activity,并且有Bundle数据
     */
    protected void openActivity(Class<?> pClass,Bundle pbundle){
        Intent intent=new Intent(this,pClass);
        if(pbundle!=null){
            intent.putExtra("data",pbundle);
        }
        startActivity(intent);
    }

    /**
     * 2.1通过Action启动Activity
     */
    protected void openActivity(String pAction){
        openActivity(pAction,null);
    }

    /**
     * 2.2通过Action启动Activity,并且有Bundle数据
     */
    protected void openActivity(String pAction,Bundle pBundle){
        Intent intent=new Intent(pAction);
        if(pBundle!=null){
            intent.putExtra("data",pBundle);
        }
        startActivity(intent);
    }
}
