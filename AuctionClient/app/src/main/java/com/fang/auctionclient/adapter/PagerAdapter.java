package com.fang.auctionclient.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.fang.auctionclient.bean.One_level;
import com.fang.auctionclient.fragments.FragmentTest;
import com.fang.auctionclient.fragments.FragmentTest2;
import com.fang.auctionclient.fragments_wf.CommonFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/26.
 */
public class PagerAdapter extends FragmentPagerAdapter{

    private List<One_level> mList;
    private List<Fragment> mFragList = null;

    public PagerAdapter(FragmentManager fm,List<One_level> mList) {
        super(fm);
        this.mList = mList;
        mFragList = getFragmentList();
    }

    @Override
    public Fragment getItem(int i) {
        return mFragList.get(i);
    }

    @Override
    public int getCount() {
        return mFragList.size();
    }

    //动态生成所有的fragment
    private List<Fragment> getFragmentList(){
        List<Fragment> list = new ArrayList<>();
        for(int i=0;i<mList.size();i++){
            list.add(new CommonFragment(mList.get(i).getCategory_id()));
        }
        /**
         * 测试代码，只加载一个commonFragment访问网络测试
         */
//        list.add(new FragmentTest2());
//        list.add(new CommonFragment(mList.get(0).getCategory_id()));
//        for(int i=0;i<mList.size()-2;i++){
//            list.add(new FragmentTest2());
//        }
        return list;
    }
}
