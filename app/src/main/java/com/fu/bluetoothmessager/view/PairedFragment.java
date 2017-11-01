package com.fu.bluetoothmessager.view;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.fu.bluetoothmessager.R;
import com.fu.bluetoothmessager.adapter.ContactAdapter;
import com.fu.bluetoothmessager.model.Device;
import java.util.Set;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PairedFragment extends Fragment {


    @BindView(R.id.rvPairDevices)
    RecyclerView rvPairDevices;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;

    private BluetoothAdapter mBtAdapter;

    public PairedFragment() {
        // Required empty public constructor
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.mBtAdapter = bluetoothAdapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paired, container, false);
        ButterKnife.bind(this, view);
        setupUI();
        return view;
    }

    private void setupUI() {
        ContactAdapter pairedDevicesAdapter = new ContactAdapter();

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            rlEmpty.setVisibility(View.GONE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesAdapter.addDevice(new Device(device.getName(), device.getAddress()));
            }
        } else {
            rlEmpty.setVisibility(View.VISIBLE);
        }
        rvPairDevices.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPairDevices.setAdapter(pairedDevicesAdapter);
    }

}
