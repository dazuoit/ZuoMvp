package com.zuo.demo.lib_common.mine_ble.ble_base;


import com.zuo.demo.lib_common.mine_ble.base.controller.BaseControllerTools;
import com.zuo.demo.lib_common.utils.EmptyUtils;

/**
 * @author zuo
 * @filename: MineBleUtils
 * @date: 2020/3/23
 * @description: 蓝牙连接工具类
 * @version: 5.0.7
 */
public class MineBleUtils {
    /**
     * 校验名字
     * @param name
     * @param evnetTag
     * @return
     */
    public static boolean checkNames(String name, BaseControllerTools evnetTag) {
        if (EmptyUtils.isEmpty(name) || EmptyUtils.isEmpty(evnetTag)) {
            return false;
        }

        return false;
    }
}
