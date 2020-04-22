package com.zuo.demo.lib_common.mine_ble.evnetbus_bean;

import com.clj.fastble.data.BleDevice;

/**
 * @author zuo
 * @filename: OnRssiSuccessBean
 * @date: 2020/3/23
 * @description: 描述
 * @version: 版本号
 */
public class OnRssiSuccessBean extends BleDeviceBean {
    public OnRssiSuccessBean(int rssi, BleDevice bleDevice) {
        super(bleDevice);
        this.rssi = rssi;
    }

    public int rssi;
}
