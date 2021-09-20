package com.example.alumniserver.idhelper;

import org.springframework.security.core.context.SecurityContextHolder;

public class IdHelper {

    public String getLoggedInUserId(String token) {
        if(System.getProperty("alumni-server" + token) == null)
            System.setProperty("alumni-server" + token,
                    SecurityContextHolder.getContext().getAuthentication().getName());
        return System.getProperty("alumni-server" + token);
    }

}
