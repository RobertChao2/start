package cn.chao.maven.service.impl;

import java.util.List;

import cn.chao.maven.entity.UsersEntity;
import cn.chao.maven.entity.UsersEntityExample;
import cn.chao.maven.mapper.MainMapper;
import cn.chao.maven.mapper.UsersEntityMapper;
import cn.chao.maven.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {
	
	@Autowired
	private UsersEntityMapper tbUsersMapper;
	
	@Autowired
	private MainMapper mainMapper;

	@Override
	public List<UsersEntity> selUserList() {
		UsersEntityExample example=new UsersEntityExample();
		return tbUsersMapper.selectByExample(example);
	}
	
	@Override
	public List<UsersEntity> selUsersToday() {
		return mainMapper.selUsersToday();
	}

	@Override
	public List<UsersEntity> selUsersYestoday() {
		return mainMapper.selUsersYesterday();
	}


	@Override
	public List<UsersEntity> selUsersYearWeek() {
		return mainMapper.selUsersYearWeek();
	}
	
	@Override
	public List<UsersEntity> selUsersMonth() {
		return mainMapper.selUsersMonth();
	}

	@Override
	public int seUserCountBygender(int i) {
		UsersEntityExample example=new UsersEntityExample();
		UsersEntityExample.Criteria criteria = example.createCriteria();
		criteria.andSexEqualTo(i+"");
		List<UsersEntity> list = tbUsersMapper.selectByExample(example);
		return list.size();
	}

}
