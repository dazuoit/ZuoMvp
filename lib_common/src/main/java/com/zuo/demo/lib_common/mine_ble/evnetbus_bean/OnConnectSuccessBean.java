package com.zuo.demo.lib_common.mine_ble.evnetbus_bean;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.data.BleDevice;

/**
 * @author zuo
 * @filename: OnConnectSuccessBean
 * @date: 2020/3/23
 * @description: 描述
 * @version: 版本号
 */
public class OnConnectSuccessBean extends BleDeviceBean {
    public OnConnectSuccessBean(BleDevice bleDevice, BluetoothGatt gatt, int status) {
        super(bleDevice);
        this.bleDevice = bleDevice;
        this.gatt = gatt;
        this.status = status;
    }

    public BluetoothGatt gatt;
    public int status;
}
