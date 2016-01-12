package com.fang.auctionclient.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.fang.auctionclient.R;
import com.fang.auctionclient.uploadFile.UploadFileCallBack;
import com.fang.auctionclient.uploadFile.UploadFileTask;
import com.fang.auctionclient.ui_base.BaseActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/1/4.
 */
public class UploadFile extends BaseActivity{

    private String boundary = "--------httppost123";
    private static final String URL = "http://www.clawbook.com/api/snstest";
    private static final String FILE_NAME = "/test.txt";
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new upload().execute(URL);
        String LOCAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        file = new File(LOCAL_PATH + FILE_NAME);
        new UploadFileTask(URL, new UploadFileCallBack() {
            @Override
            public void callBack(boolean flag) {

            }
        }).execute(file);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {

    }

    private class upload extends AsyncTask<String,Void,Void>{
        private String urlName;

        @Override
        protected Void doInBackground(String... params) {
            urlName = params[0];
            uploadFile();
            return null;
        }

        private String getFile() {
            try {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    File sdCardDir = Environment.getExternalStorageDirectory();
//                    FileInputStream fis = new FileInputStream(
//                            sdCardDir.getCanonicalPath()
//                            + FILE_NAME);
                    String LOCAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
                    file = new File(LOCAL_PATH + FILE_NAME);
//                    File file = new File("/storage/sdcard1/test/snsFile.txt");//魅族外置SD卡路径
                    if(!file.exists()){
                        Log.v("aaa", "文件路径不存在");
                    }
                    FileInputStream fis = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis,"GBK"));
                    StringBuilder sb = new StringBuilder("");
                    String line = null;
                    while ((line = br.readLine())!= null){
                        sb.append(line);
                    }
                    br.close();
                    return sb.toString();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void uploadFile() {
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            try {
                conn= (HttpURLConnection) new URL(urlName).openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + boundary);
                conn.connect();
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes("--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "snsFile"
                        + "\"; filename=\"" +
                        URLEncoder.encode(file.getName(), "utf-8") + "\"\r\n");
                dos.writeBytes("Content-Type: " + "application/octet-stream" + "\r\n");
                dos.writeBytes("\r\n");
                dos.write(Base64.encode(getFile().getBytes(), Base64.DEFAULT));
                dos.writeBytes("\r\n");
                dos.writeBytes("--" + boundary + "--" + "\r\n");
                dos.writeBytes("\r\n");
                dos.flush();
                dos.close();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                Log.v("aaa", sb.toString());
                if (conn.getResponseCode() != 200) {
                    Log.v("aaa", "连接失败");
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.disconnect();
            }
        }
    }
}
