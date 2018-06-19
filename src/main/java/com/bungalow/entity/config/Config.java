package com.bungalow.entity.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by BecomeBambboo on 18/5/29.
 */
@Component
@ConfigurationProperties(prefix = "bungalow")
@Getter
@Setter
public class Config {
    //    private String url;
//    private String userName;
//    private String passWord;
//    private String driverName;
    private String path;
    private Integer interval;
    private String powerCode;
}
