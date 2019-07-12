package cn.chao.maven.service;


import cn.chao.maven.entity.AdminEntity;
import cn.chao.maven.entity.MenusEntity;
import cn.chao.maven.entity.RolesEntity;
import cn.chao.maven.utils.ResultUtil;
import cn.chao.maven.vo.Menu;
import cn.chao.maven.vo.XtreeData;

import java.util.List;

public interface AdminService {
	// 登陆
	public AdminEntity login(String username, String password);
	
	//获取所有角色
	public ResultUtil selRoles(Integer page, Integer limit);
	
	//获取所有角色
	public List<RolesEntity> selRoles();
	
	//获取所有管理员
	public ResultUtil selAdmins(Integer page, Integer limit);
	
	//获取角色菜单
	public List<Menu> selMenus(AdminEntity admin);
	
	//获取指定角色权限树
	public List<XtreeData> selXtreeData(AdminEntity admin);
	//获取指定角色权限树
	public List<XtreeData> selXtreeData1(AdminEntity admin);
	
	//更新角色信息
	public void updRole(RolesEntity role, String m);
	
	//删除指定角色
	public void delRole(Long roleId);

	//批量删除指定角色
	public void delRoles(String rolesId);
	
	//根据角色名查询角色
	public RolesEntity selRoleByRoleName(String roleName);

	//添加新角色
	public void insRole(RolesEntity role, String m);

	//删除指定管理员
	public void delAdminById(Long id);

	//批量删除指定管理员
	public void delAdmins(String adminStr);

	//管理员用户名唯一性校验
	public AdminEntity selAdminByUserName(String username);

	//新增管理员
	public void insAdmin(AdminEntity admin);

	//根据id得到管理员
	public AdminEntity selAdminById(Long id);

	//根据email得到管理员
	public AdminEntity selAdminByEmail(String eMail, String username);

	//更新管理员信息
	public void updAdmin(AdminEntity admin);

	public void updAdmin1(AdminEntity admin);

	public List<MenusEntity> selMenusByParentId();

	public MenusEntity selMenuById(Long menuId);

	public void insMenu(MenusEntity menus);

	public void updMenu(MenusEntity menus);

	public MenusEntity selMenuByTitle(String title);

	public MenusEntity selMenusById(Long menuId);

	public void delMenuById(Long menuId);

	public List<MenusEntity> selMenusById1(Long menuId);

	public void updMenuSortingById(MenusEntity menus);

	public List<MenusEntity> checkTitleSameLevel(MenusEntity menus);
}
