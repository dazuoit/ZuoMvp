package com.zuo.demo.lib_common.mine_ble.base.ble_bean;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Handler;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.zuo.demo.lib_common.utils.EmptyUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zuo.
 * @date 2019/3/277.
 * @describe .ble 基础bean
 */

public abstract class BaseBleDevice {
    protected BleDevice mBleDevice; // 蓝牙对象

    protected Map<String, BluetoothGattService> mBluetoothGattServiceList = new HashMap<>(); // 所有服务

    protected String wirteServiceUuid;// 写入服务

    protected String wirteGattCharacteristicUuid;// 写入特征

    protected String readServiceUuid; // 读取服务

    protected String readGattCharacteristicUuid; // 读取特征

    protected MyHandler handler = new MyHandler(this); // handler

    protected static class MyHandler extends Handler {
        private final WeakReference<BaseBleDevice> mBean;

        public MyHandler(BaseBleDevice bean) {
            mBean = new WeakReference<BaseBleDevice>(bean);
        }
    }

    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
    }

    // 自定义EFFROM参数
    public static String tip = "未知";
    public static String NO_ACTIVE_TIME = "未激活";
    protected String deviceModel = tip;// 设备型号

    protected String serialnumber = tip;// 设备序列号
    protected String activateDate = null;// 设备激活时间
    protected byte version = 0; // 版本

    protected String mac;// mac
    protected String name;// 设备名
    protected int mRssi;// 信号值

    // 激活时间相关
    public int checkActivieTimeNum = 0; // 激活时间记录器

    public boolean isGetAll = false; // 是否获取到激活时间序列号,型号

    public BaseBleDevice(BleDevice bleDevice) {
        this.mBleDevice = bleDevice;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (EmptyUtils.isEmpty(mBleDevice)) {
            return;
        }
        this.mac = mBleDevice.getMac();
        this.name = mBleDevice.getName();
        BleManager.getInstance().clearCharacterCallback(mBleDevice);
        BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(mBleDevice);
        if (EmptyUtils.isEmpty(gatt)) {
            return;
        }
        List<BluetoothGattService> serviceList = gatt.getServices();
        for (BluetoothGattService service : serviceList) {
            String uuid_service = service.getUuid().toString();
            mBluetoothGattServiceList.put(uuid_service, service);

            List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristicList) {
                int charaProp = characteristic.getProperties();
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
                    wirteServiceUuid = uuid_service;
                    wirteGattCharacteristicUuid = characteristic.getUuid().toString();


                } else if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    readServiceUuid = uuid_service;
                    readGattCharacteristicUuid = characteristic.getUuid().toString();
                    toGetEpprom();
                }
            }
        }
    }

    /**
     * 获取信息
     */
    protected abstract void toGetEpprom();

    public String getMac() {
        return mac;
    }

    public String getName() {
        return name;
    }

    // 获取设备
    public BleDevice getBleDevice() {
        return mBleDevice;
    }

    // 获取信号强度
    public int getBleDeviceRssi() {
        return mBleDevice.getRssi();
    }

    public String getWirteServiceUuid() {
        return wirteServiceUuid;
    }

    public void setWirteServiceUuid(String wirteServiceUuid) {
        this.wirteServiceUuid = wirteServiceUuid;
    }

    public String getWirteGattCharacteristicUuid() {
        return wirteGattCharacteristicUuid;
    }

    public void setWirteGattCharacteristicUuid(String wirteGattCharacteristicUuid) {
        this.wirteGattCharacteristicUuid = wirteGattCharacteristicUuid;
    }

    public String getReadServiceUuid() {
        return readServiceUuid;
    }

    public void setReadServiceUuid(String readServiceUuid) {
        this.readServiceUuid = readServiceUuid;
    }

    public String getReadGattCharacteristicUuid() {
        return readGattCharacteristicUuid;
    }

    public void setReadGattCharacteristicUuid(String readGattCharacteristicUuid) {
        this.readGattCharacteristicUuid = readGattCharacteristicUuid;
    }

    public void setmRssi(int mRssi) {
        this.mRssi = mRssi;
    }
    // efrom=========================================================================================================================

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public String getActivateDate() {
        return activateDate;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

}
