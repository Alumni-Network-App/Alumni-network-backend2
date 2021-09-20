package com.example.alumniserver.idhelper;

import org.springframework.security.core.context.SecurityContextHolder;

public class IdHelper {

    public String getLoggedInUserId() {
        if(System.getProperty("alumni-user-id") == null)
            System.setProperty("alumni-user-id",
                    SecurityContextHolder.getContext().getAuthentication().getName());
        return System.getProperty("alumni-user-id");
    }

}
