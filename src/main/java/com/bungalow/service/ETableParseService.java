package com.bungalow.service;

import com.bungalow.dao.GenerationPlanDao;
import com.bungalow.entity.config.Config;
import com.bungalow.entity.efile.ETable;
import com.bungalow.service.LocalEFileProcessService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bungalow.entity.base.GenerationPlan;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 处理ETable并入库
 * Created by ChengXi on 2016/10/20.
 */
@Service
@Slf4j
public class ETableParseService {
    @Autowired
    private Config config;

    @Autowired
    private GenerationPlanDao generationPlanDao;

    private SimpleDateFormat datefmt = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat hourfmt = new SimpleDateFormat("HH");
    private SimpleDateFormat minutefmt = new SimpleDateFormat("mm");


    private static Logger logger = LoggerFactory.getLogger(LocalEFileProcessService.class);

    public Boolean parseETable(List<ETable> list) {
        if (list.size() > 0) {
            List<String> info = dealWithInfo(list);
            return dealWithList(info, list);
        }
        logger.warn("E文件为空，不予处理");
        return false;
    }

    private Boolean dealWithList(List<String> info, List<ETable> list) {

        List<GenerationPlan> gpList = new ArrayList<>();

        Calendar eFileCalendar = Calendar.getInstance();
        eFileCalendar.set(Calendar.HOUR_OF_DAY, 0);
        eFileCalendar.set(Calendar.MINUTE, 0);


        try {
            Date eFileDate = new Date(datefmt.parse(info.get(2)).getTime());
            eFileCalendar.setTime(eFileDate);
            eFileCalendar.add(Calendar.DAY_OF_MONTH, 0);
            eFileCalendar.add(Calendar.MINUTE, 15);

            java.util.Date utilDate = new java.util.Date();
            java.sql.Date nowDate = new java.sql.Date(utilDate.getTime());

            Date t1 = java.sql.Date.valueOf(eFileDate.toString());
            Date t2 = java.sql.Date.valueOf(nowDate.toString());

            //日期比较
            if (t1.before(t2)) {
                logger.info("当前E文件日期超前，不予处理");
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        for (ETable eTable : list) {

            String tableName = "YC" + eTable.getDate().split("-")[0] + eTable.getDate().split("-")[1];

            if (eTable.getTableName().split("::")[0].equalsIgnoreCase("风电发电计划")) {

                //E文件数据条目和配置文件一致才入库处理
                if (eTable.getDatas().size() == config.getDataNumber()) {
                    for (Object[] objs : eTable.getDatas()) {
                        if (objs.length < 3) {
                            continue;
                        }

                        GenerationPlan generationPlan = new GenerationPlan();
                        generationPlan.setName(config.getPowerCode());

                        //设置日期
                        java.util.Date date = eFileCalendar.getTime();
                        String sdf = datefmt.format(date);
                        Calendar cal = Calendar.getInstance();
                        cal.set(1970, 0, 1, 0, 0, 0);
                        long intervalMilli = date.getTime() - cal.getTimeInMillis();
                        int intervalDate = (int) (intervalMilli / (24 * 60 * 60 * 1000));
                        generationPlan.setSDate(intervalDate);

                        //设置时间
                        int hour = Integer.parseInt(hourfmt.format(date));
                        int minute = Integer.parseInt(minutefmt.format(date));
                        int time = hour * 60 + minute;
                        if (hour == 0 && minute == 0) {
                            time = 1440;
                        }

                        generationPlan.setTime(time);

                        generationPlan.setFlag(51);
                        generationPlan.setData(Float.parseFloat((String) objs[2]));
                        generationPlan.setRawData(0);
                        gpList.add(generationPlan);
                        eFileCalendar.add(Calendar.MINUTE, 15);
                    }
                } else {
                    logger.warn("E文件数据数量为{}，少于配置文件里配置数量{}，不予入库处理", eTable.getDatas().size(), config.getDataNumber());
                    return false;
                }
            }

            //双数据源
            if (config.getDoubleDataSource()) {
                if (generationPlanDao.dataSource1Select(gpList, tableName) != 0) {
                    generationPlanDao.dataSource1Delete(gpList, tableName);
                }
                generationPlanDao.dataSource1Save(gpList, tableName);
                if (generationPlanDao.dataSource2Select(gpList, tableName) != 0) {
                    generationPlanDao.dataSource2Delete(gpList, tableName);
                }
                generationPlanDao.dataSource2Save(gpList, tableName);
                return true;

            }
            //单一数据源
            if (!config.getDoubleDataSource()) {
                if (generationPlanDao.dataSource1Select(gpList, tableName) != 0) {
                    generationPlanDao.dataSource1Delete(gpList, tableName);
                }
                generationPlanDao.dataSource1Save(gpList, tableName);
                return true;

            }
        }
        return false;
    }


    private List<String> dealWithInfo(List<ETable> list) {
        String district = list.get(0).getTableName().split("::")[1].split("\\.")[0];
        String farmCode = list.get(0).getTableName().split("::")[1].split("\\.")[1];
        String date = list.get(0).getDate().split("-")[0] + list.get(0).getDate().split("-")[1] + list.get(0).getDate().split("-")[2];
        String time = "0000";

        List<String> info = new ArrayList<>();
        info.add(district);
        info.add(farmCode);
        info.add(date);
        info.add(time);

        return info;
    }

}
