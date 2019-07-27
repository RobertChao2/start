package cn.chao.maven.webmagic;

import us.codecraft.webmagic.monitor.SpiderStatusMXBean;

public interface CustomSpiderStatusMXBean extends SpiderStatusMXBean {

    public String getSchedulerName();

}