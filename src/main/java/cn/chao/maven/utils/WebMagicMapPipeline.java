package cn.chao.maven.utils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WebMagicMapPipeline implements Pipeline {

    public static final Map<String,String> map = new HashMap<>();

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());
        Iterator var3 = resultItems.getAll().entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var3.next();
            System.out.println((String)entry.getKey() + ":\t" + entry.getValue());
            map.put((String)entry.getKey(),entry.getValue().toString());
        }
    }
}
