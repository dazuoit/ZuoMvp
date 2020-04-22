package com.zuo.demo.lib_common.mine_ble.base.callback;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.util.List;

/**
 * @author zuo
 * @filename: BaseBleCallBack
 * @date: 2020/3/25
 * @description: 蓝牙通信基础回调
 * @version: 都可选择性重写
 */
public interface BaseBleCallBack {
    default void onScanStarted(boolean success) {

    }

    default void onLeScan(BleDevice bleDevice) {

    }

    default void onScanning(BleDevice bleDevice) {

    }

    default void onScanFinished(List<BleDevice> scanResultList) {

    }

    default void onStartConnect(BleDevice bleDevice) {

    }

    default void onConnectFail(BleDevice bleDevice, BleException exception) {

    }

    default void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {

    }

    default void onDisConnected(BleDevice bleDevice, boolean isActiveDisConnected, BluetoothGatt gatt, int status) {

    }

    default void onReadSuccess(BleDevice bleDevice, byte[] data) {

    }

    default void onReadFailure(BleDevice bleDevice, BleException exception) {

    }

    default void onWriteSuccess(BleDevice bleDevice, final int current, final int total, final byte[] justWrite) {

    }

    default void onWriteFailure(BleDevice bleDevice, BleException exception) {

    }

    default void onNotifySuccess(BleDevice bleDevice) {

    }

    default void onNotifyFailure(BleDevice bleDevice, final BleException exception) {

    }

    default void onIndicateSuccess(BleDevice bleDevice) {

    }

    default void onIndicateFailure(BleDevice bleDevice, final BleException exception) {

    }

    default void onIndicateCharacteristicChanged(BleDevice bleDevice, byte[] data) {

    }

    default void onCharacteristicChanged(BleDevice bleDevice, byte[] data) {

    }

    default void onRssiSuccess(BleDevice bleDevice, int rssi) {

    }

    default void onRssiFailure(BleDevice bleDevice, BleException exception) {

    }

}
