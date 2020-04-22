package com.zuo.demo.lib_common.mine_ble.evnetbus_bean;

import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

/**
 * @author zuo
 * @filename: BleExceptionBean
 * @date: 2020/3/23
 * @description: 异常
 * @version: 5.0.7
 */
public class BleExceptionBean extends BleDeviceBean {
    public BleExceptionBean(BleException exception, BleDevice bleDevice) {
        super(bleDevice);
        this.exception = exception;
    }

    public BleException exception;

}
