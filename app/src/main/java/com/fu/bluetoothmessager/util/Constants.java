/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fu.bluetoothmessager.util;

import com.fu.bluetoothmessager.service.BluetoothChatService;

/**
 * Defines several constants used between {@link BluetoothChatService} and the UI.
 */
public class Constants {

    // This variable to handle this fragment auto load by viewpager or not
    public static boolean AUTO_LOAD_DEVICE = true;

    public static final int TYPE_YOU = 0;
    public static final int TYPE_ME = 1;


    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int CONNECT_WRONG = 5;
    public static final int START_CHAT = 6;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_KEY = "device_key";
    public static final String TOAST = "toast";
    public static final String BLUETOOTH_SOCKET = "bluetooth_socket";

    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    public static int CURRENT_STATE;

}
