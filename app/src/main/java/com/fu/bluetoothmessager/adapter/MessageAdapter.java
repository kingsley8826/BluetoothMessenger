package com.fu.bluetoothmessager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fu.bluetoothmessager.R;
import com.fu.bluetoothmessager.database.RealmContext;
import com.fu.bluetoothmessager.model.Device;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Device> devices;

    public MessageAdapter() {
        devices = RealmContext.getInstance().getAllDevice();
        if (devices == null) devices = new ArrayList<>();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, final int position) {
        holder.bind(devices.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(devices.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

}

