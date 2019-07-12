package cn.chao.maven.service;

import cn.chao.maven.entity.UsersEntity;
import cn.chao.maven.utils.ResultUtil;
import cn.chao.maven.vo.UserSearch;

public interface UserService {
	//用户邮箱唯一性检验
	public UsersEntity selUserByEmail(String eMail, Long uid);

	//用户昵称唯一性检验
	public UsersEntity selUserByNickname(String nickname, Long uid);

	//增加用户
	public void insUserService(UsersEntity user) throws Exception;

	//得到用户信息
	public ResultUtil selUsers(Integer page, Integer limit, UserSearch search);

	//批量删除用户
	public void delUsersService(String userStr);

	//删除指定用户
	public void delUserByUid(String uid);

	//查询用户
	public UsersEntity selUserByUid(Long uid);

	//更新用户信息
	public void updUserService(UsersEntity user);
}
