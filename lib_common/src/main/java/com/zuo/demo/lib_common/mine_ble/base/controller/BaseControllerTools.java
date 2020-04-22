package com.zuo.demo.lib_common.mine_ble.base.controller;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.zuo.demo.lib_common.mine_ble.ble_base.EventMsg;
import com.zuo.demo.lib_common.mine_ble.ble_base.MineBleEventBusTag;
import com.zuo.demo.lib_common.mine_ble.ble_base.MineBleManager;
import com.zuo.demo.lib_common.mine_ble.ble_base.MineBleUtils;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.BleDeviceBean;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.BleExceptionBean;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.BleReturnData;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.OnConnectSuccessBean;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.OnDisConnectedBean;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.OnRssiSuccessBean;
import com.zuo.demo.lib_common.mine_ble.evnetbus_bean.OnWriteSuccessBean;
import com.zuo.demo.lib_common.utils.EmptyUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * @author zuo
 * @filename: BaseControllerTools
 * @date: 2020/3/23
 * @description: 控制基类
 * @version: 5.0.7
 */
public abstract class BaseControllerTools {

    private BaseControllerTools eventbusTag;// eventbusTag

    public void setEventbusTag(BaseControllerTools evnetTag) {
        this.eventbusTag = evnetTag;
        if (!isEventBusRegistered(eventbusTag)) {
            EventBus.getDefault().register(eventbusTag);
        }
    }

    /**
     * 是否注册
     *
     * @param subscribe
     * @return
     */
    public boolean isEventBusRegistered(Object subscribe) {
        return EventBus.getDefault().isRegistered(subscribe);
    }

    // 停止扫描
    public void cancelScan() {
        BleManager.getInstance().cancelScan();
    }

    // 断开连接
    public void stopdDevice(BleDevice bleDevice) {

        if (BleManager.getInstance().isConnected(bleDevice)) {
            BleManager.getInstance().disconnect(bleDevice);
        }
    }

    // 关闭通知
    public void notifyClose(BleDevice bleDevice, String uuid_service, String mBluetoothGattCharacteristic) {
        BleManager.getInstance().stopNotify(bleDevice, uuid_service, mBluetoothGattCharacteristic);
    }

    // 关闭通知
    public void indicateClose(BleDevice bleDevice, String uuid_service, String mBluetoothGattCharacteristic) {
        BleManager.getInstance().stopIndicate(bleDevice, uuid_service, mBluetoothGattCharacteristic);
    }

    // 蓝牙扫描
    public void startScan() {
        MineBleManager.getInstance().scan();
    }

    // 连接蓝牙设备
    public synchronized void connect(final BleDevice bleDevice) {
        MineBleManager.getInstance().connect(bleDevice);
    }

    // 读取信息
    public void read(BleDevice bleDevice, String uuid_service, String mBluetoothGattCharacteristic) {
        MineBleManager.getInstance().read(bleDevice, uuid_service, mBluetoothGattCharacteristic);
    }

    // 写入数据
    public synchronized void write(BleDevice bleDevice, String uuid_service, String uuid_write, byte[] data) {
        MineBleManager.getInstance().write(bleDevice, uuid_service, uuid_write, data);
    }

    // 监听通知
    public void notifyOpen(BleDevice bleDevice, String uuid_service, String mBluetoothGattCharacteristic) {
        MineBleManager.getInstance().notifyOpen(bleDevice, uuid_service, mBluetoothGattCharacteristic);
    }

    // 监听通知(失败反复) indicate是一定会收到数据，notify有可能会丢失数据。indicate底层封装了应答机制，如果没有收到中央设备的回应，会再次发送直至成功
    public void indicateOpen(BleDevice bleDevice, String uuid_service, String mBluetoothGattCharacteristic) {
        MineBleManager.getInstance().notifyOpen(bleDevice, uuid_service, mBluetoothGattCharacteristic);
    }

    public void readRssi(BleDevice bleDevice) {
        MineBleManager.getInstance().readRssi(bleDevice);
    }

    /**
     * 释放
     */
    public void releaseSelf() {
        if (isEventBusRegistered(eventbusTag)) {
            EventBus.getDefault().unregister(eventbusTag);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMineBleManager(EventMsg eventMsg) {
        if (eventMsg == null || EmptyUtils.isEmpty(eventMsg.getTag()) || EmptyUtils.isEmpty(eventMsg.getData())) {
            return;
        }
        String tag = eventMsg.getTag();
        Object object = eventMsg.getData();
        if (tag.equals(MineBleEventBusTag.release)) {
            release();
        } else if (MineBleEventBusTag.onScanStarted.equals(tag) && object instanceof Boolean) {
            // 扫描开始
            onScanStarted((Boolean) object);
        } else if (MineBleEventBusTag.onScanFinished.equals(tag) && object instanceof List) {
            // 扫描结束
            onScanFinished((List<BleDevice>) object);
        } else {
            if (object instanceof BleDeviceBean) {
                BleDeviceBean bleDeviceBean = (BleDeviceBean) object;
                if (bleDeviceBean.bleDevice != null
                        && MineBleUtils.checkNames(bleDeviceBean.bleDevice.getName(), eventbusTag)) {
                    switch (tag) {
                    case MineBleEventBusTag.onLeScan:
                        onLeScan(bleDeviceBean.bleDevice);
                        break;
                    case MineBleEventBusTag.onScanning:
                        onScanning(bleDeviceBean.bleDevice);
                        break;
                    case MineBleEventBusTag.onStartConnect:
                        onStartConnect(bleDeviceBean.bleDevice);
                        break;
                    case MineBleEventBusTag.onConnectFail:
                        if (bleDeviceBean instanceof BleExceptionBean) {
                            BleExceptionBean bleExceptionBean = (BleExceptionBean) bleDeviceBean;
                            onConnectFail(bleExceptionBean.bleDevice, bleExceptionBean.exception);
                        }
                        break;
                    case MineBleEventBusTag.onConnectSuccess:
                        if (bleDeviceBean instanceof OnConnectSuccessBean) {
                            OnConnectSuccessBean onConnectSuccessBean = (OnConnectSuccessBean) bleDeviceBean;
                            onConnectSuccess(onConnectSuccessBean.bleDevice, onConnectSuccessBean.gatt,
                                    onConnectSuccessBean.status);
                        }
                        break;
                    case MineBleEventBusTag.onDisConnected:
                        if (bleDeviceBean instanceof OnDisConnectedBean) {
                            OnDisConnectedBean onDisConnectedBean = (OnDisConnectedBean) bleDeviceBean;
                            onDisConnected(onDisConnectedBean.isActiveDisConnected, onDisConnectedBean.bleDevice,
                                    onDisConnectedBean.gatt, onDisConnectedBean.status);
                        }
                        break;
                    case MineBleEventBusTag.onReadSuccess:
                        if (bleDeviceBean instanceof BleReturnData) {
                            BleReturnData bleReturnData = (BleReturnData) bleDeviceBean;
                            onReadSuccess(bleReturnData.data, bleDeviceBean.bleDevice);
                        }
                        break;
                    case MineBleEventBusTag.onReadFailure:
                        if (bleDeviceBean instanceof BleExceptionBean) {
                            BleExceptionBean bleExceptionBean = (BleExceptionBean) bleDeviceBean;
                            onReadFailure(bleExceptionBean.exception, bleExceptionBean.bleDevice);
                        }
                        break;
                    case MineBleEventBusTag.onWriteSuccess:
                        if (bleDeviceBean instanceof OnWriteSuccessBean) {
                            OnWriteSuccessBean onWriteSuccessBean = (OnWriteSuccessBean) bleDeviceBean;
                            onWriteSuccess(onWriteSuccessBean.current, onWriteSuccessBean.total,
                                    onWriteSuccessBean.justWrite, onWriteSuccessBean.bleDevice);
                        }
                        break;
                    case MineBleEventBusTag.onWriteFailure:
                        if (bleDeviceBean instanceof BleExceptionBean) {
                            BleExceptionBean bleExceptionBean = (BleExceptionBean) bleDeviceBean;
                            onWriteFailure(bleExceptionBean.exception, bleExceptionBean.bleDevice);
                        }
                        break;
                    case MineBleEventBusTag.onNotifySuccess:
                        onNotifySuccess(bleDeviceBean.bleDevice);
                        break;
                    case MineBleEventBusTag.onNotifyFailure:
                        if (bleDeviceBean instanceof BleExceptionBean) {
                            BleExceptionBean bleExceptionBean = (BleExceptionBean) bleDeviceBean;
                            onNotifyFailure(bleExceptionBean.exception, bleExceptionBean.bleDevice);
                        }
                        break;
                    case MineBleEventBusTag.onCharacteristicChanged:
                        if (bleDeviceBean instanceof BleReturnData) {
                            BleReturnData bleReturnData = (BleReturnData) bleDeviceBean;
                            onCharacteristicChanged(bleReturnData.data, bleDeviceBean.bleDevice);
                        }
                        break;
                    case MineBleEventBusTag.onIndicateSuccess:
                        onIndicateSuccess(bleDeviceBean.bleDevice);
                        break;
                    case MineBleEventBusTag.onIndicateFailure:
                        if (bleDeviceBean instanceof BleExceptionBean) {
                            BleExceptionBean bleExceptionBean = (BleExceptionBean) bleDeviceBean;
                            onIndicateFailure(bleExceptionBean.exception, bleExceptionBean.bleDevice);
                        }
                        break;
                    case MineBleEventBusTag.indicate_onCharacteristicChanged:
                        if (bleDeviceBean instanceof BleReturnData) {
                            BleReturnData bleReturnData = (BleReturnData) bleDeviceBean;
                            onIndicateCharacteristicChanged(bleReturnData.data, bleDeviceBean.bleDevice);
                        }
                        break;
                    case MineBleEventBusTag.onRssiFailure:
                        if (bleDeviceBean instanceof BleExceptionBean) {
                            BleExceptionBean bleExceptionBean = (BleExceptionBean) bleDeviceBean;
                            onRssiFailure(bleExceptionBean.exception, bleExceptionBean.bleDevice);
                        }
                        break;
                    case MineBleEventBusTag.onRssiSuccess:
                        if (bleDeviceBean instanceof OnRssiSuccessBean) {
                            OnRssiSuccessBean onRssiSuccessBean = (OnRssiSuccessBean) bleDeviceBean;
                            onRssiSuccess(onRssiSuccessBean.rssi, onRssiSuccessBean.bleDevice);
                        }
                        break;
                    default:
                        break;
                    }
                }
            }
        }
    }

    /**
     * scan
     */
    public abstract void onScanStarted(boolean success);

    public abstract void onLeScan(BleDevice bleDevice);

    public abstract void onScanning(BleDevice bleDevice);

    public abstract void onScanFinished(List<BleDevice> scanResultList);

    /**
     * connect
     */
    public abstract void onStartConnect(BleDevice bleDevice);

    public abstract void onConnectFail(BleDevice bleDevice, BleException exception);

    public abstract void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status);

    public abstract void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status);

    /**
     * read
     */
    public abstract void onReadSuccess(byte[] data, BleDevice bleDevice);

    public abstract void onReadFailure(BleException exception, BleDevice bleDevice);

    /**
     * write
     */
    public abstract void onWriteSuccess(int current, int total, byte[] justWrite, BleDevice bleDevice);

    public abstract void onWriteFailure(BleException exception, BleDevice bleDevice);

    /**
     * notify
     */
    public abstract void onNotifySuccess(BleDevice bleDevice);

    public abstract void onNotifyFailure(BleException exception, BleDevice bleDevice);

    public abstract void onCharacteristicChanged(byte[] data, BleDevice bleDevice);

    /**
     * indicateOpen
     */
    public abstract void onIndicateSuccess(BleDevice bleDevice);

    public abstract void onIndicateFailure(BleException exception, BleDevice bleDevice);

    public abstract void onIndicateCharacteristicChanged(byte[] data, BleDevice bleDevice);

    /**
     * readRssi
     */
    public abstract void onRssiFailure(BleException exception, BleDevice bleDevice);

    public abstract void onRssiSuccess(int rssi, BleDevice bleDevice);

    // 释放
    public abstract void release();
}
