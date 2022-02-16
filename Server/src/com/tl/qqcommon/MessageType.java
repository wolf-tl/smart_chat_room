package com.tl.qqcommon;

/**
 * @author tl
 * 表示消息类型
 */
public interface MessageType {
    String MESSAGE_SUCCED="1";//表示登录成功
    String MESSAGE_FAIL="2";//表示登录失败
    String MESSAGE_GETUSERLIST="3";
    String MESSAGE_RET_USERLIST="4";//表示返回用户列表
    String MESSAGE_EXIT="5";
    String MESSAGE_PRIVATECHAT="6";//表示私聊
    String MESSAGE_TOALLCHAT="7";//表示群聊
    String MESSAGE_SENDFILES="8";//表示在线发送文件
    String MESSAGE_OFFLINECHAT="9";//表示发送离线信息
}
