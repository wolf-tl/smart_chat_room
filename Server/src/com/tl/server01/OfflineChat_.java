package com.tl.server01;

import com.tl.qqcommon.Messages;
import com.tl.qqcommon.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author tl
 */
public class OfflineChat_ {
    private Server01SocketManager server01SocketManager;
    private Messages messages;
    private HashMap<String, ArrayList<Messages>> hashMap = new HashMap<>();
    ArrayList<Messages>list1=new ArrayList<>();
    ArrayList<Messages>list2=new ArrayList<>();
    ArrayList<Messages>list3=new ArrayList<>();
    ArrayList<Messages>list4=new ArrayList<>();
    ArrayList<Messages>list5=new ArrayList<>();

    public Server01SocketManager getServer01SocketManager() {
        return server01SocketManager;
    }

    public void setServer01SocketManager(Server01SocketManager server01SocketManager) {
        this.server01SocketManager = server01SocketManager;
    }

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    //模拟数据库
    public void database(Messages messages) {
        if(messages.getGetter().equals("至尊宝")){
            //用数组存储多条信息
            list1.add(messages);
            //更新数组
            hashMap.put(messages.getGetter(), list1);
        }
        if(messages.getGetter().equals("紫霞仙子")){
            list2.add(messages);
            hashMap.put(messages.getGetter(), list2);
        }
        if(messages.getGetter().equals("猪八戒")){

            list3.add(messages);
            hashMap.put(messages.getGetter(), list3);
        }
        if(messages.getGetter().equals("唐僧")){

            list4.add(messages);
            hashMap.put(messages.getGetter(), list4);
        }
        if(messages.getGetter().equals("菩提老祖")){
            list5.add(messages);
            hashMap.put(messages.getGetter(), list5);
        }

    }
    //发送离线信息给目标用户
    public void send() {
        Iterator<String> iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()) {
            //当目标用户上线时，把离线信息发送出去
            String target=iterator.next();
            Server01Socket s;
            if ((s =server01SocketManager.hm.get(target)) != null) {
                //取出目标用户的socket
                Socket socket=s.getSocket();
                try {

                    //将数组中所有messages发送给目标用户
                    for(int i=0;i<hashMap.get(target).size();i++){
                        //下面这行代码需放在循环里头
                        //这里的输出流对象的个数应该与客户端的输入流对象相对应
                        //即一个流对象只发送或接收一次
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject(hashMap.get(target).get(i));
                    }
                    //移除数据库已发送的数据
                    //一定要用iterator的remove()，不然会报ConcurrentModificationException
                    //原因：迭代器的expectedModCount和HashMap中的modCount的值不一致，系统会比较这两个值，不一致时抛出异常
                    iterator.remove();
                    //移除ArrayList中的messages
                    if(target.equals("至尊宝")){
                        list1.clear();
                    }
                    if(target.equals("紫霞仙子")){
                        list2.clear();
                    }
                    if(target.equals("猪八戒")){
                        list3.clear();
                    }
                    if(target.equals("唐僧")){
                        list4.clear();
                    }
                    if(target.equals("菩提老祖")){
                        list5.clear();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


