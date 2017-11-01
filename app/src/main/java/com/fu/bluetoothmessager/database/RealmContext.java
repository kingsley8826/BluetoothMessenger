package com.fu.bluetoothmessager.database;


import android.content.Context;

import com.fu.bluetoothmessager.model.Device;
import com.fu.bluetoothmessager.model.ChatData;

import java.util.List;

import io.realm.Realm;

public class RealmContext {

    private Realm realm;

    private RealmContext() {
        realm = Realm.getDefaultInstance();
    }

    private static RealmContext instance;

    public static RealmContext getInstance() {
        return instance;
    }

    public static void init(Context context) {
        Realm.init(context);
        instance = new RealmContext();

    }

    //---------------------------------------------------------------------------------------------

    public void insertChatData(Device device, ChatData chatData) {
        Device oldDevice = getDevice(device.getAddress());
        if (oldDevice == null) {
            realm.beginTransaction();
            device.addChatData(chatData);
            realm.commitTransaction();
            insert(device);
        } else {
            realm.beginTransaction();
            device.addChatData(chatData);
            realm.commitTransaction();
        }

    }

    public void insert(Device device) {
        realm.beginTransaction();
        realm.copyToRealm(device);
        realm.commitTransaction();
    }

    public Device getDevice(String address) {
        List<Device> deviceList = realm.where(Device.class).findAll();
        for (Device device : deviceList) {
            if (device.getAddress().equals(address)) {
                return device;
            }
        }
        return null;
    }

    public List<Device> getAllDevice() {
        return realm.where(Device.class).findAll();
    }
}
