package com.fang.auctionclient.util;

import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Color;

import com.fang.auctionclient.R;

public class MainActivity extends Activity implements OnRefreshListener {

	private List<String> textList;
	private MyAdapter adapter;
	private RefreshListView rListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		rListView = (RefreshListView) findViewById(R.id.refreshlistview);
		textList = new ArrayList<String>();
		for (int i = 0; i < 25; i++) {
			textList.add("����һ��ListView������" + i);
		}
		adapter = new MyAdapter();
		rListView.setAdapter(adapter);
		rListView.setOnRefreshListener(this);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return textList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return textList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView textView = new TextView(MainActivity.this);
			textView.setText(textList.get(position));
			textView.setTextColor(Color.WHITE);
			textView.setTextSize(18.0f);
			return textView;
		}

	}

	@Override
	public void onDownPullRefresh() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				SystemClock.sleep(2000);
				for (int i = 0; i < 2; i++) {
					textList.add(0, "��������ˢ�³���������" + i);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				adapter.notifyDataSetChanged();
				rListView.hideHeaderView();
			}
		}.execute(new Void[] {});
	}

	@Override
	public void onLoadingMore() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				SystemClock.sleep(5000);

				textList.add("���Ǽ��ظ������������1");
				textList.add("���Ǽ��ظ������������2");
				textList.add("���Ǽ��ظ������������3");
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				adapter.notifyDataSetChanged();

				// ���ƽŲ�������
				rListView.hideFooterView();
			}
		}.execute(new Void[] {});
	}

}
