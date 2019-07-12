package cn.chao.maven.service.impl;

import java.util.Date;
import java.util.List;

import cn.chao.maven.entity.LogEntity;
import cn.chao.maven.entity.LogEntityExample;
import cn.chao.maven.mapper.LogEntityMapper;
import cn.chao.maven.service.LogService;
import cn.chao.maven.utils.MyUtil;
import cn.chao.maven.utils.ResultUtil;
import cn.chao.maven.vo.UserSearch;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {
	
	@Autowired
	private LogEntityMapper LogEntityMapper;

	@Override
	public void insLog(LogEntity log) {
		LogEntityMapper.insert(log);
	}

	@Override
	public ResultUtil selLogList(Integer page, Integer limit, UserSearch search) {
		PageHelper.startPage(page, limit);
		LogEntityExample example=new LogEntityExample();
		//设置按创建时间降序排序
		example.setOrderByClause("id DESC");
		LogEntityExample.Criteria criteria = example.createCriteria();
		
		if(search.getOperation()!=null&&!"".equals(search.getOperation())){
			criteria.andOperationLike("%"+search.getOperation()+"%");
		}
		
		if(search.getCreateTimeStart()!=null&&!"".equals(search.getCreateTimeStart())){
			criteria.andCreateTimeGreaterThanOrEqualTo(MyUtil.getDateByString(search.getCreateTimeStart()));
		}
		if(search.getCreateTimeEnd()!=null&&!"".equals(search.getCreateTimeEnd())){
			criteria.andCreateTimeLessThanOrEqualTo(MyUtil.getDateByString(search.getCreateTimeEnd()));
		}
		
		List<LogEntity> logs = LogEntityMapper.selectByExample(example);
		PageInfo<LogEntity> pageInfo = new PageInfo<LogEntity>(logs);
		ResultUtil resultUtil = new ResultUtil();
		resultUtil.setCode(0);
		resultUtil.setCount(pageInfo.getTotal());
		resultUtil.setData(pageInfo.getList());
		return resultUtil;
	}

	@Override
	public int delLogsByDate(Date date) {
		LogEntityExample example=new LogEntityExample();
		LogEntityExample.Criteria criteria = example.createCriteria();
		criteria.andCreateTimeLessThanOrEqualTo(date);
		int count = LogEntityMapper.deleteByExample(example);
		return count;
	}

}
