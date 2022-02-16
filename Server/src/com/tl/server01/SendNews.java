package com.tl.server01;

import com.tl.qqcommon.MessageType;
import com.tl.qqcommon.Messages;
import com.tl.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author tl
 * 新建一个线程用于给所有在线用户推送新闻
 */
public class SendNews extends Thread{
    private Server01SocketManager server01SocketManager;

    public Server01SocketManager getServer01SocketManager() {
        return server01SocketManager;
    }

    public void setServer01SocketManager(Server01SocketManager server01SocketManager) {
        this.server01SocketManager = server01SocketManager;
    }

    @Override
    public void run() {
        while(true){
            System.out.println("请输入新闻内容/退出请输入exit");
            String content= Utility.readString(100);
            if(content.equals("exit")){
                System.out.println("已退出新闻小程序");
                break;
            }
            Messages messages = new Messages();
            messages.setMesType(MessageType.MESSAGE_TOALLCHAT);
            messages.setContent(content);
            messages.setSender("服务器");
            HashMap<String, Server01Socket> hm=server01SocketManager.hm;
            Iterator<String> iterator = hm.keySet().iterator();
            while(iterator.hasNext()){
                Socket socket=hm.get(iterator.next()).getSocket();
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(messages);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
