package cn.chao.maven.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.monitor.SpiderStatusMXBean;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import javax.management.JMException;
import java.util.List;

public class WebMagicTest implements PageProcessor {

    private Site site = Site.me().setDomain("http://my.oschina.net");

    @Override
    public void process(Page page) {
        System.out.println("执行结果处理：");
        List<String> links = page.getHtml().links().regex("http://my\\.oschina\\.net/flashsword/blog/\\d+").all();
        System.out.println(links);
        page.addTargetRequests(links);  // 增加要抓取的 URL 。
        // page.putField() 来保存抽取结果
        // page.getHtml().xpath()则是按照某个规则对结果进行抽取。
        page.putField("title", page.getHtml().xpath("//div[@class='content']/h3").toString());
//        page.putField("title", page.getHtml().xpath("//div[@class='BlogEntity']/div[@class='BlogTitle']/h1").toString());
        // toString 表示转化为单个String。
        page.putField("content", page.getHtml().$("div.content").toString());
        // all() 则转化为一个 String 列表。
        page.putField("tags",page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws JMException {
        SpiderMonitor spiderMonitor = new SpiderMonitor(){
            @Override
            protected SpiderStatusMXBean getSpiderStatusMBean(Spider spider, MonitorSpiderListener monitorSpiderListener) {
                return new CustomSpiderStatus(spider, monitorSpiderListener);
            }
        };

        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
/*        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy("101.101.101.101",8888),new Proxy("102.102.102.102",8888)));*/

        Spider spider = Spider.create(new WebMagicTest()).addUrl("http://my.oschina.net/flashsword/blog")
                .addPipeline(new ConsolePipeline()).thread(2);
/*        spider.setDownloader(httpClientDownloader);*/

        spiderMonitor.register(spider);
        spider.start();
    }
}
