package com.example.bibloltu;

public class UserInfo {
    private static String loggedInType = "";
    private static int userID;
    public String getLoggedInType(){
        return loggedInType;
    }

    public void setLoggedInType(String loggedInType){
        this.loggedInType = loggedInType;
    }

    public int getUserID(){
        return userID;
    }
    public void setUserID(int userID){
        this.userID = userID;
    }
}
