package cn.chao.maven.service.impl;

import cn.chao.maven.service.WebMagicService;
import cn.chao.maven.utils.WebMagicMapPipeline;
import cn.chao.maven.utils.WebMagicUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Spider;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
@Service
@Log4j2
public class WebMagicServiceImpl implements WebMagicService {

    private static final String regx = "(http|https)://(www.)?(\\w+(\\.)?)+";
    private List<String> weburl;
    private List<String> strings1;
    private String domain;

    @Override
    public Map<String, String> getOneFormResult(String webUrl, String xPathSelect) {
        log.debug("正式开始操作爬取内容……");
        String[] split = webUrl.split(",");
        String[] split1 = xPathSelect.split(",");
//        weburl = webUrl;    // 这里可以设置成一个数组转集合的形式表现 URL 的内容。
        weburl = Arrays.asList(split);
        strings1 = Arrays.asList(split1);
        // 获取网址的域名 Domain
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(weburl.get(0));
        boolean b = matcher.find();
        if(b){
            domain = matcher.group();
        }
        getMapPopeline();
        return WebMagicMapPipeline.map;
    }

    private void getMapPopeline() {
        // http://my.oschina.net/flashsword/blog
        // //div[@class='content']/h3

        // 查看爬取的内容的 url 的大小多少内容
        log.debug("获取前台输入的\t"+weburl.size()+"\t个 WEB 地址……");
        for (int i=0 ; i < weburl.size();i++){
            log.debug("正在爬取第\t"+(i+1)+"\t个WEB地址的内容……");
            System.out.println(weburl.get(i)+"、"+strings1.get(i)+"、"+domain);
            WebMagicUtils webMagicUtils = new WebMagicUtils(weburl.get(i),strings1,domain);
            Spider spider = Spider.create(webMagicUtils).addUrl(weburl.get(i).toString())
                    .addPipeline(new WebMagicMapPipeline()).thread(5);
            spider.run();
        }

    }
}
