package cn.chao.maven.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.chao.maven.entity.*;
import cn.chao.maven.mapper.*;
import cn.chao.maven.service.AdminService;
import cn.chao.maven.utils.ResultUtil;
import cn.chao.maven.vo.Menu;
import cn.chao.maven.vo.XtreeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private AdminEntityMapper adminMapper;

	@Autowired
	private RolesEntityMapper tbRolesMapper;

	@Autowired
	private RolesMenusEntityMapper tbRolesMenusMapper;

	@Autowired
	private AdminMenusMapper adminMenusMapper;

	@Autowired
	private MenusEntityMapper tbMenusMapper;

	/**
	 * 管理员登陆
	 */
	@Override
	public AdminEntity login(String username, String password) {
		//对密码加密
		password=DigestUtils.md5DigestAsHex(password.getBytes());
		AdminEntityExample example = new AdminEntityExample();
		AdminEntityExample.Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		criteria.andPasswordEqualTo(password);
		List<AdminEntity> admin = adminMapper.selectByExample(example);
		if (admin != null && admin.size() > 0) {
			return admin.get(0);
		}
		return null;
	}

	@Override
	public ResultUtil selRoles(Integer page, Integer limit) {
		PageHelper.startPage(page, limit);
		RolesEntityExample example = new RolesEntityExample();
		List<RolesEntity> list = tbRolesMapper.selectByExample(example);
		PageInfo<RolesEntity> pageInfo = new PageInfo<RolesEntity>(list);
		ResultUtil resultUtil = new ResultUtil();
		resultUtil.setCode(0);
		resultUtil.setCount(pageInfo.getTotal());
		resultUtil.setData(pageInfo.getList());
		return resultUtil;
	}

	@Override
	public ResultUtil selAdmins(Integer page, Integer limit) {
		PageHelper.startPage(page, limit);
		AdminEntityExample example = new AdminEntityExample();
		List<AdminEntity> list = adminMapper.selectByExample(example);
		// 将roleName写进TbAdmin
		for (AdminEntity tbAdmin : list) {
			// tbAdmin.setRoleName();
			List<RolesEntity> roles = selRoles();
			for (RolesEntity tbRole : roles) {
				if (tbRole.getRoleId() == tbAdmin.getRoleId()) {
//					tbAdmin.setRoleName(tbRole.getRoleName());
					tbAdmin.setRoleId(tbRole.getRoleId());
				}
			}
		}
		PageInfo<AdminEntity> pageInfo = new PageInfo<AdminEntity>(list);
		ResultUtil resultUtil = new ResultUtil();
		resultUtil.setCode(0);
		resultUtil.setCount(pageInfo.getTotal());
		resultUtil.setData(pageInfo.getList());
		return resultUtil;
	}

	@Override
	public List<RolesEntity> selRoles() {
		RolesEntityExample example = new RolesEntityExample();
		List<RolesEntity> list = tbRolesMapper.selectByExample(example);
		return list;
	}

	@Override
	public List<Menu> selMenus(AdminEntity admin) {
		List<Menu> results = new ArrayList<>();
		Long roleId = admin.getRoleId();
		RolesMenusEntityExample example = new RolesMenusEntityExample();
		RolesMenusEntityExample.Criteria criteria = example.createCriteria();
		criteria.andRoleIdEqualTo(roleId);
		List<RolesMenusEntityKey> list = tbRolesMenusMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			List<MenusEntity> menus = adminMenusMapper.getMenus(roleId);
			for (int i = 0; i < menus.size(); i++) {
				if (menus.get(i).getParentId() == 0) {
					Menu menu = new Menu();
					menu.setTitle(menus.get(i).getTitle());
					menu.setIcon(menus.get(i).getIcon());
					menu.setHref(menus.get(i).getHref());
					menu.setSpread(menus.get(i).getSpread());
					List<Menu> menus2 = new ArrayList<>();
					for (int j = 0; j < menus.size(); j++) {
						if (menus.get(j).getParentId() == menus.get(i).getMenuId()) {
							Menu menu2 = new Menu();
							menu2.setTitle(menus.get(j).getTitle());
							menu2.setIcon(menus.get(j).getIcon());
							menu2.setHref(menus.get(j).getHref());
							menu2.setSpread(menus.get(j).getSpread());
							menus2.add(menu2);
						}
					}
					menu.setChildren(menus2);
					results.add(menu);
				}
			}
		}
		return results;
	}

	@Override
	public List<XtreeData> selXtreeData(AdminEntity admin) {
		List<XtreeData> list = new ArrayList<>();
		// 获取所有的权限菜单
		MenusEntityExample example = new MenusEntityExample();
		example.setOrderByClause("sorting DESC");
		List<MenusEntity> allMenus = tbMenusMapper.selectByExample(example);
		// 获取指定角色的菜单
		List<MenusEntity> menus = adminMenusMapper.getMenus(admin.getRoleId());
		for (MenusEntity m : allMenus) {
			if (m.getParentId() == 0) {
				XtreeData x = new XtreeData();
				x.setTitle(m.getTitle());
				x.setValue(m.getMenuId() + "");
				List<XtreeData> list2 = new ArrayList<>();
				for (MenusEntity m1 : allMenus) {
					if (m1.getParentId() == m.getMenuId()) {
						XtreeData x1 = new XtreeData();
						x1.setTitle(m1.getTitle());
						x1.setValue(m1.getMenuId() + "");
						// 是否拥有权限
						x1.setChecked(false);
						for (MenusEntity mh : menus) {
							if (mh.getMenuId() == m1.getMenuId()) {
								x1.setChecked(true);
								break;
							}
						}
						// 使数据data不为null
						List<XtreeData> l = new ArrayList<>();
						x1.setData(l);
						list2.add(x1);
					}
				}
				x.setData(list2);
				list.add(x);
			}
		}

		// 拥有没有子节点的节点，设置选中
		for (XtreeData xd : list) {
			if (xd.getData() == null || xd.getData().size() == 0) {
				for (MenusEntity tbMenus : menus) {
					if (tbMenus.getMenuId() == Long.parseLong(xd.getValue())) {
						xd.setChecked(true);
						break;
					}
				}
			}
		}
		//默认拥有首页菜单权限
		list.get(0).setDisabled(true);
		list.get(0).setChecked(true);
		return list;
	}
	@Override
	public List<XtreeData> selXtreeData1(AdminEntity admin) {
		List<XtreeData> list = new ArrayList<>();
		// 获取所有的权限菜单
		MenusEntityExample example = new MenusEntityExample();
		example.setOrderByClause("sorting DESC");
		List<MenusEntity> allMenus = tbMenusMapper.selectByExample(example);
		// 获取指定角色的菜单
		List<MenusEntity> menus = adminMenusMapper.getMenus(admin.getRoleId());
		for (MenusEntity m : allMenus) {
			if (m.getParentId() == 0) {
				XtreeData x = new XtreeData();
				x.setTitle(m.getTitle());
				x.setValue(m.getMenuId() + "");
				//一级菜单选中
				for (MenusEntity mh : menus) {
					if (mh.getMenuId() == m.getMenuId()) {
						x.setChecked(true);
						break;
					}
				}
				List<XtreeData> list2 = new ArrayList<>();
				for (MenusEntity m1 : allMenus) {
					if (m1.getParentId() == m.getMenuId()) {
						XtreeData x1 = new XtreeData();
						x1.setTitle(m1.getTitle());
						x1.setValue(m1.getMenuId() + "");
						List<XtreeData> list3 = new ArrayList<>();
						//二级菜单选中
						for (MenusEntity mh : menus) {
							if (mh.getMenuId() == m1.getMenuId()) {
								x1.setChecked(true);
								break;
							}
						}
						for (MenusEntity m2 : allMenus) {
							if (m2.getParentId() == m1.getMenuId()) {
								XtreeData x2 = new XtreeData();
								x2.setTitle(m2.getTitle());
								x2.setValue(m2.getMenuId() + "");
								//三级菜单选中
								for (MenusEntity mh1 : menus) {
									if (mh1.getMenuId() == m2.getMenuId()) {
										x2.setChecked(true);
										break;
									}
								}
								// 使数据data不为null
								List<XtreeData> l = new ArrayList<>();
								x2.setData(l);
								list3.add(x2);
							}
						}

						x1.setData(list3);
						list2.add(x1);
					}
				}
				x.setData(list2);
				list.add(x);
			}
		}

		// 拥有没有子节点的节点，设置选中
		for (XtreeData xd : list) {
			if (xd.getData() == null || xd.getData().size() == 0) {
				for (MenusEntity tbMenus : menus) {
					if (tbMenus.getMenuId() == Long.parseLong(xd.getValue())) {
						xd.setChecked(true);
					}
				}
			}
		}
		//默认拥有首页菜单权限
		list.get(0).setDisabled(true);
		list.get(0).setChecked(true);
		return list;
	}

	@Override
	public void updRole(RolesEntity role, String m) {
		// 更新角色信息
		tbRolesMapper.updateByPrimaryKey(role);
		// 先删除角色所有权限
		RolesMenusEntityExample example = new RolesMenusEntityExample();
		RolesMenusEntityExample.Criteria criteria = example.createCriteria();
		criteria.andRoleIdEqualTo(role.getRoleId());
		tbRolesMenusMapper.deleteByExample(example);
		// 更新权限信息
		if (m != null && m.length() != 0) {
			String[] array = m.split(",");
			List<String> result = new ArrayList<>();
			boolean flag;
			for(int i=0;i<array.length;i++){
			    flag = false;
			    for(int j=0;j<result.size();j++){
			        if(array[i].equals(result.get(j))){
			            flag = true;
			            break;
			        }
			    }
			    if(!flag){
			        result.add(array[i]);
			    }
			}
			// 重新赋予权限
			if (result != null && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					RolesMenusEntityKey record = new RolesMenusEntityKey();
					record.setMenuId(Long.parseLong(result.get(i)));
					record.setRoleId(role.getRoleId());
					// 维护角色—菜单表
					tbRolesMenusMapper.insert(record);
				}
			}
		}
	}

	@Override
	public void delRole(Long roleId) {
		tbRolesMapper.deleteByPrimaryKey(roleId);
	}

	@Override
	public void delRoles(String rolesId) {
		String[] rids = rolesId.split(",");
		for (String id : rids) {
			tbRolesMapper.deleteByPrimaryKey(Long.parseLong(id));
		}
	}

	@Override
	public RolesEntity selRoleByRoleName(String roleName) {
		RolesEntityExample example = new RolesEntityExample();
		RolesEntityExample.Criteria criteria = example.createCriteria();
		criteria.andRoleNameEqualTo(roleName);
		List<RolesEntity> roles = tbRolesMapper.selectByExample(example);
		if (roles != null && roles.size() > 0) {
			return roles.get(0);
		}
		return null;
	}

	@Override
	public void insRole(RolesEntity role, String m) {
		//维护角色表
		tbRolesMapper.insert(role);
		// 维护角色-菜单表
		if (m != null && m.length() != 0) {
			String[] array = m.split(",");
			List<String> result = new ArrayList<>();
			boolean flag;
			for(int i=0;i<array.length;i++){
			    flag = false;
			    for(int j=0;j<result.size();j++){
			        if(array[i].equals(result.get(j))){
			            flag = true;
			            break;
			        }
			    }
			    if(!flag){
			        result.add(array[i]);
			    }
			}
			// 重新赋予权限
			if (result != null && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					RolesMenusEntityKey record = new RolesMenusEntityKey();
					record.setMenuId(Long.parseLong(result.get(i)));
					record.setRoleId(role.getRoleId());
					// 维护角色—菜单表
					tbRolesMenusMapper.insert(record);
				}
			}
		}
	}

	@Override
	public void delAdminById(Long id) {
		adminMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void delAdmins(String adminStr) {
		String[] strs = adminStr.split(",");
		if(strs!=null&&strs.length>0){
			for (String str : strs) {
				adminMapper.deleteByPrimaryKey(Long.parseLong(str));
			}
		}
	}

	@Override
	public AdminEntity selAdminByUserName(String username) {
		AdminEntityExample example = new AdminEntityExample();
		AdminEntityExample.Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<AdminEntity> admins = adminMapper.selectByExample(example);
		if (admins != null && admins.size() > 0) {
			return admins.get(0);
		}
		return null;
	}

	@Override
	public void insAdmin(AdminEntity admin) {
		//对密码md5加密
		admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
		adminMapper.insert(admin);
	}

	@Override
	public AdminEntity selAdminById(Long id) {
		AdminEntity admin=adminMapper.selectByPrimaryKey(id);
		//为了安全，密码置空
		admin.setPassword("");
		return admin;
	}

	@Override
	public AdminEntity selAdminByEmail(String eMail,String username) {
		AdminEntityExample example = new AdminEntityExample();
		AdminEntityExample.Criteria criteria = example.createCriteria();
		criteria.andEMailEqualTo(eMail);
		if(username!=null&&!"".equals(username)){
			criteria.andUsernameNotEqualTo(username);
		}
		List<AdminEntity> admins = adminMapper.selectByExample(example);
		if (admins != null && admins.size() > 0) {
			return admins.get(0);
		}
		return null;
	}

	@Override
	public void updAdmin(AdminEntity admin) {
		AdminEntity a = adminMapper.selectByPrimaryKey(admin.getId());
		admin.setPassword(a.getPassword());
		adminMapper.updateByPrimaryKey(admin);
	}

	@Override
	public void updAdmin1(AdminEntity admin) {
		admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
		adminMapper.updateByPrimaryKey(admin);
	}

	@Override
	public List<MenusEntity> selMenusByParentId() {
		MenusEntityExample example=new MenusEntityExample();
		example.setOrderByClause("sorting DESC");
		List<MenusEntity> data = tbMenusMapper.selectByExample(example);
		return data;
	}

	@Override
	public MenusEntity selMenuById(Long menuId) {
		MenusEntity menu = tbMenusMapper.selectByPrimaryKey(menuId);
		return menu;
	}

	@Override
	public void insMenu(MenusEntity menus) {
		tbMenusMapper.insert(menus);
	}

	@Override
	public void updMenu(MenusEntity menus) {
		tbMenusMapper.updateByPrimaryKey(menus);
	}

	@Override
	public MenusEntity selMenuByTitle(String title) {
		MenusEntityExample example=new MenusEntityExample();
		MenusEntityExample.Criteria criteria = example.createCriteria();
		criteria.andTitleEqualTo(title);
		List<MenusEntity> data = tbMenusMapper.selectByExample(example);
		if(data!=null&&data.size()>0){
			return data.get(0);
		}
		return null;
	}

	@Override
	public MenusEntity selMenusById(Long menuId) {
		MenusEntityExample example=new MenusEntityExample();
		MenusEntityExample.Criteria criteria = example.createCriteria();
		criteria.andMenuIdEqualTo(menuId);
		List<MenusEntity> data = tbMenusMapper.selectByExample(example);
		if(data!=null&&data.size()>0){
			return data.get(0);
		}
		return null;
	}

	@Override
	public void delMenuById(Long menuId) {
		tbMenusMapper.deleteByPrimaryKey(menuId);
	}

	@Override
	public List<MenusEntity> selMenusById1(Long menuId) {
		MenusEntityExample example=new MenusEntityExample();
		MenusEntityExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(menuId);
		List<MenusEntity> data = tbMenusMapper.selectByExample(example);
		return data;
	}

	@Override
	public void updMenuSortingById(MenusEntity menus) {
		MenusEntity m = tbMenusMapper.selectByPrimaryKey(menus.getMenuId());
		m.setSorting(menus.getSorting());
		tbMenusMapper.updateByPrimaryKey(m);
	}

	@Override
	public List<MenusEntity> checkTitleSameLevel(MenusEntity menus) {
		MenusEntityExample example=new MenusEntityExample();
		MenusEntityExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(menus.getParentId());
		criteria.andTitleEqualTo(menus.getTitle());
		List<MenusEntity> data = tbMenusMapper.selectByExample(example);
		return data;
	}
}
