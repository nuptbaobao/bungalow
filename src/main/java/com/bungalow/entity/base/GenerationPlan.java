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
    //    private String district;  //地区
//    private String farmcode;  //风电场代码
    //    private long sTime;   //时间戳存储
    private String name; //遥测表里代码
    private int sDate; // 日期
    private int time; // 时间
    private int flag;   //52
    private Float data; //预测数据
    private int rawData; //固定为0
//    private Integer serialNumber; //序号
//    private String dataName; //数据名称
//    private Float generationValue; //数据值
}
