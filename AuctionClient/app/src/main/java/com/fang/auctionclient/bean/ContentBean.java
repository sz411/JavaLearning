package com.fang.auctionclient.bean;

/**
 * Created by Administrator on 2015/12/25.
 */
public class ContentBean {
    private String imageurl;
    private String title;
    private String loveNum;
    private String url;

    public String getImageurl() {
            return imageurl;
        }

    public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

    public String getTitle() {
            return title;
        }

    public void setTitle(String title) {
            this.title = title;
        }

    public String getLoveNum() {
            return loveNum;
        }

    public void setLoveNum(String loveNum) {
            this.loveNum = loveNum;
        }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "wanfeng [imageurl=" + imageurl + "," +
                " title=" + title + "," +
                " loveNum=" + loveNum + "]";
    }
}
