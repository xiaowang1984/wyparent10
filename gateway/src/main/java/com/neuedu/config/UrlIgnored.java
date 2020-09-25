package com.neuedu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "secure.ignored")
@Component
public class UrlIgnored {
    private List<String> urls = new ArrayList<>();
}
