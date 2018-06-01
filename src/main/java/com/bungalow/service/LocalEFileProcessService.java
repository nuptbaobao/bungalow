package com.bungalow.service;

import com.bungalow.entity.efile.impl.D5kEfileParser;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import com.bungalow.entity.edom.io.EdomSAXContentHandler;
import com.bungalow.entity.config.Config;
import com.bungalow.entity.efile.ETable;

import javax.annotation.PostConstruct;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时读取E文件并进行解析
 * <p>
 * Created by BecomeBamboo on 2018/5/29.
 */


@Service
@EnableScheduling
public class LocalEFileProcessService {
    private static Logger logger = LoggerFactory.getLogger(LocalEFileProcessService.class);

    @Autowired
    private Config config;

    @Autowired
    private ETableParseService eTableParseService;

    private D5kEfileParser parser = new D5kEfileParser();


    private final ScheduledExecutorService seService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void init() {
        // 定时启动任务
        seService.scheduleAtFixedRate(new process(), 0, config.getInterval(), TimeUnit.SECONDS);
        logger.info("初始化开始");
    }

    class process implements Runnable {
        public void run() {
            logger.info("任务开始");
            long a = System.currentTimeMillis();

            String filePath = config.getPath();
            List<String> files = getFiles(filePath);
            Collections.sort(files);
            processEFiles(filePath, files);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            logger.info("所有E文件处理耗时: {}秒 当前时间: {}", (System.currentTimeMillis() - a) / 1000, sft.format(calendar.getTime()));
        }
    }

    /**
     * 获取E文件数量
     *
     * @return 文件名称列表
     */
    public List<String> getFiles(String path) {
        List<String> files = Lists.newArrayList();

        File f = new File(path);
        if (!f.exists()) {
            logger.info("没有E文件夹");
            return null;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isFile()) {
                files.add(fs.getName());
            } else {
                continue;
            }
        }
        return files;
    }

    /**
     * E文件解析及入库
     *
     * @param filesPath E文件绝对路径
     * @param files     E文件列表
     */
    public void processEFiles(String filesPath, List<String> files) {
        for (int i = 0; i < files.size(); i++) {
            try {
                List<ETable> list = parser.parseFile(filesPath + "/" + files.get(i));
                eTableParseService.parseETable(list);
                File file = new File(filesPath + "/" + files.get(i));
//                file.delete();
                logger.info("E文件解析成功");
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("E文件解析失败");
            }
        }
    }


}
