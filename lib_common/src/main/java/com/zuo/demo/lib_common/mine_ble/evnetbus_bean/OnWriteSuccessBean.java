package com.zuo.demo.lib_common.mine_ble.evnetbus_bean;

import com.clj.fastble.data.BleDevice;

/**
 * @author zuo
 * @filename: OnWriteSuccessBean
 * @date: 2020/3/23
 * @description: 描述
 * @version: 版本号
 */
public class OnWriteSuccessBean extends BleDeviceBean {
    public OnWriteSuccessBean(int current, int total, byte[] justWrite, BleDevice bleDevice) {
        super(bleDevice);
        this.current = current;
        this.total = total;
        this.justWrite = justWrite;
    }

    public int current;
    public int total;
    public byte[] justWrite;
}
