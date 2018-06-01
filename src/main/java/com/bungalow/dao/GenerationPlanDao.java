package com.bungalow.dao;

import com.bungalow.entity.base.GenerationPlan;
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

    public int[] save(final List<GenerationPlan> planList) {

        final String sql = "MERGE INTO TB_GENERATIONPLAN T1 " +
                "USING (SELECT ? AS DISTRICT, ? AS FARMCODE, ? AS STIME, ? AS SERIALNUMBER, ? AS DATANAME, ? AS GENERATIONVALUE FROM dual) T2 " +
                "ON (T1.DISTRICT=T2.DISTRICT AND T1.FARMCODE=T2.FARMCODE AND T1.STIME=T2.STIME AND T1.SERIALNUMBER=T2.SERIALNUMBER AND T1.DATANAME=T2.DATANAME) " +
                "WHEN MATCHED THEN " +
                "UPDATE SET T1.GENERATIONVALUE = T2.GENERATIONVALUE " +
                "WHEN NOT MATCHED THEN " +
                "INSERT (DISTRICT,FARMCODE,STIME,SERIALNUMBER,DATANAME,GENERATIONVALUE) VALUES(T2.DISTRICT,T2.FARMCODE,T2.STIME,T2.SERIALNUMBER,T2.DATANAME,T2.GENERATIONVALUE)";

        try {
            int[] updateCounts = jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setString(1, planList.get(i).getDistrict());
                            ps.setString(2, planList.get(i).getFarmcode());
                            ps.setLong(3, planList.get(i).getSTime());
                            ps.setInt(4, planList.get(i).getSerialNumber());
                            ps.setString(5, planList.get(i).getDataName());
                            ps.setFloat(6, planList.get(i).getGenerationValue());
                        }

                        @Override
                        public int getBatchSize() {
                            return planList.size();
                        }
                    });
            jdbcTemplate.update("COMMIT");
            return updateCounts;

        } catch (Exception e) {
            logger.info("TB_GENERATIONPLAN 入库错误");
        }
        return null;

    }


}
