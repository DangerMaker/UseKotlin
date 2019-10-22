package com.god.kotlin.user;

import com.god.kotlin.data.entity.User;

import java.util.List;

public class UserHelper {

    public static List<User> userList;

    public static User user;

    public static User getUser(){
        return user;
    }

    public static User getUserByMarket(String market){
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            if(user.getMarket().equals(market)){
                return user;
            }
        }

        return null;
    }

    public static List<User> getUserList(){
        return userList;
    }

    public static void setUserList(List<User> args){
        userList = args;
        user = userList.get(0);
    }

}
