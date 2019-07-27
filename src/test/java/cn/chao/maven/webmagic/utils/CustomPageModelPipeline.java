package cn.chao.maven.webmagic.utils;

import cn.chao.maven.webmagic.OschinaBlog1;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

public class CustomPageModelPipeline implements PageModelPipeline<OschinaBlog1> {

    public CustomPageModelPipeline() {
    }

    @Override
    public void process(OschinaBlog1 oschinaBlog1, Task task) {
        System.out.println("自定义处理结果");
        System.out.println("获取的博客内容："+oschinaBlog1);
        System.out.println("Task = "+task);
    }
}
