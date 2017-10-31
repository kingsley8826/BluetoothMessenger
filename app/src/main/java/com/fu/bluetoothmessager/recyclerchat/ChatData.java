package com.fu.bluetoothmessager.recyclerchat;

import com.fu.bluetoothmessager.util.Type;

/**
 * Created by Tuan-FPT on 31/10/2017.
 */

public class ChatData {

    private Type type;
    private String text;
    private String time;
    private int image;

    public ChatData(Type type, String time, int image) {
        this.type = type;
        this.time = time;
        this.image = image;
    }

    public ChatData(Type type, String text, String time) {
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
