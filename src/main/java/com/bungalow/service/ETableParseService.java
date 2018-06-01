package com.bungalow.service;

import com.bungalow.dao.GenerationPlanDao;
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
    private GenerationPlanDao generationPlanDao;

    private SimpleDateFormat datefmt = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat timefmt = new SimpleDateFormat("HHmm");

    private static Logger logger = LoggerFactory.getLogger(LocalEFileProcessService.class);

    public void parseETable(List<ETable> list) {
        if (list.size() > 0) {
            List<String> info = dealWithInfo(list);
            if (info != null) {
                dealWithList(info, list);
            }
        }
    }

    private void dealWithList(List<String> info, List<ETable> list) {

        List<GenerationPlan> gpList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        try {
            Date date = new Date(datefmt.parse(info.get(2)).getTime());
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 0);
            calendar.add(Calendar.MINUTE, 15);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (ETable eTable : list) {
            if (eTable.getTableName().split("::")[0].equalsIgnoreCase("风电发电计划")) {
                for (Object[] objs : eTable.getDatas()) {
                    if (objs.length < 3) {
                        continue;
                    }

                    GenerationPlan generationPlan = new GenerationPlan();
                    generationPlan.setDistrict(info.get(0));
                    generationPlan.setFarmcode(info.get(1));
                    java.util.Date date = calendar.getTime();
                    generationPlan.setSTime(date.getTime() / 1000);

                    generationPlan.setSerialNumber(Integer.parseInt((String) objs[0]));
                    generationPlan.setDataName((String) objs[1]);
                    generationPlan.setGenerationValue(Float.parseFloat((String) objs[2]));

                    gpList.add(generationPlan);
                    calendar.add(Calendar.MINUTE, 15);
                }
            }
        }
        generationPlanDao.save(gpList);
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
