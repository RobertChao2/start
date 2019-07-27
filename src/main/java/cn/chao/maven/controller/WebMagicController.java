package cn.chao.maven.controller;

import cn.chao.maven.service.WebMagicService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
public class WebMagicController {

    @Autowired
    private WebMagicService webMagicService;

    @ResponseBody
    @RequestMapping(value = "webmagic/getOneForm",method = RequestMethod.POST)
    public Map<String, String> getOneForm(String webUrl,String xpathSelect){
        log.debug("正在执行单页表单爬虫提交……");
        System.out.println(webUrl);
        System.out.println(xpathSelect);
        Map<String, String> result = webMagicService.getOneFormResult(webUrl, xpathSelect);
        if(result.isEmpty()){
            log.debug("爬取内容失败，正在返回……");
            Map<String,String> map = new HashMap<>();
            map.put("400","请求内容为空，请稍后再试");
            return map;
        }else {
            log.debug("爬取内容成功，正在返回……");
            return result;
        }
    }
}
