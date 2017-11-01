package com.fu.bluetoothmessager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.fu.bluetoothmessager.R;
import com.fu.bluetoothmessager.database.RealmContext;
import com.fu.bluetoothmessager.model.ChatData;
import com.fu.bluetoothmessager.model.Device;
import com.fu.bluetoothmessager.util.Constants;

import io.realm.RealmList;


public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private RealmList<ChatData> items;
    private Device device;

    public ConversationAdapter(Device device) {
        this.device = device;
        this.items = device.getConversationList();
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case Constants.TYPE_YOU:
                View v2 = inflater.inflate(R.layout.layout_holder_you, viewGroup, false);
                viewHolder = new HolderYouAndMe(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.layout_holder_me, viewGroup, false);
                viewHolder = new HolderYouAndMe(v);
                break;
        }
        return viewHolder;
    }

    public void addItem(ChatData chatData) {
        RealmContext.getInstance().insertChatData(device, chatData);
        items = device.getConversationList();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case Constants.TYPE_YOU:
                HolderYouAndMe hy = (HolderYouAndMe) viewHolder;
                configureViewHolderYou(hy, position);
                break;
            default:
                HolderYouAndMe hm = (HolderYouAndMe) viewHolder;
                configureViewHolderMe(hm, position);
                break;
        }
    }

    private void configureViewHolderMe(HolderYouAndMe vh1, int position) {
        vh1.getTime().setText(items.get(position).getTime());
        vh1.getChatText().setText(items.get(position).getText());
    }

    private void configureViewHolderYou(HolderYouAndMe vh1, int position) {
        vh1.getTime().setText(items.get(position).getTime());
        vh1.getChatText().setText(items.get(position).getText());
    }

}
