package com.zuo.demo.lib_common.mine_ble.ble_base;

import android.bluetooth.BluetoothGatt;

import com.blankj.utilcode.util.LogUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.exception.BleException;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.BleDeviceBean;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.BleExceptionBean;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.BleReturnData;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.OnConnectSuccessBean;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.OnDisConnectedBean;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.OnRssiSuccessBean;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.OnWriteSuccessBean;
import com.zuo.demo.lib_common.utils.EmptyUtils;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author zuo
 * @filename: MineBleManager
 * @date: 2020/3/23
 * @description: 蓝牙分发类
 * @version: 5.0.7
 */
public class MineBleManager {

    /**
     * 单例获取
     * @return
     */
    public static MineBleManager getInstance() {
        return MineBleManagerHolder.mMineBleManager;
    }

    private static class MineBleManagerHolder {
        private static MineBleManager mMineBleManager = new MineBleManager();
    }

    /**
     * 扫描
     */
    public void scan() {
        if (BleManager.getInstance().getScanSate() == BleScanState.STATE_SCANNING) {
            cancelScan();
        }
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                post(MineBleEventBusTag.onScanStarted, success);
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                post(MineBleEventBusTag.onLeScan, new BleDeviceBean(bleDevice));
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                post(MineBleEventBusTag.onScanning, new BleDeviceBean(bleDevice));
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                post(MineBleEventBusTag.onScanFinished, scanResultList);
            }
        });

    }

    /**
     * 停止扫描
     */
    public void cancelScan() {
        BleManager.getInstance().cancelScan();
    }

    /**
     * 连接
     * @param bleDevice
     */
    public synchronized void connect(final BleDevice bleDevice) {
        if (EmptyUtils.isEmpty(bleDevice)) {
            return;
        }
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                post(MineBleEventBusTag.onStartConnect, new BleDeviceBean(bleDevice));
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                post(MineBleEventBusTag.onConnectFail, new BleExceptionBean(exception, bleDevice));
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                post(MineBleEventBusTag.onConnectSuccess, new OnConnectSuccessBean(bleDevice, gatt, status));
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                post(MineBleEventBusTag.onDisConnected,
                        new OnDisConnectedBean(isActiveDisConnected, device, gatt, status));
            }
        });
    }

    /**
     * 读
     * @param bleDevice
     * @param uuid_service
     * @param mBluetoothGattCharacteristic
     */
    public synchronized void read(final BleDevice bleDevice, String uuid_service, String mBluetoothGattCharacteristic) {
        BleManager.getInstance().read(bleDevice, uuid_service, mBluetoothGattCharacteristic, new BleReadCallback() {
            @Override
            public void onReadSuccess(byte[] data) {
                post(MineBleEventBusTag.onReadSuccess, new BleReturnData(data, bleDevice));
            }

            @Override
            public void onReadFailure(BleException exception) {
                post(MineBleEventBusTag.onReadFailure, new BleExceptionBean(exception, bleDevice));
            }
        });
    }

    /**
     * 写
     * @param bleDevice
     * @param uuid_service
     * @param uuid_write
     * @param data
     */
    public synchronized void write(final BleDevice bleDevice, String uuid_service, String uuid_write, final byte[] data) {
        BleManager.getInstance().write(bleDevice, uuid_service, uuid_write, data, new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
                post(MineBleEventBusTag.onWriteSuccess, new OnWriteSuccessBean(current, total, justWrite, bleDevice));
            }

            @Override
            public void onWriteFailure(BleException exception) {
                post(MineBleEventBusTag.onWriteFailure, new BleExceptionBean(exception, bleDevice));
            }
        });
    }

    /**
     * 打开通知
     * @param bleDevice
     * @param uuid_service
     * @param mBluetoothGattCharacteristic
     */
    public void notifyOpen(final BleDevice bleDevice, String uuid_service, String mBluetoothGattCharacteristic) {
        BleManager.getInstance().notify(bleDevice, uuid_service, mBluetoothGattCharacteristic, new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                post(MineBleEventBusTag.onNotifySuccess, new BleDeviceBean(bleDevice));
            }

            @Override
            public void onNotifyFailure(BleException exception) {
                post(MineBleEventBusTag.onNotifyFailure, new BleExceptionBean(exception, bleDevice));
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                post(MineBleEventBusTag.onCharacteristicChanged, new BleReturnData(data, bleDevice));
            }
        });
    }

    /**
     * 监听通知(失败反复) indicate是一定会收到数据，notify有可能会丢失数据。indicate底层封装了应答机制，如果没有收到中央设备的回应，会再次发送直至成功
     * @param bleDevice
     * @param uuid_service
     * @param mBluetoothGattCharacteristic
     */
    public void indicateOpen(final BleDevice bleDevice, String uuid_service, String mBluetoothGattCharacteristic) {
        BleManager.getInstance().indicate(bleDevice, uuid_service, mBluetoothGattCharacteristic,
                new BleIndicateCallback() {
                    @Override
                    public void onIndicateSuccess() {
                        post(MineBleEventBusTag.onIndicateSuccess, new BleDeviceBean(bleDevice));
                    }

                    @Override
                    public void onIndicateFailure(BleException exception) {
                        post(MineBleEventBusTag.onIndicateFailure, new BleExceptionBean(exception, bleDevice));
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        post(MineBleEventBusTag.indicate_onCharacteristicChanged, new BleReturnData(data, bleDevice));
                    }
                });
    }

    /**
     * 读取信号值
     * @param bleDevice
     */
    public synchronized void readRssi(final BleDevice bleDevice) {
        BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
            @Override
            public void onRssiFailure(BleException exception) {
                post(MineBleEventBusTag.onIndicateFailure, new BleExceptionBean(exception, bleDevice));
            }

            @Override
            public void onRssiSuccess(int rssi) {
                post(MineBleEventBusTag.onRssiSuccess, new OnRssiSuccessBean(rssi, bleDevice));
            }
        });
    }

    /**
     * 分发
     * @param tag
     * @param object
     */
    public void post(String tag, Object object) {
        if (tag != null) {
            LogUtils.w("ble_post",tag);
            EventBus.getDefault().post(new EventMsg(tag, object));
        }
    }

    /**
     * 释放
     */
    public void release() {
        BleManager.getInstance().disconnectAllDevice();
        post(MineBleEventBusTag.release, true);
    }
}
