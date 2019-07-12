package cn.chao.maven.service;

import cn.chao.maven.entity.UsersEntity;

import java.util.List;

public interface MainService {

	public List<UsersEntity> selUserList();

	public List<UsersEntity> selUsersToday();

	public List<UsersEntity> selUsersYestoday();
	
	public List<UsersEntity> selUsersYearWeek();
	
	public List<UsersEntity> selUsersMonth();

	public int seUserCountBygender(int i);
}
