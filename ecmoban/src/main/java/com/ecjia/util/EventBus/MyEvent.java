package com.ecjia.util.EventBus;

import com.ecjia.entity.responsemodel.MYMESSAGE;

/**
 * Created by Administrator on 2015/2/5.
 */
public class MyEvent extends Event {

    public MyEvent(boolean flag, int tag) {
        super(flag, tag);
        this.flag = flag;
        this.mTag = tag;
    }

    public MyEvent(String msg, int tag) {
        super(msg, tag);
        this.msg = msg;
        this.mTag = tag;
    }

    public MyEvent(String msg) {
        super(msg);
        this.msg = msg;
    }

    public MyEvent(String msg,String value) {
        super(msg,value);
        this.msg = msg;
        this.value=value;
    }

    public MyEvent(int tag) {
        super(tag);
        this.mTag = tag;
    }

    public MyEvent(boolean flag) {
        super(flag);
        this.flag = flag;
    }

    @Override
    void myEvent() {

    }

    public MYMESSAGE getMyMessage() {
        return myMessage;
    }

    public void setMyMessage(MYMESSAGE myMessage) {
        this.myMessage = myMessage;
    }

    private MYMESSAGE myMessage;
}
