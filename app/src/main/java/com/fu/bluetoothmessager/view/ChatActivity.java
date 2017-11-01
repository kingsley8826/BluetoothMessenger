package com.fu.bluetoothmessager.view;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fu.bluetoothmessager.BaseActivity;
import com.fu.bluetoothmessager.R;
import com.fu.bluetoothmessager.database.RealmContext;
import com.fu.bluetoothmessager.model.BDeviceSocket;
import com.fu.bluetoothmessager.util.Constants;
import com.fu.bluetoothmessager.model.Device;
import com.fu.bluetoothmessager.model.ChatData;
import com.fu.bluetoothmessager.adapter.ConversationAdapter;
import com.fu.bluetoothmessager.service.BluetoothChatService;
import com.fu.bluetoothmessager.util.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity {

    @BindView(R.id.rvConversationView)
    RecyclerView rvConversationView;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.btnSend)
    ImageView btnSend;


    private ConversationAdapter mConversationArrayAdapter;
    private StringBuffer mOutStringBuffer;
    private Device currentDevice;

    private BluetoothDevice bDevice = null;
    private BluetoothChatService mChatService = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        BDeviceSocket deviceSocket = (BDeviceSocket) intent.getSerializableExtra(Constants.BLUETOOTH_SOCKET);
        BluetoothSocket mBluetoothSocket = deviceSocket.getBluetoothSocket();
        mChatService = new BluetoothChatService(mBluetoothSocket, mHandler);
        mChatService.start();

        bDevice = deviceSocket.getBluetoothDevice();

        Device realmDevice = RealmContext.getInstance().getDevice(bDevice.getAddress());
        if (realmDevice == null) {
            currentDevice = new Device(bDevice.getName(), bDevice.getAddress());
        } else {
            currentDevice = realmDevice;
        }


        setupToolbarWithUpNav(R.id.toolbar, currentDevice.getName(), R.drawable.ic_back);
        setupChat();
    }

    private void setupChat() {
        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ConversationAdapter(currentDevice);

        rvConversationView.setLayoutManager(new LinearLayoutManager(this));
        rvConversationView.setAdapter(mConversationArrayAdapter);
        rvConversationView.scrollToPosition(mConversationArrayAdapter.getItemCount() - 1);

        mOutStringBuffer = new StringBuffer("");

        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String message = etMessage.getText().toString();
                sendMessage(message);
            }
        });

    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {

        if (Constants.CURRENT_STATE != Constants.STATE_CONNECTED) {
            Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            etMessage.setText(mOutStringBuffer);
        }
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
                    ChatData writeChatData = new ChatData(Constants.TYPE_ME, writeMessage, DateTime.getCurrentTime());

                    // add to adapter and realm
                    mConversationArrayAdapter.addItem(writeChatData);
                    rvConversationView.scrollToPosition(mConversationArrayAdapter.getItemCount() - 1);
                    break;


                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    ChatData readChatData = new ChatData(Constants.TYPE_YOU, readMessage, DateTime.getCurrentTime());

                    // add to adapter and realm
                    mConversationArrayAdapter.addItem(readChatData);
                    rvConversationView.scrollToPosition(mConversationArrayAdapter.getItemCount() - 1);
                    break;


                case Constants.CONNECT_WRONG:
                    Toast.makeText(getBaseContext(), msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_userphoto, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }
}
