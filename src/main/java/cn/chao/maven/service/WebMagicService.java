package cn.chao.maven.service;

import java.util.Map;

public interface WebMagicService {

    public Map<String,String> getOneFormResult(String webUrl,String xPathSelect);
}
