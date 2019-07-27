package cn.chao.maven.utils;

import lombok.extern.log4j.Log4j2;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

@Log4j2
public class WebMagicUtils implements PageProcessor {

    private String webUrls;
    private List<String> xPathSelects;
    private String getDomain;   // 网址的域名。

    public WebMagicUtils(String webUrls,List<String> xPathSelects,String getDomain) {
        this.webUrls = webUrls;
        this.xPathSelects = xPathSelects;
        this.getDomain = getDomain;
    }

    private Site site = Site.me().setDomain(getDomain);

    @Override
    public void process(Page page) {
        log.debug("执行爬取的处理结果……");
        List<String> links = page.getHtml().links().regex("http://my\\.oschina\\.net/flashsword/blog/\\d+").all();
        page.addTargetRequests(links);  // 增加要抓取的 URL 。
        // page.putField() 来保存抽取结果
        // page.getHtml().xpath()则是按照某个规则对结果进行抽取。
//        page.putField("title", page.getHtml().xpath("//div[@class='content']/h3").toString());
//        // toString 表示转化为单个String。
//        page.putField("content", page.getHtml().$("div.content").toString());
//        // all() 则转化为一个 String 列表。
//        page.putField("tags",page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
        for (int i = 0; i < xPathSelects.size(); i++) {
            if(xPathSelects.get(i).startsWith("//")){
                page.putField("string"+i,page.getHtml().xpath(xPathSelects.get(i)).toString());
            }else {
                page.putField("string"+i, page.getHtml().$(xPathSelects.get(i)).toString());
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
