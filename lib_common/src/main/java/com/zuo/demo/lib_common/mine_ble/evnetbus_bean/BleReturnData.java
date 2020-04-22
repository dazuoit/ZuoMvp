package com.zuo.demo.lib_common.mine_ble.evnetbus_bean;

import com.clj.fastble.data.BleDevice;

/**
 * @author zuo
 * @filename: BleReturnData
 * @date: 2020/3/23
 * @description: 带参数的类
 * @version: 5.0.7
 */
public class BleReturnData extends BleDeviceBean {
    public BleReturnData(byte[] data, BleDevice bleDevice) {
        super(bleDevice);
        this.data = data;
    }

    public byte[] data;
}
