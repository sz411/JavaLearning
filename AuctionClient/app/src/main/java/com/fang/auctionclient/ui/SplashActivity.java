package com.fang.auctionclient.ui;

import android.content.pm.FeatureGroupInfo;
import android.content.pm.FeatureInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fang.auctionclient.R;
import com.fang.auctionclient.ui_base.BaseActivity;

/**
 * Created by Administrator on 2015/12/18.
 */
public class SplashActivity extends BaseActivity{
    ImageView mTranslate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        closeActionBar();
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        mTranslate= (ImageView) findViewById(R.id.splash_loading_item);
    }

    protected void initView(){
        Animation translate= AnimationUtils.loadAnimation(this,R.anim.translate);
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                openActivity(WanfengActivity.class);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTranslate.setAnimation(translate);
    }
}
