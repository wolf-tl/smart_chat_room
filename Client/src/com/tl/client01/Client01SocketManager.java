package com.tl.client01;

import java.util.HashMap;

/**
 * @author tl
 */
public class Client01SocketManager {
   private static HashMap<String,Client01Socket> hm= new HashMap<>();

    public static HashMap<String, Client01Socket> getHm() {
        return hm;
    }

    public static void socketMap(String userID,Client01Socket client01Socket){
        hm.put(userID,client01Socket);
   }
}
