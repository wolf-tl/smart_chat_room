package com.tl.server01;

import com.tl.qqcommon.MessageType;
import com.tl.qqcommon.Messages;
import com.tl.qqcommon.User;
import jdk.nashorn.internal.ir.CallNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @author tl
 * 一定要好好学集合啊！！！
 * 注意：关流动作必须在程序最后执行，当两端socket保持通信时关流会抛出"socket is closed"异常
 * 当用户登录失败时，无法提示用户哪个地方输入错误；
 * 离线发送信息时，当目标用户在线时无法发送；
 */
public class Server01 {
    private ServerSocket serverSocket;
    private Server01Socket server01Socket;
    private Server01SocketManager server01SocketManager;
    private Messages messages;
    //创建一个HashMap用来模拟数据库
    private static HashMap<String,User> userData=new HashMap<>();
    //开辟一个静态代码块
    static {
        userData.put("至尊宝",new User("至尊宝","123456"));
        userData.put("菩提老祖",new User("菩提老祖","666666"));
        userData.put("唐僧",new User("唐僧","233333"));
        userData.put("猪八戒",new User("猪八戒","555555"));
        userData.put("紫霞仙子",new User("紫霞仙子","456789"));
    }

    public static HashMap<String, User> getUserData() {
        return userData;
    }

    public boolean checkby(User user) {

        User user1=userData.get(user.getUserID());
        //过关斩将
        if(user1==null){
            System.out.println("用户不存在");
            return false;
            //可以提示用户不存在
        }
        if(!user1.getPasswd().equals(user.getPasswd())){
            System.out.println("密码错误");
            return false;//提示密码错误
        }
        return true;
    }

    public void ser() {
        try {
            serverSocket = new ServerSocket(9999);
            server01SocketManager = new Server01SocketManager();//创建线程集合，集中管理线程
            SendNews sendNews = new SendNews();
            sendNews.setServer01SocketManager(server01SocketManager);
            sendNews.start();
            OfflineChat_ offlineChat_ = new OfflineChat_();
            offlineChat_.setServer01SocketManager(server01SocketManager);
            while (true) {
            //连接新的socket
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            User user = (User) objectInputStream.readObject();//接收客户端发送的用户登录信息

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            //判断用户名密码是否正确
            if (checkby(user)) {
                System.out.println("已连接");
                messages = new Messages(user.getUserID(), "如来佛祖：", "欢迎"+user.getUserID()+"来到qq世界", "2022", MessageType.MESSAGE_SUCCED);
                objectOutputStream.writeObject(messages);
                //创建线程保持socket与客户端通信
                server01Socket = new Server01Socket(socket,server01SocketManager);
                server01Socket.setOfflineChat_(offlineChat_);
                server01Socket.start();
                server01SocketManager.socketmap(user.getUserID(), server01Socket);
                //在此处给目标用户发送离线信息，如果对方已经在线则无法执行该行代码
                //如果实现对方在线仍能发送离线信息，可以创建一个线程，把该方法放在线程中不停循环
                //该方法会出现并发错误并跳出while循环导致服务器无法连接新socket
                //出错的原因：iterator修改次数异常
                offlineChat_.send();
            } else {

                messages = new Messages();
                messages.setMesType(MessageType.MESSAGE_FAIL);
                objectOutputStream.writeObject(messages);
                socket.close();
            }
        }

    }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
