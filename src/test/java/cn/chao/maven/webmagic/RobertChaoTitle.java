package cn.chao.maven.webmagic;

import lombok.Data;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;


/**
 * 爬取自己的博客
 * 将URL中常用的字符.默认做了转义，变成了\.
 * 将"*"替换成了".*"，直接使用可表示通配符。
 */
@Data
@TargetUrl("https://blog.csdn.net/Roobert_Chao\\w+/\\w+")
@HelpUrl("https://blog.csdn.net/Roobert_Chao\\w+")
public class RobertChaoTitle {

    // @ExtractBy是一个用于抽取元素的注解，它描述了一种抽取规则。

    @ExtractBy(value = "div#id='content_views'", type = ExtractBy.Type.Css)
    private String csdnContent;

    @ExtractBy("//div[@class='article-title-box']/h1[@class='title-article']/text()")
    private String readme;

    public static void main(String[] args) {
        OOSpider.create(Site.me(),new ConsolePageModelPipeline(),new Class[]{RobertChaoTitle.class})
                .addUrl("https://blog.csdn.net/Roobert_Chao/article/details/96883357").thread(5).run();
    }
}


//@TargetUrl("https://github.com/\\w+/\\w+")
//@HelpUrl("https://github.com/\\w+")
//@Data
//public class RobertChaoTitle {
//
//    @ExtractBy(value = "//h1[@class='entry-title public']/strong/a/text()", notNull = true)
//    private String name;
//
//    @ExtractByUrl("https://github\\.com/(\\w+)/.*")
//    private String author;
//
//    @ExtractBy("//div[@id='readme']/tidyText()")
//    private String readme;
//
//    public static void main(String[] args) {
//        OOSpider.create(Site.me().setSleepTime(1000)
//                , new ConsolePageModelPipeline(), RobertChaoTitle.class)
//                .addUrl("https://github.com/code4craft").thread(5).run();
//    }
//}
