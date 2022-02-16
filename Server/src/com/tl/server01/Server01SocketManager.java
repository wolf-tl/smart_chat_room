package com.tl.server01;

import com.tl.qqcommon.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author tl
 */
public class Server01SocketManager {


    public HashMap<String,Server01Socket> hm=new HashMap<>();
    //将用户的socket线程放入集合中
    public void socketmap(String userID,Server01Socket server01Socket){
        hm.put(userID, server01Socket);
    }
    //通过用户名得到相应的socket线程对象
    public Server01Socket getsocketThread(String userID){
        return hm.get(userID);
    }
    //返回用户列表
    //当有两个用户名一样的客户端登录，则只返回一个客户
    public String retuserList(){
        Iterator<String> iterator = hm.keySet().iterator();
        String users="";
        while(iterator.hasNext()){
            users+=iterator.next()+" ";
        }
        return users;
    }
    //移除socket进程
    public void removeSocketThread(String userID){
        hm.remove(userID);
    }
}
