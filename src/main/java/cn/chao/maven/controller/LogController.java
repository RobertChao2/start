package cn.chao.maven.controller;

import cn.chao.maven.service.LogService;
import cn.chao.maven.utils.ResultUtil;
import cn.chao.maven.vo.UserSearch;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("log/")
@Log4j2
public class LogController {
	
	@Autowired
	private LogService logServiceImpl;
	
	@RequestMapping("logList")
	@RequiresPermissions("log:log:list")
	public String logList(){
		return "page/log/logList";
	}
	
	@RequestMapping("getLogList")
	@RequiresPermissions("log:log:list")
	@ResponseBody
	public ResultUtil getLogList(Integer page, Integer limit, UserSearch search){
		log.debug("分页的内容：page = " +page +"，limit = "+limit);
		return logServiceImpl.selLogList(page,limit,search);
	}
}
