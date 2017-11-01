package com.fu.bluetoothmessager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.fu.bluetoothmessager.adapter.PagerAdapter;
import com.fu.bluetoothmessager.database.RealmContext;
import com.fu.bluetoothmessager.model.BDeviceSocket;
import com.fu.bluetoothmessager.util.Constants;
import com.fu.bluetoothmessager.model.Device;
import com.fu.bluetoothmessager.service.BluetoothFinderService;
import com.fu.bluetoothmessager.view.ChatActivity;
import com.fu.bluetoothmessager.view.MessagesFragment;
import com.fu.bluetoothmessager.view.NonSwipeViewPager;
import com.fu.bluetoothmessager.view.OtherDeviceFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    NonSwipeViewPager viewPager;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_POSITION = 1;
    private static final int REQUEST_ENABLE_BT = 3;


    private BluetoothFinderService mFinderService = null;
    private BluetoothAdapter mBtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        RealmContext.init(this);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        if (mBtAdapter != null && !mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

        } else if (mFinderService == null) {
            accessLocationPermission();
            setupTab();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFinderService != null) {
            mFinderService.start();
        }
    }

    private void setupTab() {
        mFinderService = new BluetoothFinderService(this, mHandler);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        PagerAdapter pagerAdapter = new PagerAdapter(this.getSupportFragmentManager(), mBtAdapter);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (viewPager.getCurrentItem() == 1 && tab.getPosition() == 2) {
                    // This case init OtherDeviceFragment
                    OtherDeviceFragment otherDeviceFragment = (OtherDeviceFragment)
                            viewPager.getAdapter().instantiateItem(viewPager, tab.getPosition());
                    otherDeviceFragment.init();

                } else if (viewPager.getCurrentItem() == 0 && tab.getPosition() == 1) {
                    // In this case, OtherDeviceFragment go onCreate, show setFlag = true to it don't load
                    Constants.AUTO_LOAD_DEVICE = true;

                } else if (viewPager.getCurrentItem() == 0 && tab.getPosition() == 2) {
                    // In this case, OtherDeviceFragment go onCreate, and we need it load in this case
                    Constants.AUTO_LOAD_DEVICE = false;

                } else if (viewPager.getCurrentItem() == 1 && tab.getPosition() == 0) {
                    // This case to MessageFragment reload when back from 1 to 0
                    MessagesFragment messagesFragment = (MessagesFragment)
                            viewPager.getAdapter().instantiateItem(viewPager, tab.getPosition());
                    messagesFragment.setupUI();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2) {
                    Constants.AUTO_LOAD_DEVICE = true;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void accessLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarseLocation = this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFineLocation = this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

            List<String> listRequestPermission = new ArrayList<>();

            if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
                listRequestPermission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (accessFineLocation != PackageManager.PERMISSION_GRANTED) {
                listRequestPermission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (!listRequestPermission.isEmpty()) {
                String[] strRequestPermission = listRequestPermission.toArray(new String[listRequestPermission.size()]);
                requestPermissions(strRequestPermission, REQUEST_ENABLE_POSITION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ENABLE_POSITION:
                if (grantResults.length > 0) {
                    for (int gr : grantResults) {
                        // Check if request is granted or not
                        if (gr != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                }
                break;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    accessLocationPermission();
                    setupTab();
                } else {
                    finish();
                }
        }
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.START_CHAT:
                    BDeviceSocket deviceSocket = (BDeviceSocket) msg.obj;
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra(Constants.BLUETOOTH_SOCKET, deviceSocket);
                    startActivity(intent);
                    break;
                case Constants.CONNECT_WRONG:

                    Toast.makeText(getBaseContext(), msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * Establish connection with other device
     */
    private void connectDevice(String address, boolean secure) {
        // Get the BluetoothDevice object
        BluetoothDevice bluetoothDevice;
        try {
            bluetoothDevice = mBtAdapter.getRemoteDevice(address);
            mFinderService.connect(bluetoothDevice, secure);
        } catch (Exception ex) {
            Toast.makeText(this, "Device's Address is not a valid",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void DevicesItemClick(Device device) {

        mBtAdapter.cancelDiscovery();
        connectDevice(device.getAddress(), false);
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFinderService != null) {
            mFinderService.stop();
        }
        EventBus.getDefault().unregister(this);
    }

}
