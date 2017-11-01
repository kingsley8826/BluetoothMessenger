package com.fu.bluetoothmessager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fu.bluetoothmessager.R;
import com.fu.bluetoothmessager.model.Device;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;


public class ContactAdapter extends  RecyclerView.Adapter<ContactViewHolder>{

    private ArrayList<Device> devices = new ArrayList<>();

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
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
    public void addDevice(Device device) {
        devices.add(device);
        notifyDataSetChanged();
    }

}
