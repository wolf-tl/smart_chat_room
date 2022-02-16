package com.tl.server01;

import com.tl.qqcommon.MessageType;
import com.tl.qqcommon.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

/**
 * @author tl
 * 虽然socket是私有的，但是在该类中依然能够访问其他线程的socket：
 * Socket targetsocket=server01SocketManager.getsocketThread(messages.getGetter()).socket;
 */
public class Server01Socket extends Thread{
    private Socket socket;
    private String userID;
    private Server01SocketManager server01SocketManager;
    private OfflineChat_ offlineChat_;
    public Server01Socket(){

    }
    public Server01Socket(Socket socket){
        this.socket=socket;
    }
    public Server01Socket(String userID,Socket socket){
        this.userID=userID;
        this.socket=socket;
    }
    public Server01Socket(Socket socket,Server01SocketManager server01SocketManager){
        this.socket=socket;
        this.server01SocketManager=server01SocketManager;
    }
    public Socket getSocket(){
        return this.socket;
    }

    public OfflineChat_ getOfflineChat_() {
        return offlineChat_;
    }

    public void setOfflineChat_(OfflineChat_ offlineChat_) {
        this.offlineChat_ = offlineChat_;
    }

    @Override
    public void run() {
        while(true){
            try {
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                Messages messages=(Messages) objectInputStream.readObject();//向下转型
                if(messages.getMesType().equals(MessageType.MESSAGE_GETUSERLIST)){
                    Messages messages1 = new Messages();
                    messages1.setGetter(messages.getSender());//客户端的身份转变
                    messages1.setMesType(MessageType.MESSAGE_RET_USERLIST);
                    //获取用户列表
                    String users=server01SocketManager.retuserList();
                    messages1.setContent(users);//返回用户列表
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(messages1);
                }
                //私聊
                if(messages.getMesType().equals(MessageType.MESSAGE_PRIVATECHAT)){
                    Socket targetsocket=server01SocketManager.getsocketThread(messages.getGetter()).getSocket();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(targetsocket.getOutputStream());
                    objectOutputStream.writeObject(messages);
                }
                //群聊
                if(messages.getMesType().equals(MessageType.MESSAGE_TOALLCHAT)){
                    Iterator<String> iterator = server01SocketManager.hm.keySet().iterator();
                    while(iterator.hasNext()){
                        //取出所有socket对象
                        Socket targetsocket=server01SocketManager.hm.get(iterator.next()).getSocket();
                        //排除自己
                        if(!targetsocket.equals(socket)){
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(targetsocket.getOutputStream());
                            objectOutputStream.writeObject(messages);
                        }
                    }
                }
                //在线发送文件
                if(messages.getMesType().equals(MessageType.MESSAGE_SENDFILES)){
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream
                            (server01SocketManager.hm.get(messages.getGetter()).getSocket().getOutputStream());
                    objectOutputStream.writeObject(messages);
                }
                //储存离线信息
                if(messages.getMesType().equals(MessageType.MESSAGE_OFFLINECHAT)){
                    offlineChat_.database(messages);
                }
                //接收到客户端的退出指令，移除socket线程，并关闭socket
                if(messages.getMesType().equals(MessageType.MESSAGE_EXIT)){
                    server01SocketManager.removeSocketThread(messages.getSender());
//                    server01SocketManager.getsocket(messages.getSender()).socket.close();
                    socket.close();
                    break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
