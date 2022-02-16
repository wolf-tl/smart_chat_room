package com.tl.client01;

import com.tl.qqcommon.MessageType;
import com.tl.qqcommon.Messages;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author tl
 */
public class Client01Socket extends Thread{
    Socket socket;
    FileOutputStream fileOutputStream;
    public Client01Socket(){

    }
    public Client01Socket(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        while(true){
            try {
                //如果没有数据传过来，就会阻塞
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Messages messages=(Messages) objectInputStream.readObject();
                //显示用户列表
                if(messages.getMesType().equals(MessageType.MESSAGE_RET_USERLIST)){
                    System.out.println(messages.getGetter()+"数据传输中");
                    System.out.println(messages.getContent());
                }
                //私聊
                else if(messages.getMesType().equals(MessageType.MESSAGE_PRIVATECHAT)){
                    System.out.println(messages.getSendTime()+":  "+messages.getSender()+": "+messages.getContent());
                }
                //群聊
                else if(messages.getMesType().equals(MessageType.MESSAGE_TOALLCHAT)){
                    System.out.println(messages.getSender()+": "+messages.getContent());
                }
                //在线接收文件到指定路径下
                else if(messages.getMesType().equals(MessageType.MESSAGE_SENDFILES)){
                    fileOutputStream=new FileOutputStream(messages.getTar());
                    fileOutputStream.write(messages.getFile());
                    fileOutputStream.close();
                }
                //接收离线信息，并显示发送者
                else if(messages.getMesType().equals(MessageType.MESSAGE_OFFLINECHAT)){
                    System.out.println(messages.getSender()+": "+messages.getContent());
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
