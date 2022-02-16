package com.tl.qqcommon;

import java.io.Serializable;

/**
 * @author tl
 * 表示消息内容
 */
public class Messages implements Serializable {
    private static final long serialVersionUID =1L;
    private String sender;
    private String getter;
    private String content;
    private String sendTime;
    private String mesType;
    //扩充文件需要的属性
    private byte[] file;
    private String src;
    private String tar;

    public Messages(String sender, String getter, String content, String sendTime, String mesType) {
        this.sender = sender;
        this.getter = getter;
        this.content = content;
        this.sendTime = sendTime;
        this.mesType = mesType;
    }
    public Messages(){

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }
}
