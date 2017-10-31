package com.fu.bluetoothmessager.recyclerchat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.fu.bluetoothmessager.R;
import com.fu.bluetoothmessager.model.Device;
import com.fu.bluetoothmessager.util.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tuan-FPT on 31/10/2017.
 */

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<ChatData> items;
    private Context mContext;

    private final int YOU = 1;
    private final int ME = 2;

    public ConversationAdapter(Context context, Device device) {
        this.mContext = context;
        this.items = device.getConversationList();
    }




    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        //More to come
        if (items.get(position).getType() == Type.YOU) {
            return YOU;
        } else if (items.get(position).getType() == Type.ME) {
            return ME;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case YOU:
                View v2 = inflater.inflate(R.layout.layout_holder_you, viewGroup, false);
                viewHolder = new HolderYou(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.layout_holder_me, viewGroup, false);
                viewHolder = new HolderMe(v);
                break;
        }
        return viewHolder;
    }

    public void addItem(ChatData chatData) {
        items.add(chatData);
        notifyDataSetChanged();
    }

    public void addItem(List<ChatData> item) {
        items.addAll(item);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case YOU:
                HolderYou vh2 = (HolderYou) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            default:
                HolderMe vh = (HolderMe) viewHolder;
                configureViewHolder3(vh, position);
                break;
        }
    }

    private void configureViewHolder3(HolderMe vh1, int position) {
        vh1.getTime().setText(items.get(position).getTime());
        vh1.getChatText().setText(items.get(position).getText());
    }

    private void configureViewHolder2(HolderYou vh1, int position) {
        vh1.getTime().setText(items.get(position).getTime());
        vh1.getChatText().setText(items.get(position).getText());
    }

}
