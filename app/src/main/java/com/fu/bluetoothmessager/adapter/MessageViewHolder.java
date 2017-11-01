package com.fu.bluetoothmessager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fu.bluetoothmessager.R;
import com.fu.bluetoothmessager.model.Device;
import com.fu.bluetoothmessager.model.ChatData;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvDeviceName)
    TextView tvDeviceName;
    @BindView(R.id.tvLastMessage)
    TextView tvLastMessage;
    @BindView(R.id.tvTime)
    TextView tvTime;

    public MessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Device device) {
        tvDeviceName.setText(device.getName());
        if (device.getConversationList().size() > 0) {
            ChatData lastMassageData = device.getConversationList().get(device.getConversationList().size() - 1);
            tvLastMessage.setText(lastMassageData.getText());
            tvTime.setText(lastMassageData.getTime());
        }
    }
}