package com.fu.bluetoothmessager.model;

import com.fu.bluetoothmessager.R;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Device extends RealmObject implements Serializable {

    @PrimaryKey
    private String address;

    private String name;
    private int image;
    private RealmList<ChatData> conversationList;

    public Device() {
    }

    public Device(String name, String address) {
        this.name = name;
        this.address = address;
        this.image = R.drawable.user4;
    }

    public Device(String name, String address, RealmList<ChatData> conversationList) {
        this.name = name;
        this.address = address;
        this.conversationList = conversationList;
    }

    public Device(RealmList<ChatData> conversationList) {
        if (conversationList == null) {
            this.conversationList = new RealmList<>();
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

    public RealmList<ChatData> getConversationList() {
        if (conversationList == null) {
            return new RealmList<>();
        } else {
            return conversationList;
        }

    }

    public void addChatData(ChatData chatData) {
        if (conversationList == null) {
            conversationList = new RealmList<>();
            conversationList.add(chatData);
        } else {
            RealmList<ChatData> chatList = new RealmList<>(); // Create new List to fix bug can't insert RealmList
            for (ChatData chat : conversationList) {
                chatList.add(chat);
            }
            conversationList = chatList;
            conversationList.add(chatData);
        }
    }

}
