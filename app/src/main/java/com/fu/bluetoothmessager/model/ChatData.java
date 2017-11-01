package com.fu.bluetoothmessager.model;

import com.fu.bluetoothmessager.util.Type;

import io.realm.RealmObject;


public class ChatData extends RealmObject{

    private int type;
    private String text;
    private String time;
    private int image;

    public ChatData() {
    }

    public ChatData(int type, String time, int image) {
        this.type = type;
        this.time = time;
        this.image = image;
    }

    public ChatData(int type, String text, String time) {
        this.type = type;
        this.text = text;
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
