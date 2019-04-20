package com.smartgreen.model;

public class ExtendedEvent extends Event {
    /**
     * 成功发送次数
     */
    private int send = 0;

    /**
     * 最后成功发送时间
     */
    private long lastsent = 0;

    /**
     * 增加一次成功发送计数
     */
    public void plusOneSend() {
        send++;
    }

    public int getSend() {
        return send;
    }

    public long getLastsent() {
        return lastsent;
    }

    public void setLastsent(long lastsent) {
        this.lastsent = lastsent;
    }
}
