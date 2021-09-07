package com.example.alumniserver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AlumniServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AlumniServerApplication.class)
                .profiles("heroku", "user")
                .run(args);
    }

}
