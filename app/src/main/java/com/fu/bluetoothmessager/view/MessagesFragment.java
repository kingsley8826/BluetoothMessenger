package com.fu.bluetoothmessager.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fu.bluetoothmessager.R;
import com.fu.bluetoothmessager.adapter.MessageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {

    @BindView(R.id.rvMessagesDevices)
    RecyclerView rvMessagesDevices;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        setupUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setupUI() {
        rlEmpty.setVisibility(View.GONE);
        MessageAdapter messageAdapter = new MessageAdapter();
        rvMessagesDevices.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMessagesDevices.setAdapter(messageAdapter);
        if (messageAdapter.getItemCount() == 0) {
            rlEmpty.setVisibility(View.VISIBLE);
        }
    }
}
