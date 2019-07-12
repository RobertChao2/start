package cn.chao.maven.service;

import cn.chao.maven.entity.UsersEntity;

public interface AccountService {
	
	//根据eCode和status查询用户
	public UsersEntity selUserByCodeAndStatus(String eCode, String status);

	//更新用户状态
	public void updUserStatus(UsersEntity user);
}
