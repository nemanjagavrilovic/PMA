package com.projekat.pma.model;

public class Notification {

    String title;
    String text;
    Long identificator;


    public Notification(String title, String text) {
        this.title = title;
        this.text = text;
    }



    public Notification(){};
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

    public Long getIdentificator() {
        return identificator;
    }

    public void setIdentificator(Long identificator) {
        this.identificator = identificator;
    }
}
