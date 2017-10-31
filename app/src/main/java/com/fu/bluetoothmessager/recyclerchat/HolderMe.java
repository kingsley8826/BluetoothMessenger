package com.fu.bluetoothmessager.recyclerchat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fu.bluetoothmessager.R;


/**
 * Created by Tuan-FPT on 31/10/2017.
 */

public class HolderMe extends RecyclerView.ViewHolder {

    private TextView time, chatText;

    public HolderMe(View v) {
        super(v);
        time = (TextView) v.findViewById(R.id.tv_time);
        chatText = (TextView) v.findViewById(R.id.tv_chat_text);
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public TextView getChatText() {
        return chatText;
    }

    public void setChatText(TextView chatText) {
        this.chatText = chatText;
    }
}
