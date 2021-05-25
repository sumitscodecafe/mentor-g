package com.example.mentorg;

public class HomeMenuModel {
    String title;
    String imgUrl;
    public HomeMenuModel(){}

    public HomeMenuModel(String title, String imgUrl) {
        this.title = title;
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }
    public String getImgUrl() {
        return imgUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
