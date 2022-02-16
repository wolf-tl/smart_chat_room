package com.tl.qqcommon;

import java.io.Serializable;

/**
 * @author tl
 * 表示一个用户
 */
public class User implements Serializable {
    private static final long serialVersionUID =1L;
    private String userID=null;
    private String passwd=null;

    public User(String userID, String passwd) {
        this.userID = userID;
        this.passwd = passwd;
    }
    public User(){}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
