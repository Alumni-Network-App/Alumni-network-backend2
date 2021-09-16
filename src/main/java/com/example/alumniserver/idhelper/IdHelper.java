package com.example.alumniserver.idhelper;

import org.springframework.security.core.context.SecurityContextHolder;

public class IdHelper {

    private static String id;

    public static String getLoggedInUserId() {
        return id;
    }

    public static void setLoggedInUserId() {
        id = SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
