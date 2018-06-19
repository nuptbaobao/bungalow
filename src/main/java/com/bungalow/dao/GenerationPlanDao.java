package com.bungalow.dao;

import com.bungalow.entity.base.GenerationPlan;
import com.bungalow.entity.config.Config;
import com.bungalow.service.LocalEFileProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by BecomeBamboo on 2018/5/30.
 */
@Repository("generationPlanDao")
public class GenerationPlanDao {
    private static Logger logger = LoggerFactory.getLogger(LocalEFileProcessService.class);


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int[] save(final List<GenerationPlan> planList, String tableName) {

//        final String sql = "MERGE INTO TB_GENERATIONPLAN T1 " +
//                "USING (SELECT ? AS DISTRICT, ? AS FARMCODE, ? AS STIME, ? AS SERIALNUMBER, ? AS DATANAME, ? AS GENERATIONVALUE FROM dual) T2 " +
//                "ON (T1.DISTRICT=T2.DISTRICT AND T1.FARMCODE=T2.FARMCODE AND T1.STIME=T2.STIME AND T1.SERIALNUMBER=T2.SERIALNUMBER AND T1.DATANAME=T2.DATANAME) " +
//                "WHEN MATCHED THEN " +
//                "UPDATE SET T1.GENERATIONVALUE = T2.GENERATIONVALUE " +
//                "WHEN NOT MATCHED THEN " +
//                "INSERT (DISTRICT,FARMCODE,STIME,SERIALNUMBER,DATANAME,GENERATIONVALUE) VALUES(T2.DISTRICT,T2.FARMCODE,T2.STIME,T2.SERIALNUMBER,T2.DATANAME,T2.GENERATIONVALUE)";
//merge语句
//        final String sql = "MERGE INTO " + tableName + " T1 " +
//        "USING (SELECT ? AS NAME, ? AS SDATE, ? AS TIME, ? AS FLAG, ? AS DATA, ? AS RAWDATA FROM dual) T2 " +
//                "ON (T1.NAME=T2.NAME AND T1.SDATE=T2.SDATE AND T1.TIME=T2.TIME AND T1.FLAG=T2.FLAG AND T1.RAWDATA=T2.RAWDATA) " +
//                "WHEN MATCHED THEN " +
//                "UPDATE SET T1.DATA = T2.DATA " +
//                "WHEN NOT MATCHED THEN " +
//                "INSERT (NAME,SDATE,TIME,FLAG,DATA,RAWDATA) VALUES(T2.NAME,T2.SDATE,T2.TIME,T2.FLAG,T2.DATA,T2.RAWDATA)";

        //直接插入
        final String sql = "INSERT INTO " + tableName +
                " (NAME,SDATE,TIME,FLAG,DATA,RAWDATA) VALUES(?,?,?,?,?,?)";


        try {
            int[] updateCounts = jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setString(1, planList.get(i).getName());
                            ps.setInt(2, planList.get(i).getSDate());
                            ps.setInt(3, planList.get(i).getTime());
                            ps.setInt(4, planList.get(i).getFlag());
                            ps.setFloat(5, planList.get(i).getData());
                            ps.setFloat(6, planList.get(i).getRawData());
                        }

                        @Override
                        public int getBatchSize() {
                            return planList.size();
                        }
                    });
            logger.info(tableName + " 写入预测数据");
            jdbcTemplate.update("COMMIT");
            logger.info(tableName + " 预测数据入库成功");
            return updateCounts;

        } catch (Exception e) {
            logger.info(tableName + " 预测数据入库错误");
            logger.info(e.getMessage());
        }
        return null;

    }

    public int[] delete(final List<GenerationPlan> planList, String tableName) {

        final String sql = "DELETE FROM " + tableName + " WHERE NAME=? AND SDATE=? ";


        try {
            int[] updateCounts = jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setString(1, planList.get(i).getName());
                            ps.setInt(2, planList.get(i).getSDate());
                        }

                        @Override
                        public int getBatchSize() {
                            return planList.size();
                        }
                    });
            logger.info(tableName + " 开始删除原始预测数据");
            jdbcTemplate.update("COMMIT");
            logger.info(tableName + " 删除原始预测数据成功");
            return updateCounts;

        } catch (Exception e) {
            logger.info(tableName + " 删除原始预测数据错误");
            logger.info(e.getMessage());
        }
        return null;

    }

    public int select(final List<GenerationPlan> planList, String tableName) {

        int date = planList.get(0).getSDate();
        String name = "\'" + planList.get(0).getName() + "\'";
        final String sql = "SELECT COUNT (*) FROM " + tableName + " WHERE NAME=" + name + " AND SDATE= " + date;


        try {
            int number = jdbcTemplate.queryForObject(sql, Integer.class);
            logger.info("原先预测数据有{}条", number);
            return number;
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return 0;

    }


}
