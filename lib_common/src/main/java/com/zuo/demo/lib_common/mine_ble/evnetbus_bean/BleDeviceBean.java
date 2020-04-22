package com.zuo.demo.lib_common.mine_ble.evnetbus_bean;

import com.clj.fastble.data.BleDevice;

/**
 * @author zuo
 * @filename: BleDeviceBean
 * @date: 2020/3/23
 * @description: 分发基类
 * @version: 5.0.7
 */
public class BleDeviceBean {
    public BleDeviceBean(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    public BleDevice bleDevice;
}
