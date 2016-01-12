package com.fang.auctionclient.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fang.auctionclient.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/13.
 */
public class MyAdapter extends BaseAdapter{
    Context context;
    private static final int TYPE_COUNT=2;
    private static final int TYPE_ONE=0;
    private static final int TYPE_TWO=1;
    private int currentType;
    LayoutInflater mInflater;
    JSONArray jsonArray;
    public MyAdapter(Context context){
        this.context=context;
        this.mInflater=LayoutInflater.from(context);
        jsonArray=new JSONArray();
    }


    private ArrayList<HashMap<String,Object>> getData(){
        ArrayList<HashMap<String,Object>> hashMaps=new ArrayList<>();
        for(int i=0;i<3;i++){
            HashMap<String,Object> hashMap=new HashMap<String,Object>();
            hashMap.put("title_1","这是标题"+i);
            hashMap.put("text_1","这是内容"+i);
            hashMaps.add(hashMap);
        }
        return hashMaps;
    };

    private ArrayList<HashMap<String,Object>> getData2(){
        ArrayList<HashMap<String,Object>> hashMaps=new ArrayList<>();
        for(int i=0;i<3;i++){
            HashMap<String,Object> hashMap=new HashMap<String,Object>();
            hashMap.put("title_1","这2是2标2题2"+i);
            hashMap.put("text_1","这2是2内2容2"+i);
            hashMaps.add(hashMap);
        }
        return hashMaps;//这里不能用全局变量！！！！！！！！！！！！！！！！！不然每次都会加上去，越来越多
    };

    @Override
    public int getCount() {
        return getData().size()+getData2().size();
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
    public int getItemViewType(int position) {
        Log.d("aaa","getItemViewType:   "+position);
        if( 0 <= position && position < getData().size() )
            return TYPE_ONE;
        else if(position >= getData().size())
            return TYPE_TWO;
        else {
            return 100;
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View Type_1 = null;
        View Type_2 = null;
        //由getItemViewType获取需要不同布局的判断
        currentType = getItemViewType(position);
        Log.d("aaa","currentType:   " + currentType);
        Log.d("aaa","position:  " + position);
        if(currentType == TYPE_ONE){
            Log.v("aaa","第一个ITEM输出");
            Type1_ViewHolder Type1_holder;
            if(convertView == null){
                Type_1 = mInflater.inflate(R.layout.listitem_test1, null);
                Type1_holder = new Type1_ViewHolder();
                Type1_holder.iv_1 = (ImageView) Type_1.findViewById(R.id.iv_1);
                Type1_holder.title_1 = (TextView) Type_1.findViewById(R.id.title_1);
                Type1_holder.text_1 = (TextView) Type_1.findViewById(R.id.text_1);
                Type_1.setTag(Type1_holder);
                convertView = Type_1;
            }else {
                Type1_holder = (Type1_ViewHolder) convertView.getTag();
            }
            Type1_holder.iv_1.setImageResource(R.drawable.bomb12);
            Type1_holder.title_1.setText(getData().get(position).get("title_1").toString());
            Type1_holder.text_1.setText(getData().get(position).get("text_1").toString());
        }
        else if(currentType == TYPE_TWO){
            Type2_ViewHolder Type2_holder;
            if(convertView == null){
                Type2_holder = new Type2_ViewHolder();
                Type_2 = mInflater.inflate(R.layout.listitem_test2, null);
                Type2_holder.iv_2 = (ImageView) Type_2.findViewById(R.id.iv_2);
                Type2_holder.title_2 = (TextView) Type_2.findViewById(R.id.title_2);
                Type2_holder.text_2 = (TextView) Type_2.findViewById(R.id.text_2);
                Type2_holder.iv_3 = (ImageView) Type_2.findViewById(R.id.iv_3);
                Type_2.setTag(Type2_holder);
                convertView = Type_2;
            }else {
                Type2_holder = (Type2_ViewHolder) convertView.getTag();
            }
            Type2_holder.iv_2.setImageResource(R.drawable.bomb12);
            Type2_holder.title_2.setText(getData2().get(position-3).get("title_1").toString());
            Type2_holder.text_2.setText(getData2().get(position-3).get("text_1").toString());
            Type2_holder.iv_3.setImageResource(R.drawable.bomb12);
        }
        return convertView;
    }

    public static class Type1_ViewHolder{
        public ImageView iv_1;
        public TextView title_1;
        public TextView text_1;
    }
    public static class Type2_ViewHolder{
        public ImageView iv_2;
        public TextView title_2;
        public TextView text_2;
        public ImageView iv_3;
    }
}
