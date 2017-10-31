package com.fu.bluetoothmessager;


import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fu.bluetoothmessager.model.BDeviceSocket;
import com.fu.bluetoothmessager.model.Constants;
import com.fu.bluetoothmessager.service.BluetoothFinderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceListFragment extends Fragment {

    @BindView(R.id.rvPairDevices)
    ListView rvPairDevices;
    @BindView(R.id.rvOtherDevices)
    ListView rvOtherDevices;
    @BindView(R.id.title_new_devices)
    TextView tvNewDeviceTitle;
    @BindView(R.id.title_paired_devices)
    TextView tvPairedDeviceTitle;
    @BindView(R.id.button_scan)
    Button btnScan;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private ProgressDialog pDialog;
    private boolean allowSearch = false;


    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private BluetoothFinderService mFinderService = null;

    public DeviceListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accessLocationPermission();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBtAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (mBtAdapter != null && !mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mFinderService == null) {
            initService();
            setupFinder();
        }
        if (mBtAdapter != null && mBtAdapter.isEnabled()) {
            setupFinder();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFinderService != null) {
            mFinderService.start();
        }
    }

    private void initService() {
        mFinderService = new BluetoothFinderService(getContext(), mHandler);
    }

    private void setupFinder() {
        btnScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                allowSearch = true;
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        ArrayAdapter<String> pairedDevicesArrayAdapter =
                new ArrayAdapter<>(getContext(), R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.device_name);

        // Find and set up the ListView for paired devices
        rvPairDevices.setAdapter(pairedDevicesArrayAdapter);
        rvPairDevices.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        rvOtherDevices.setAdapter(mNewDevicesArrayAdapter);
        rvOtherDevices.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            tvPairedDeviceTitle.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            Toast.makeText(getActivity(), "No paired",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    initService();
                    setupFinder();
                } else {
                    getActivity().finish();
                }
        }
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        tvNewDeviceTitle.setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }


    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            connectDevice(address, false);
        }
    };

    private void accessLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarseLocation = getContext().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFineLocation = getContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

            List<String> listRequestPermission = new ArrayList<String>();

            if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
                listRequestPermission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (accessFineLocation != PackageManager.PERMISSION_GRANTED) {
                listRequestPermission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (!listRequestPermission.isEmpty()) {
                String[] strRequestPermission = listRequestPermission.toArray(new String[listRequestPermission.size()]);
                requestPermissions(strRequestPermission, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
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

    /**
     * Establish connection with other device
     */
    private void connectDevice(String address, boolean secure) {
        // Get the BluetoothDevice object
        BluetoothDevice bluetoothDevice;
        try {
            bluetoothDevice = mBtAdapter.getRemoteDevice(address);
            mFinderService.connect(bluetoothDevice, secure);
        }catch (Exception ex){
            Toast.makeText(getActivity(), "Device's Address is not a valid",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
                if (allowSearch && mNewDevicesArrayAdapter.getCount() == 0) {
                    tvNewDeviceTitle.setVisibility(View.GONE);
                    btnScan.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Not Found",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.START_CHAT:
                    BDeviceSocket deviceSocket = (BDeviceSocket) msg.obj;
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("Device_info", deviceSocket);
                    startActivity(intent);
                    break;
                case Constants.CONNECT_WRONG:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFinderService != null) {
            mFinderService.stop();
        }
    }
}
