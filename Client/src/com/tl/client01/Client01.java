package com.tl.client01;

import com.tl.qqclient.view.QQview;
import com.tl.qqcommon.MessageType;
import com.tl.qqcommon.Messages;
import com.tl.qqcommon.User;


import javax.jws.soap.SOAPBinding;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;



/**
 * @author tl
 * 注意：关流动作必须在程序最后执行，当两端socket保持通信时关流会抛出"socket is closed"异常
 * 可能有多个socket对象
 */
public class Client01 {
    Socket socket;
    Client01Socket client01Socket;
    public Messages messages;
    FileInputStream fileInputStream;

    public Socket getSocket() {
        return socket;
    }

    public boolean clientserver(User user) throws IOException, ClassNotFoundException {
        //这里可以new多个？
        socket=new Socket(InetAddress.getLocalHost(),9999);
        String userID=user.getUserID();
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        //将用户的登录信息发送给服务端
        objectOutputStream.writeObject(user);
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        //接收服务端发过来的数据
        messages = (Messages) objectInputStream.readObject();
        //关流
//        objectInputStream.close();
//        objectOutputStream.close();
        //判断是否登录成功
        if(messages.getMesType().equals(MessageType.MESSAGE_SUCCED)){
            //创建一个线程用于实时接收服务端的数据
            client01Socket = new Client01Socket(socket);
            client01Socket.start();//启动线程
            Client01SocketManager.getHm().put(userID,client01Socket);
            return true;
        }
        else {
            socket.close();
            return false;

        }
    }
    //创建一个请求获取用户列表信息的方法
    public void getusersList(User user){
        try {
            Messages messages = new Messages();
            messages.setMesType(MessageType.MESSAGE_GETUSERLIST);
            messages.setSender(user.getUserID());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //发送私聊的方法
    public void privateChat(User user,String target,String content,String data){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            Messages messages = new Messages();
            messages.setSender(user.getUserID());
            messages.setMesType(MessageType.MESSAGE_PRIVATECHAT);
            messages.setGetter(target);
            messages.setContent(content);
            messages.setSendTime(data);
            objectOutputStream.writeObject(messages);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //群聊方法
    public void toallChat(User user,String cont,String dat){
        Messages messages = new Messages();
        messages.setContent(cont);
        messages.setMesType(MessageType.MESSAGE_TOALLCHAT);
        messages.setSender(user.getUserID());
        messages.setSendTime(dat);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //在线发送文件
    public void sendFiles(User user,String taruser,String src,String tar,String data_){
        Messages messages = new Messages();
        messages.setMesType(MessageType.MESSAGE_SENDFILES);
        messages.setSendTime(data_);
        messages.setSender(user.getUserID());
        messages.setGetter(taruser);
        messages.setSrc(src);
        messages.setTar(tar);

        try {
            fileInputStream = new FileInputStream(src);
            byte[] files = new byte[(int)new File(src).length()];
            fileInputStream.read(files);
            messages.setFile(files);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(messages);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    //发送离线信息
    public void offlineChat(User user,String con,String userID,String date){
        Messages messages = new Messages();
        messages.setSender(user.getUserID());
        messages.setContent(con);
        messages.setMesType(MessageType.MESSAGE_OFFLINECHAT);
        messages.setGetter(userID);
        messages.setSendTime(date);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //退出系统 方法
    public void shutdown(User user){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            Messages messages=new Messages();
            //发送退出系统指令给服务端
            messages.setMesType(MessageType.MESSAGE_EXIT);
            //告诉服务端自己属于哪个客户端
            messages.setSender(user.getUserID());
            objectOutputStream.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
