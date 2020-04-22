package com.zuo.demo.lib_common.mine_ble.evnetbus_bean;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.data.BleDevice;

/**
 * @author zuo
 * @filename: OnDisConnectedBean
 * @date: 2020/3/23
 * @description: 描述
 * @version: 版本号
 */
public class OnDisConnectedBean extends BleDeviceBean {
    public OnDisConnectedBean(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
        super(device);
        this.isActiveDisConnected = isActiveDisConnected;
        this.gatt = gatt;
        this.status = status;
    }

    public boolean isActiveDisConnected;
    public BluetoothGatt gatt;
    public int status;
}
