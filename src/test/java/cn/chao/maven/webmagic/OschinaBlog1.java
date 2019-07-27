package cn.chao.maven.webmagic;

import cn.chao.maven.webmagic.utils.CustomPageModelPipeline;
import lombok.Data;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import java.util.List;

/**
 *  WebMagic 的注解的部分。官网例子，仅仅请求到了 HTML ，并没有对作出处理。
 */

@Data
@TargetUrl("http://my.oschina.net/flashsword/blog/\\d+")    // url 的正则表达式的判定。
public class OschinaBlog1 implements AfterExtractor {

    public OschinaBlog1() {
    }

    @ExtractBy("//title")  // 获取标题
    private String title;

    @ExtractBy("//div[@class='content']/h3")
    private String name;

    @ExtractBy(value = "div.content",type = ExtractBy.Type.Css)     // 类型是 CSS ， div 标签下的 BlogContent 。
    private String content;

    @ExtractBy(value = "//div[@class='BlogTags']/a/text()", notNull = true)
    private List<String> tags;

    @Override
    public void afterProcess(Page page) {
        System.out.println("执行");
        String s = page.getHtml().get();
        System.out.println("name = " + name);
        System.out.println("得到结果"+s);
    }

    public static void main(String[] args) {
        OOSpider.create(Site.me(),new Class[]{OschinaBlog1.class }).addUrl("http://my.oschina.net/flashsword/blog")
//                .setScheduler(new RedisScheduler("127.0.0.1"))
                .addPipeline(new ConsolePipeline())
                .run();
        System.out.println("执行结束");
    }

}
