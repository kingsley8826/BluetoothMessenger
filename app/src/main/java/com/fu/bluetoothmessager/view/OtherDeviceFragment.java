package com.fu.bluetoothmessager.view;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.fu.bluetoothmessager.R;
import com.fu.bluetoothmessager.adapter.ContactAdapter;
import com.fu.bluetoothmessager.model.Device;
import com.fu.bluetoothmessager.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherDeviceFragment extends Fragment {

    @BindView(R.id.rvOtherDevices)
    RecyclerView rvOtherDevices;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;

    private ProgressDialog pDialog;


    private BluetoothAdapter mBtAdapter;
    private ContactAdapter mOtherDeviceAdapter;

    public OtherDeviceFragment() {
        // Required empty public constructor
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.mBtAdapter = bluetoothAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_device, container, false);
        ButterKnife.bind(this, view);
        if(!Constants.AUTO_LOAD_DEVICE){
            init();
        }
        return view;
    }


    /**
     * Don't discovery at onCreate because this fragment will load when before it appearance
     * (when click tabLayout at position 1)
     */
    public void init() {
        doDiscovery();
        setupUI();
    }

    private void setupUI() {

        mOtherDeviceAdapter = new ContactAdapter();
        rvOtherDevices.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOtherDevices.setAdapter(mOtherDeviceAdapter);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        mBtAdapter.startDiscovery();
    }


    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) { // When discovery finds a device
                if (rlEmpty.getVisibility() == View.VISIBLE) {
                    rlEmpty.setVisibility(View.GONE);
                }
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mOtherDeviceAdapter.addDevice(new Device(device.getName(), device.getAddress()));
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { // When discovery is finished, change the Activity title
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
                if (mOtherDeviceAdapter.getItemCount() == 0) {
                    rlEmpty.setVisibility(View.VISIBLE);
                }
                getActivity().unregisterReceiver(mReceiver);
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();

        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        try {
            getActivity().unregisterReceiver(mReceiver);
        } catch (Exception ex) {
            Log.e("OtherDeviceFragment", "Receiver not registered");
        }
    }

}
