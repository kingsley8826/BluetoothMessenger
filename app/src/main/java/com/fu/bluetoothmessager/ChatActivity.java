package com.fu.bluetoothmessager;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.fu.bluetoothmessager.model.BDeviceSocket;
import com.fu.bluetoothmessager.model.Constants;
import com.fu.bluetoothmessager.model.Device;
import com.fu.bluetoothmessager.recyclerchat.ChatData;
import com.fu.bluetoothmessager.recyclerchat.ConversationAdapter;
import com.fu.bluetoothmessager.service.BluetoothChatService;
import com.fu.bluetoothmessager.util.DateTime;
import com.fu.bluetoothmessager.util.Type;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity {

    @BindView(R.id.lvConversationView)
    RecyclerView lvConversationView;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.btnSend)
    ImageView btnSend;


    private ConversationAdapter mConversationArrayAdapter;
    private StringBuffer mOutStringBuffer;

    private BluetoothDevice mDevice = null;
    private BluetoothChatService mChatService = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        BDeviceSocket deviceSocket = (BDeviceSocket) intent.getSerializableExtra("Device_info");
        BluetoothSocket mBluetoothSocket = deviceSocket.getBluetoothSocket();
        mDevice = deviceSocket.getBluetoothDevice();
        mChatService = new BluetoothChatService(mBluetoothSocket, mHandler);
        mChatService.start();

        setupToolbarWithUpNav(R.id.toolbar, mDevice.getName(), R.drawable.ic_back);
        setupChat();
    }

    private void setupChat() {
        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ConversationAdapter(this, new Device(mDevice.getName(), mDevice.getAddress()));

        lvConversationView.setLayoutManager(new LinearLayoutManager(this));
        lvConversationView.setAdapter(mConversationArrayAdapter);

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
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.addItem(new ChatData(Type.ME, writeMessage, DateTime.getCurrentTime()));
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mConversationArrayAdapter.addItem(new ChatData(Type.YOU, readMessage, DateTime.getCurrentTime()));
                    break;
//                case Constants.MESSAGE_DEVICE_NAME:
//                    // save the connected device's name
//                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
//                    if (null != this) {
//                        Toast.makeText(this, "Connected to "
//                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
//                    }
//                    break;
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
