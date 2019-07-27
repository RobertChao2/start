package cn.chao.maven.quartz;

import java.util.Date;

import cn.chao.maven.entity.LogEntity;
import cn.chao.maven.service.LogService;
import cn.chao.maven.utils.DateUtil;
import cn.chao.maven.utils.GlobalUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 清除系统日志任务
 * @author Mr Du
 *
 */

@Component
@Log4j2
public class MethodLogQuartz {

    private static Logger LOGGER = Logger.getLogger(MethodLogQuartz.class);
    private static final String LOGDAYS = "log.days";

    @Autowired
    private LogService logServiceImpl;
    /** 
     * cron表达式：* * * * * *（共6位，使用空格隔开，或者是 7位，年（可以省略））
     * cron表达式：*(秒0-59) *(分 钟0-59) *(小时0-23) *(日期1-31) *(月份1-12或是JAN-DEC) *(星期1-7或是SUN-SAT)
     * L W C /
     */ 
    @Scheduled(cron="0 0 0 * * ?")  // 每天 0点执行
    public void clearLog() {
        int logDays = Integer.valueOf(GlobalUtil.getValue(LOGDAYS));
        Date date = DateUtil.getDate(DateUtil.getDate(), -logDays);
        int count = logServiceImpl.delLogsByDate(date);
        String date1 = DateUtil.getStringDate(date, DateUtil.DateFormat1);
		LOGGER.info("日志定时删除任务，删除日志数量:" + count + ", createDate < " + date1);
        LogEntity log = new LogEntity();
        log.setIp("主机主动发起");
        log.setOperation("定时删除日志："+count+"条");
        log.setCreateTime(date);
        log.setUsername("系统自动任务");
        log.setMethod("自动清除日志任务");
        log.setParams("无参数");
		logServiceImpl.insLog(log);
    }

    @Scheduled(cron = "0 0 * * * ?")    // 每小时执行一次
    public void getTask1(){
        log.debug("每小时执行一次的定时任务。");
    }

    @Scheduled(cron = "0 * * * * ?")    // 每分钟执行一次
    public void getTask2(){
        log.debug("每分钟执行一次的定时任务。");
    }
}