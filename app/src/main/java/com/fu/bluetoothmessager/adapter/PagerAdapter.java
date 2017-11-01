package com.fu.bluetoothmessager.adapter;

import android.bluetooth.BluetoothAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.fu.bluetoothmessager.view.MessagesFragment;
import com.fu.bluetoothmessager.view.OtherDeviceFragment;
import com.fu.bluetoothmessager.view.PairedFragment;


public class PagerAdapter extends FragmentPagerAdapter {

    private BluetoothAdapter bluetoothAdapter;

    //Constructor to the class
    public PagerAdapter(FragmentManager fm, BluetoothAdapter bluetoothAdapter) {
        super(fm);
        this.bluetoothAdapter = bluetoothAdapter;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MessagesFragment();
            case 1:
                PairedFragment pairedFragment = new PairedFragment();
                pairedFragment.setBluetoothAdapter(bluetoothAdapter);
                return pairedFragment;
            case 2:
                OtherDeviceFragment otherDeviceFragment = new OtherDeviceFragment();
                otherDeviceFragment.setBluetoothAdapter(bluetoothAdapter);
                return otherDeviceFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "MESSAGES";
            case 1:
                return "PAIRED";
            case 2:
                return "OTHERS";
            default:
                return null;
        }
    }
}
