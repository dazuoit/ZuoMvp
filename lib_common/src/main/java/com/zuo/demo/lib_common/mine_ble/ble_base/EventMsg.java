package com.zuo.demo.lib_common.mine_ble.ble_base;



public class EventMsg {
    public String mTag;//消息tag
    public Object mData;//消息内容

    public EventMsg(String tag, Object object) {
        mTag = tag;
        mData = object;
    }

    public String getTag() {
        return mTag;
    }

    public Object getData() {
        return mData;
    }
}
