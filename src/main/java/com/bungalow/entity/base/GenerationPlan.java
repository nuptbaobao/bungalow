package com.bungalow.entity.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by BecomeBambboo on 18/5/29.
 */

@Getter
@Setter
public class GenerationPlan {
    private String district;  //地区
    private String farmcode;  //风电场代码
    private long sTime;   //时间戳存储
    //    private String sdate; // 日期
//    private String stime; // 时间
    private Integer serialNumber; //序号
    private String dataName; //数据名称
    private Float generationValue; //数据值
}
