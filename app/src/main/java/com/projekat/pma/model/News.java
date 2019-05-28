package com.projekat.pma.model;

import android.media.Image;

import java.util.List;

public class News {

    String title;
    String image;
    String text;
    List<Restriction> restriction;
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

    public void setRestriction(List<Restriction> restriction) {
        this.restriction = restriction;
    }

    public List<Restriction> getRestriction() {
        return restriction;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
