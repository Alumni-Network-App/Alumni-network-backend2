package com.example.alumniserver.idhelper;

import org.springframework.security.core.context.SecurityContextHolder;

public class IdHelper {

    public static String getLoggedInUserId() {
        return System.getProperty("alumni.my.uid");
    }

    public static void setLoggedInUserId() {
        System.setProperty("alumni.my.uid", SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
