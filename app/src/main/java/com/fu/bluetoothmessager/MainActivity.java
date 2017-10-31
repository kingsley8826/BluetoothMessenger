package com.fu.bluetoothmessager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fu.bluetoothmessager.service.BluetoothChatService;
import com.fu.bluetoothmessager.DeviceListFragment;
import com.fu.bluetoothmessager.R;

public class MainActivity extends AppCompatActivity {

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, new DeviceListFragment())
                .commit();
    }
}
