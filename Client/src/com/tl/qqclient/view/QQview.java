package com.tl.qqclient.view;

import com.tl.client01.Client01;
import com.tl.qqcommon.User;
import com.tl.utils.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tl
 * 客户端的菜单界面
 * 目前系统允许两个用户名一样的现象，后期需要优化
 */
public class QQview {
    private boolean loop=true;
    private String key="";
    private  Client01 client01;
    private User user;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new QQview().mainMenu();
    }

    private void mainMenu() throws IOException, ClassNotFoundException {
        while(loop){
            System.out.println("======欢迎登录网络通讯系统======");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.println("请谨慎选择！");
            key= Utility.readString(1);
            switch(key){
                case "1":
                    System.out.println("进入登录界面");
                    System.out.print("请输入用户名：");
                    String userID=Utility.readString(50);
                    System.out.print("请输入密码：");
                    String pwd=Utility.readString(50);
                    user=new User();
                    user.setUserID(userID);
                    user.setPasswd(pwd);
                    //调用client01.clientserver()方法，判断是否登录成功
                    client01 = new Client01();
                    if(client01.clientserver(user)){
                        System.out.println("======登录成功！======");
                        System.out.println(client01.messages.getGetter()+": "+client01.messages.getContent()+" "+client01.messages.getSendTime());
                        while(loop){
                            System.out.println("======网络通讯系统二级界面======");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发信息");
                            System.out.println("\t\t 3 私聊信息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 5 发送离线信息");
                            System.out.println("\t\t 6 发送离线文件");
                            System.out.println("\t\t 9 退出系统");
                            key=Utility.readString(1);
                            switch(key){
                                case "1":
                                    System.out.println("显示在线用户列表");
                                    client01.getusersList(user);
                                    break;
                                case "2":
                                    System.out.println("群发信息");
                                    System.out.println("请输入群发的内容");
                                    String cont=Utility.readString(100);
                                    String dat=new Date().toString();
                                    client01.toallChat(user,cont,dat);
                                    break;
                                case "3":
                                    System.out.println("私聊信息");
                                    System.out.print("请选择私聊对象：");
                                    String target= Utility.readString(50);
                                    System.out.print("请输入私聊内容：");
                                    String content=Utility.readString(100);
                                    String data=new Date().toString();
                                    //写一个发送私聊信息的方法
                                    client01.privateChat(user,target,content,data);
                                    break;
                                case "4":
                                    System.out.println("在线发送文件");
                                    System.out.print("请输入目标用户：");
                                    String taruser=Utility.readString(50);
                                    System.out.print("请输入文件的位置：");
                                    String src=Utility.readString(50);
                                    System.out.print("请输入发送给对方文件的位置：");
                                    String tar=Utility.readString(50);
                                    String data_=new Date().toString();
                                    client01.sendFiles(user,taruser,src,tar,data_);
                                    break;
                                case "5":
                                    System.out.println("发送离线信息");
                                    System.out.print("请输入目标用户：");
                                    String direct=Utility.readString(50);
                                    System.out.print("请输入发送的内容：");
                                    String con=Utility.readString(100);
                                    String date=new Date().toString();
                                    client01.offlineChat(user,con,direct,date);
                                    break;
                                case "6":
                                    break;
                                case "9":
                                    loop=false;
                                    System.out.println("已退出系统");
                                    //与服务端通信，让服务端关闭socket
                                    client01.shutdown(user);
                                    //客户端关闭进程
                                    System.exit(0);
                                    break;
                            }
                        }
                    }

                    break;
                case "9":
                    loop=false;
                    System.out.println("已退出系统");
                    break;
            }
        }
    }
}
