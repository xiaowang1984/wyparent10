package com.neuedu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Beans {
    UrlIgnored ignored(){
        return new UrlIgnored();
    }
}
