package cn.chao.maven.service;
import cn.chao.maven.entity.LogEntity;
import cn.chao.maven.utils.ResultUtil;
import cn.chao.maven.vo.UserSearch;

import java.util.Date;

public interface LogService {
	//添加日志
	public void insLog(LogEntity log);
	
	//获取日志列表
	ResultUtil selLogList(Integer page, Integer limit, UserSearch search);

	//删除指定日期以前的日志
	public int delLogsByDate(Date date);
}
