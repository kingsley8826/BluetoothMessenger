package com.fu.bluetoothmessager.model;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.Serializable;

/**
 * Created by Tuan-FPT on 30/10/2017.
 */

public class BDeviceSocket implements Serializable {

    private static transient  BluetoothSocket bluetoothSocket = null;
    private static transient BluetoothDevice bluetoothDevice;

    public BDeviceSocket(BluetoothSocket bluetoothSocket, BluetoothDevice bluetoothDevice) {
        this.bluetoothSocket = bluetoothSocket;
        this.bluetoothDevice = bluetoothDevice;
    }

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}
