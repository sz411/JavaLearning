package com.fang.auctionclient.uploadFile;

import android.os.AsyncTask;

import java.io.File;

public class UploadFileTask extends AsyncTask<File, Void, Boolean> {
	private String server;
    private UploadFileCallBack callBack;

    public UploadFileTask(final String server, UploadFileCallBack callBack) {
        this.server = server;
        this.callBack = callBack;
    }
    @Override
    protected Boolean doInBackground(File... params) {
    	try {
    		String url = "";
    		for(int i = 0; i < params.length; i++) {
				File file = params[i];
//    			url = server+"&fileName="+ file.getName();
				url = server;
    			HttpPostUtil util = new HttpPostUtil(url);
    			util.addFileParameter("snsFile",file);
    			byte[] buff = util.send();
				String result = new String(buff);
//				Log.d("aaa","测试:"+result);
				if(result != "success"){
					return false;
				}
//				JSONArray dataList = new JSONArray("["+new String(buff)+"]");
//    			JSONObject item = dataList.getJSONObject(0);
//    			if(!item.getString("status").equalsIgnoreCase("success")) {
//    				return false;
//    			}
    		}
    		return true;
    	} catch(Exception e) {
    		e.printStackTrace();
			return false;
    	}
    }
	@Override
	protected void onPostExecute(Boolean result) {
		callBack.callBack(result);
	}
}
