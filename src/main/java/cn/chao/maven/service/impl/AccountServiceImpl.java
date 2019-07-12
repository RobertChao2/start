package cn.chao.maven.service.impl;

import java.util.List;

import cn.chao.maven.entity.UsersEntity;
import cn.chao.maven.entity.UsersEntityExample;
import cn.chao.maven.mapper.UsersEntityMapper;
import cn.chao.maven.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private UsersEntityMapper tbUsersMapper;

	@Override
	public UsersEntity selUserByCodeAndStatus(String eCode, String status) {
		UsersEntityExample example=new UsersEntityExample();
		UsersEntityExample.Criteria criteria = example.createCriteria();
		criteria.andECodeEqualTo(eCode);
		criteria.andStatusEqualTo(status);
		List<UsersEntity> users = tbUsersMapper.selectByExample(example);
		if(users!=null&&users.size()>0){
			return users.get(0);
		}
		return null;
	}

	@Override
	public void updUserStatus(UsersEntity user) {
		tbUsersMapper.updateByPrimaryKey(user);
	}

}
