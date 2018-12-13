package com.eth;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("")
public class AppConfig extends ResourceConfig {

    public AppConfig() {
        packages("com.eth");
    }
}
