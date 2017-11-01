package com.fu.bluetoothmessager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.fu.bluetoothmessager.R;
import com.fu.bluetoothmessager.model.Device;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ContactViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvDeviceName)
    TextView tvDeviceName;
    @BindView(R.id.tvDeviceAddress)
    TextView tvDeviceAddress;

    public ContactViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Device device) {
        tvDeviceName.setText(device.getName());
        tvDeviceAddress.setText(device.getAddress());
    }
}
