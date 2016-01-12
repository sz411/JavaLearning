package com.fang.auctionclient.uploadFile;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
  
public class HttpPostUtil {  
    URL url;  
    HttpURLConnection conn;  
    String boundary = "--------httppost123";  
    Map<String, String> textParams = new HashMap<String, String>();  
    Map<String, File> fileparams = new HashMap<String, File>();  
    DataOutputStream ds;  
  
    public HttpPostUtil(String url) throws Exception {  
        this.url = new URL(url);  
    }  
    //Re set to request the server address, or upload files.
    public void setUrl(String url) throws Exception {  
        this.url = new URL(url);  
    }  
    //Add a simple string data to form form data  
    public void addTextParameter(String name, String value) {  
        textParams.put(name, value);  
    }  
    //Add a file to the form form data
    public void addFileParameter(String name, File value) {  
        fileparams.put(name, value);  
    }  
    //Clear all have been added to the form form data
    public void clearAllParameters() {  
        textParams.clear();  
        fileparams.clear();  
    }  
    //Sending data to the server, and returns a byte array returned as a result of the server
    public byte[] send() throws Exception {  
        initConnection();  
        try {  
            conn.connect();  
        } catch (SocketTimeoutException e) {  
            // something  
            throw new RuntimeException();  
        }  
        ds = new DataOutputStream(conn.getOutputStream());  
        writeFileParams();  
        writeStringParams();  
        paramsEnd();  
        InputStream in = conn.getInputStream();  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int b;
        while ((b = in.read()) != -1) {  
            out.write(b);  
        }  
        conn.disconnect();  
        return out.toByteArray();  
    }  
    //Some file upload connection must be set
    private void initConnection() throws Exception {  
        conn = (HttpURLConnection) this.url.openConnection();  
        conn.setDoOutput(true);  
        conn.setUseCaches(false);  
        conn.setConnectTimeout(10000); //Connection timeout is 10 seconds
        conn.setRequestMethod("POST");  
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);  
    }  
    //Ordinary string data
    private void writeStringParams() throws Exception {  
        Set<String> keySet = textParams.keySet();  
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {  
            String name = it.next();  
            String value = textParams.get(name);  
            ds.writeBytes("--" + boundary + "\r\n");  
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name  
                    + "\"\r\n");  
            ds.writeBytes("\r\n");  
            ds.writeBytes(encode(value) + "\r\n");  
        }  
    }  
    //File data
    private void writeFileParams() throws Exception {  
        Set<String> keySet = fileparams.keySet();  
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {  
            String name = it.next();  
            File value = fileparams.get(name);  
            ds.writeBytes("--" + boundary + "\r\n");  
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name  
                    + "\"; filename=\"" + encode(value.getName()) + "\"\r\n");  
            ds.writeBytes("Content-Type: " + getContentType(value) + "\r\n");  
            ds.writeBytes("\r\n");
//            ds.write(getBytes(value));
            //使用Base64编码传输
            ds.write(Base64.encode(getString(value).getBytes(), Base64.DEFAULT));
            ds.writeBytes("\r\n");  
        }  
    }  
    //Get the file upload, picture format as image/png, image/jpg etc.. Non image to application/octet-stream
    private String getContentType(File f) throws Exception {  
    	// This subdivision is no longer pictures, all as application/octet-stream type
    	//return "application/octet-stream";  
        /*ImageInputStream imagein = ImageIO.createImageInputStream(f);  
        if (imagein == null) {  
            return "application/octet-stream";  
        }  
        Iterator<ImageReader> it = ImageIO.getImageReaders(imagein);  
        if (!it.hasNext()) {  
            imagein.close();  
            return "application/octet-stream";  
        }  
        imagein.close();  
        //The value returned by FormatName is converted to lowercase to uppercase, default
        return "image/" + it.next().getFormatName().toLowerCase();*/
    	return "application/octet-stream";
    }

    //为读取文本获取String，使用GBK格式读取
    private String getString(File f) throws Exception{
        FileInputStream in = new FileInputStream(f);
        BufferedReader br = new BufferedReader(new InputStreamReader(in,"GBK"));
        StringBuilder sb = new StringBuilder("");
        String line;
        while ((line = br.readLine())!= null){
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    //The file into an array of bytes
    private byte[] getBytes(File f) throws Exception {  
        FileInputStream in = new FileInputStream(f);  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        byte[] b = new byte[1024];  
        int n;  
        while ((n = in.read(b)) != -1) {  
            out.write(b, 0, n);  
        }  
        in.close();
        return out.toByteArray();  
    }  
    //Add at the end of data
    private void paramsEnd() throws Exception {  
        ds.writeBytes("--" + boundary + "--" + "\r\n");  
        ds.writeBytes("\r\n");  
    }  
    //The string contains Chinese transcoding, this is UTF-8. Server side to a decoding
    private String encode(String value) throws Exception{
        return URLEncoder.encode(value, "UTF-8");
    }  
    public static void main(String[] args) throws Exception {  
        HttpPostUtil u = new HttpPostUtil("http://localhost:3000/up_load");  
        u.addFileParameter("img", new File(  
                "D:\\semple\\moon.jpg"));  
        u.addTextParameter("text", "Chinese");  
        byte[] b = u.send();  
        String result = new String(b);  
        System.out.println(result);  
  
    }  
  
}  