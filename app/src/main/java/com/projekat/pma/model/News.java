package com.projekat.pma.model;

import android.media.Image;

public class News {

    String title;
    String text;
    String image;
    Long id;

    public News(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public News(){}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
