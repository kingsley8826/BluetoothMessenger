package com.fu.bluetoothmessager.model;

import com.fu.bluetoothmessager.R;
import com.fu.bluetoothmessager.recyclerchat.ChatData;

import java.util.ArrayList;

/**
 * Created by Tuan-FPT on 31/10/2017.
 */

public class Device {

    private String name;
    private String address;
    private int image;
    private ArrayList<ChatData> conversationList;

    public Device(String name, String address) {
        this.name = name;
        this.address = address;
        this.image = R.drawable.user4;
    }

    public Device(String name, String address,ArrayList<ChatData> conversationList) {
        this.name = name;
        this.address = address;
        this.conversationList = conversationList;
    }

    public Device(ArrayList<ChatData> conversationList) {
        if (conversationList == null) {
            this.conversationList = new ArrayList<>();
        } else {
            this.conversationList = conversationList;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getImage() {
        return image;
    }

    public ArrayList<ChatData> getConversationList() {
        if (conversationList == null) {
            return new ArrayList<>();
        } else {
            return conversationList;
        }

    }

    public void setConversationList(ArrayList<ChatData> conversationList) {
        if (conversationList == null) {
            this.conversationList = new ArrayList<>();
        } else {
            this.conversationList = conversationList;
        }
    }
}
