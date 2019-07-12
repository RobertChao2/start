package cn.chao.maven.service.impl;

import java.util.Date;
import java.util.List;

import cn.chao.maven.entity.UsersEntity;
import cn.chao.maven.entity.UsersEntityExample;
import cn.chao.maven.mapper.UsersEntityMapper;
import cn.chao.maven.service.UserService;
import cn.chao.maven.utils.EmailUtil;
import cn.chao.maven.utils.GlobalUtil;
import cn.chao.maven.utils.MyUtil;
import cn.chao.maven.utils.ResultUtil;
import cn.chao.maven.vo.UserSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UsersEntityMapper tbUsersMapper;
	
	@Override
	public UsersEntity selUserByEmail(String eMail, Long uid) {
		UsersEntityExample example=new UsersEntityExample();
		UsersEntityExample.Criteria criteria = example.createCriteria();
		criteria.andEMailEqualTo(eMail);
		if(uid!=null&&!"".equals(uid)){
			criteria.andUidNotEqualTo(uid);
		}
		List<UsersEntity> users = tbUsersMapper.selectByExample(example);
		if(users!=null&&users.size()>0){
			return users.get(0);
		}
		return null;
	}

	@Override
	public UsersEntity selUserByNickname(String nickname,Long uid) {
		UsersEntityExample example=new UsersEntityExample();
		UsersEntityExample.Criteria criteria = example.createCriteria();
		criteria.andNicknameEqualTo(nickname);
		if(uid!=null&&!"".equals(uid)){
			criteria.andUidNotEqualTo(uid);
		}
		List<UsersEntity> users = tbUsersMapper.selectByExample(example);
		if(users!=null&&users.size()>0){
			return users.get(0);
		}
		return null;
	}

	@Override
	public void insUserService(UsersEntity user) throws Exception {
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		String code= MyUtil.getStrUUID();
		user.seteCode(code);
		Date date=new Date();
		user.setCreateTime(date);
		if(Boolean.parseBoolean(GlobalUtil.getValue("send.email"))){
			user.setStatus(0+"");//0:未激活，1，正常，2，禁用
			EmailUtil.sendMail(user.geteMail(),code);
		}else{
			user.setStatus(1+"");//0:未激活，1，正常，2，禁用
		}
		tbUsersMapper.insert(user);
	}

	@Override
	public ResultUtil selUsers(Integer page, Integer limit, UserSearch search) {
		PageHelper.startPage(page, limit);
		UsersEntityExample example=new UsersEntityExample();
		//设置按创建时间降序排序
		example.setOrderByClause("create_time DESC");
		UsersEntityExample.Criteria criteria = example.createCriteria();
		
		if(search.getNickname()!=null&&!"".equals(search.getNickname())){
			//注意：模糊查询需要进行拼接”%“  如下，不进行拼接是不能完成查询的哦。
			criteria.andNicknameLike("%"+search.getNickname()+"%");
		}
		if(search.getSex()!=null&&!"-1".equals(search.getSex())){
			criteria.andSexEqualTo(search.getSex());
		}
		if(search.getStatus()!=null&&!"-1".equals(search.getStatus())){
			criteria.andStatusEqualTo(search.getStatus());
		}
		if(search.getCreateTimeStart()!=null&&!"".equals(search.getCreateTimeStart())){
			criteria.andCreateTimeGreaterThanOrEqualTo(MyUtil.getDateByString(search.getCreateTimeStart()));
		}
		if(search.getCreateTimeEnd()!=null&&!"".equals(search.getCreateTimeEnd())){
			criteria.andCreateTimeLessThanOrEqualTo(MyUtil.getDateByString(search.getCreateTimeEnd()));
		}
		
		List<UsersEntity> users = tbUsersMapper.selectByExample(example);
		PageInfo<UsersEntity> pageInfo = new PageInfo<UsersEntity>(users);
		ResultUtil resultUtil = new ResultUtil();
		resultUtil.setCode(0);
		resultUtil.setCount(pageInfo.getTotal());
		resultUtil.setData(pageInfo.getList());
		return resultUtil;
	}

	@Override
	public void delUsersService(String userStr) {
		String[] users = userStr.split(",");
		if(users!=null&&users.length>0){
			for (String uid : users) {
				tbUsersMapper.deleteByPrimaryKey(Long.parseLong(uid));
			}
		}
	}

	@Override
	public void delUserByUid(String uid) {
		tbUsersMapper.deleteByPrimaryKey(Long.parseLong(uid));
	}

	@Override
	public UsersEntity selUserByUid(Long uid) {
		return tbUsersMapper.selectByPrimaryKey(uid);
	}

	@Override
	public void updUserService(UsersEntity user) {
		UsersEntity u = tbUsersMapper.selectByPrimaryKey(user.getUid());
		user.setPassword(u.getPassword());
		user.seteCode(u.geteCode());
		user.setCreateTime(u.getCreateTime());
		tbUsersMapper.updateByPrimaryKey(user);
	}

}
