package cn.chao.maven.controller;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.chao.maven.annotation.SysLog;
import cn.chao.maven.entity.AdminEntity;
import cn.chao.maven.entity.MenusEntity;
import cn.chao.maven.entity.RolesEntity;
import cn.chao.maven.service.AdminService;
import cn.chao.maven.utils.RRException;
import cn.chao.maven.utils.ResultUtil;
import cn.chao.maven.utils.ShiroUtils;
import cn.chao.maven.vo.Menu;
import cn.chao.maven.vo.XtreeData;

import com.google.code.kaptcha.Producer;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("sys")
@Log4j2
public class AdminController {
	@Autowired
	private AdminService adminServiceImpl;
	@Autowired
	private Producer captchaProducer = null;
	
	@RequestMapping("/main")
	public String main() {
		return "page/main";
	}

	@RequestMapping("/index")
	public String index(HttpServletRequest req) {
		AdminEntity admin = (AdminEntity)SecurityUtils.getSubject().getPrincipal();
//		log.debug(admin);
		req.setAttribute("admin", admin);
		return "redirect:/index.html";      // 重定向之后，可能并不带着内容。
	}

	@RequestMapping("/refuse")
	public String refuse() {
		return "refuse";
	}

	/**
	 * 管理员登陆
	 * 
	 * @param req
	 * @param username
	 * @param password
	 * @param vcode
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	public ResultUtil login(HttpServletRequest req, String username, String password, String vcode) {
		if(StringUtils.isEmpty(vcode)||StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
			throw new RRException("参数不能为空");
		}
		String kaptcha = ShiroUtils.getKaptcha("kaptcha").toLowerCase();
		if(!vcode.toLowerCase().equals(kaptcha)){
			return ResultUtil.error("验证码不正确");
		}
		
		try{
			Subject subject = ShiroUtils.getSubject();
			// md5加密
			// password=DigestUtils.md5DigestAsHex(password.getBytes());
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
		}catch (UnknownAccountException e) {
//			return ResultUtil.error(e.getMessage());
			return ResultUtil.error("用户不存在");
		}catch (IncorrectCredentialsException e) {
//			return ResultUtil.error(e.getMessage());
			return ResultUtil.error("密码验证失败");
		}catch (LockedAccountException e) {
			return ResultUtil.error(e.getMessage());
		}catch (AuthenticationException e) {
			return ResultUtil.error("账户验证失败");
		}
		return ResultUtil.ok();
		/*String vCode = req.getSession().getAttribute("vcode").toString().toLowerCase();
		if (vcode.toLowerCase().equals(vCode)) {
			TbAdmin admin = adminServiceImpl.login(username, password);
			if (admin != null) {
				// 登陆成功
				// 将密码置空
				admin.setPassword("");
				// 设置用户信息到Session作用域
				req.getSession().setAttribute("admin", admin);
				return new ResultUtil(0);
			}
			return new ResultUtil(502, "用户名或密码错误！");
		}
		return new ResultUtil(501, "验证码错误！");*/
	}

	/**
	 * 登出
	 * @return
	 */
	@RequestMapping(value="/loginOut")
	public String loginOut(){
		ShiroUtils.logout();
		return "redirect:/login.html";
	}
	
	/**
	 * 验证码
	 * 
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	@RequestMapping("/vcode")
	public void vcode(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		VerifyCode vc = new VerifyCode();
//		BufferedImage image = vc.getImage();// 获取一次性验证码图片
		String text = captchaProducer.createText();
		BufferedImage image = captchaProducer.createImage(text);
		// 该方法必须在getImage()方法之后来调用
		// System.out.println("验证码图片上的文本:"+vc.getText());//获取图片上的文本
		// 把文本保存到session中，为验证做准备
		//req.getSession().setAttribute("vcode", vc.getText());
		//保存到shiro session
        ShiroUtils.setSessionAttribute("kaptcha", text);
		//VerifyCode.output(image, resp.getOutputStream());// 把图片写到指定流中
		ImageIO.write(image, "JPEG", resp.getOutputStream());
	}

	/**
	 * 获取用户菜单
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/getMenus", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	@ResponseBody
	public List<Menu> getMenus(HttpServletRequest req, HttpServletResponse resp) {
	//TbAdmin admin = (TbAdmin) req.getSession().getAttribute("admin");
		AdminEntity admin = (AdminEntity)SecurityUtils.getSubject().getPrincipal();
		List<Menu> menus = null;
		if (admin != null) {
			// 得到用户菜单
			menus = adminServiceImpl.selMenus(admin);
		}
		return menus;
	}
	
	@RequestMapping("/adminList")
	public String adminList() {
		return "page/admin/adminList";
	}
	
	@RequestMapping("/menuList")
	public String menuList() {
		return "page/admin/menuList";
	}
	
	@RequestMapping("/personalData")
	public String personalData(HttpServletRequest req) {
//		TbAdmin admin=(TbAdmin) req.getSession().getAttribute("admin");
		AdminEntity admin = (AdminEntity)SecurityUtils.getSubject().getPrincipal();
		AdminEntity ad = adminServiceImpl.selAdminById(admin.getId());
		List<RolesEntity> roles = adminServiceImpl.selRoles();
		req.setAttribute("ad",ad);
		req.setAttribute("roles", roles);
		return "page/admin/personalData";
	}
	
	/**
	 * 管理员列表
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping("/getAdminList")
	@RequiresPermissions("sys:admin:list")
	@ResponseBody
	public ResultUtil getAdminList(Integer page,Integer limit) {
		ResultUtil admins = adminServiceImpl.selAdmins(page, limit);
		return admins;
	}
	
	@RequestMapping("/roleList")
	@RequiresPermissions("sys:role:list")
	public String roleList() {
		return "page/admin/roleList";
	}

	/**
	 * 管理员列表
	 * @return
	 */
	@RequestMapping("/getRoleList")
	@RequiresPermissions("sys:role:list")
	@ResponseBody
	public ResultUtil getRoleList(Integer page,Integer limit) {
		return adminServiceImpl.selRoles(page, limit);
	}

	/**
	 * 跳转编辑角色页面
	 * @param roleId
	 * @param roleName
	 * @param roleRemark
	 * @param req
	 * @return
	 */
	@RequestMapping("/editRole")
	@RequiresPermissions("sys:role:update")
	public String editRole(String roleId,String roleName,String roleRemark,HttpServletRequest req) {
		RolesEntity role=new RolesEntity();
		role.setRoleId(Long.parseLong(roleId));
		role.setRoleName(roleName);
		role.setRoleRemark(roleRemark);
		req.setAttribute("role", role);
		return "page/admin/editRole";
	}
	
	/**
	 * 得到指定角色权限树
	 * @param roleId
	 * @return
	 */
	@RequestMapping("/xtreedata")
	@ResponseBody
	public List<XtreeData> xtreeData(@RequestParam(value="roleId", defaultValue="-1") Long roleId) {
		AdminEntity admin=new AdminEntity();
		admin.setRoleId(roleId);
		return adminServiceImpl.selXtreeData1(admin);
	}
	
	/**
	 * 更新角色信息
	 * @param role 角色信息
	 * @param m 权限字符串
	 */
	@SysLog(value="更新角色信息")
	@RequestMapping("/updRole")
	@RequiresPermissions("sys:role:update")
	@ResponseBody
	public void updRole(RolesEntity role,String m) {
		//角色信息保存
		adminServiceImpl.updRole(role, m);
	}
	
	/**
	 * 添加新角色
	 * @param role
	 * @param m
	 */
	@SysLog(value="添加角色信息")
	@RequestMapping("/insRole")
	@RequiresPermissions("sys:role:save")
	@ResponseBody
	public ResultUtil insRole(RolesEntity role,String m) {
		RolesEntity r = adminServiceImpl.selRoleByRoleName(role.getRoleName());
		if(r!=null){
			return new ResultUtil(500, "角色名已存在,请重试！");
		}
		log.debug("权限管理：" + m);
		// 角色信息保存
		adminServiceImpl.insRole(role, m);
		return ResultUtil.ok();
	}
	
	/**
	 * 删除指定角色信息
	 * @param roleId
	 * @return
	 */
	@SysLog(value="删除指定角色信息")
	@RequestMapping("/delRole/{roleId}")
	@RequiresPermissions("sys:role:delete")
	@ResponseBody
	public ResultUtil delRole(@PathVariable("roleId")Long roleId) {
		ResultUtil resultUtil=new ResultUtil();
		try {
			adminServiceImpl.delRole(roleId);
			resultUtil.setCode(0);
		} catch (Exception e) {
			resultUtil.setCode(500);
			e.printStackTrace();
		}
		return resultUtil;
	}
	
	/**
	 * 批量删除指定角色信息
	 * @param rolesId
	 * @return
	 */
	@SysLog(value="批量删除指定角色信息")
	@RequestMapping("/delRoles/{rolesId}")
	@RequiresPermissions("sys:role:delete")
	@ResponseBody
	public ResultUtil delRoles(@PathVariable("rolesId")String rolesId) {
		ResultUtil resultUtil=new ResultUtil();
		try {
			adminServiceImpl.delRoles(rolesId);
			resultUtil.setCode(0);
		} catch (Exception e) {
			resultUtil.setCode(500);
			e.printStackTrace();
		}
		return resultUtil;
	}
	
	@RequestMapping("/addRole")
	@RequiresPermissions("sys:role:save")
	public String addRole() {
		return "page/admin/addRole";
	}
	
	/**
	 * 角色名唯一性检查
	 * @param roleName
	 * @return
	 */
	@RequestMapping("/checkRoleName/{roleName}")
	@ResponseBody
	public ResultUtil checkRoleName(Long roleId, @PathVariable("roleName")String roleName) {
		RolesEntity role = adminServiceImpl.selRoleByRoleName(roleName);
		if(role==null){
			return new ResultUtil(0);
		}else if(role.getRoleId()==roleId){
			return new ResultUtil(0);
		}else{
			return new ResultUtil(500,"角色名已存在！");
		}
	}
	
	/**
	 * 通过id删除管理员
	 * @param id
	 * @return
	 */
	@SysLog(value="删除指定管理员")
	@RequestMapping("/delAdminById/{id}")
	@RequiresPermissions("sys:admin:delete")
	@ResponseBody
	public ResultUtil delAdminById(@PathVariable("id")Long id) {
		if(id==1){
			return ResultUtil.error();
		}
		try {
			adminServiceImpl.delAdminById(id);
			return ResultUtil.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error();
		}
	}
	
	/**
	 * 批量删除指定管理员
	 * @param req
	 * @return
	 */
	@SysLog(value="批量删除指定管理员")
	@RequestMapping("/delAdmins/{adminStr}")
	@RequiresPermissions("sys:admin:delete")
	@ResponseBody
	public ResultUtil delAdmins(HttpServletRequest req,@PathVariable("adminStr")String adminStr) {
		String[] strs = adminStr.split(",");
		for (String str : strs) {
			AdminEntity admin = (AdminEntity)SecurityUtils.getSubject().getPrincipal();
			if((admin.getId()==Long.parseLong(str))){
				return ResultUtil.error();
			}
			if("1".equals(str)){
				return ResultUtil.error();
			}
		}
		try {
			adminServiceImpl.delAdmins(adminStr);
			return ResultUtil.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error();
		}
	}
	
	@RequestMapping("/addAdmin")
	@RequiresPermissions("sys:admin:save")
	public String addAdmin(HttpServletRequest req){
		List<RolesEntity> roles = adminServiceImpl.selRoles();
		req.setAttribute("roles", roles);
		return "page/admin/addAdmin";
	}
	
	/**
	 * 管理员用户名唯一性检查
	 * @param username
	 * @return
	 */
	@RequestMapping("/checkAdminName/{username}")
	@ResponseBody
	public ResultUtil checkAdminName(@PathVariable("username")String username) {
		AdminEntity admin = adminServiceImpl.selAdminByUserName(username);
		if(admin!=null){
			return new ResultUtil(500,"管理员已存在！");
		}
		return new ResultUtil(0);
	}
	
	/**
	 * 菜单名唯一性校验
	 * @param title
	 * @return
	 */
	@RequestMapping("/checkMenuTitle/{title}")
	@ResponseBody
	public ResultUtil checkMenuTitle(@PathVariable("title")String title) {
		MenusEntity menu = adminServiceImpl.selMenuByTitle(title);
		if(menu!=null){
			return new ResultUtil(500,"菜单已存在！");
		}
		return new ResultUtil(0);
	}

	/**
	 * 对前台传来的数据类型做一个数据绑定。
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		CustomDateEditor editor = new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true);
		binder.registerCustomEditor(Date.class, editor);
	}

	/**
	 * 增加管理員
	 * 日期类型会导致数据填充失败，请求没反应
	 * @param admin
	 * @return
	 */
	@SysLog(value="添加管理员")
	@RequestMapping("/insAdmin")
	@RequiresPermissions("sys:admin:save")
	@ResponseBody
	public ResultUtil insAdmin(AdminEntity admin) {
		// 防止浏览器提交
		AdminEntity a = adminServiceImpl.selAdminByUserName(admin.getUsername());
		if(a!=null){
			return new ResultUtil(500, "用户名已存在,请重试！");
		}
		log.debug("增加新的管理员："+admin);
		adminServiceImpl.insAdmin(admin);
		return ResultUtil.ok();
	}
	
	@RequestMapping("/editAdmin/{id}")
	@RequiresPermissions("sys:admin:update")
	public String editAdmin(HttpServletRequest req,@PathVariable("id")Long id) {
		AdminEntity ad = adminServiceImpl.selAdminById(id);
		List<RolesEntity> roles = adminServiceImpl.selRoles();
		req.setAttribute("ad",ad);
		req.setAttribute("roles", roles);
		return "page/admin/editAdmin";
	}
	
	@RequestMapping("/checkAdminByEmail/{eMail}/{username}")
	@ResponseBody
	public ResultUtil checkAdminByEmail(@PathVariable("eMail") String eMail,@PathVariable("username") String username) {
		AdminEntity admin=adminServiceImpl.selAdminByEmail(eMail,username);
		if(admin!=null){
			return new ResultUtil(500,"邮箱已被占用！");
		}
		return new ResultUtil(0);
	}
	
	/**
	 * 更新管理员信息
	 * @param admin
	 */
	@SysLog(value="更新管理员信息")
	@RequestMapping("/updAdmin")
	@RequiresPermissions("sys:admin:update")
	@ResponseBody
	public ResultUtil updAdmin(AdminEntity admin) {
		if(admin!=null&&admin.getId()==1){
			return ResultUtil.error("不允许修改!");
		}
		try {
			adminServiceImpl.updAdmin(admin);
			return ResultUtil.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error();
		}
	}
	
	@RequestMapping("/changePwd")
	public String changePwd() {
		return "page/admin/changePwd";
	}
	
	/**
	 * 修改密码
	 * @param req
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	@SysLog(value="修改密码")
	@RequestMapping("/updPwd")
	@ResponseBody
	public ResultUtil updPwd(HttpServletRequest req,String oldPwd,String newPwd) {
		AdminEntity user = (AdminEntity)SecurityUtils.getSubject().getPrincipal();
		if(user!=null){
			//测试账号不支持修改密码
			if("test".equals(user.getUsername())){
				return ResultUtil.error();
			}
			AdminEntity admin = adminServiceImpl.login(user.getUsername(), oldPwd);
			if(admin!=null){
				admin.setPassword(newPwd);
				adminServiceImpl.updAdmin1(admin);
				//修改密码后移除作用域，重新登陆
				SecurityUtils.getSubject().logout();
				return ResultUtil.ok();
			}else{
				return new ResultUtil(501,"旧密码错误，请重新填写！");
			}
		}
		return new ResultUtil(500,"请求错误！");
	}
	
	@RequestMapping("/druid")
	@RequiresPermissions("sys:druid:list")
	public String druid(){
		return "redirect:/druid/index.html";
	}
	
	/**
	 * 获取菜单信息
	 * @return
	 */
	@RequestMapping("/menuData")
	@RequiresPermissions("sys:menu:list")
	@ResponseBody
	public ResultUtil menuData(){
		List<MenusEntity> list=adminServiceImpl.selMenusByParentId();
		ResultUtil resultUtil=new ResultUtil();
		resultUtil.setCode(0);
		resultUtil.setCount(list.size()+0L);
		resultUtil.setData(list);
		return resultUtil;
	}
	
	@RequestMapping("/toSaveMenu/{menuId}")
	@RequiresPermissions("sys:menu:save")
	public String toSaveMenu(@PathVariable("menuId") Long menuId,Model model){
		if(menuId!=null&&menuId!=1){
			MenusEntity menus=new MenusEntity();
			menus.setMenuId(menuId);
			model.addAttribute("menu",menus);
			model.addAttribute("flag","1");
			return "page/admin/menuForm";
		}else{
			model.addAttribute("msg","不允许操作！");
			return "page/active";
		}
	}
	@RequestMapping("/toEditMenu/{menuId}")
	@RequiresPermissions("sys:menu:update")
	public String toEditMenu(@PathVariable("menuId") Long menuId,Model model){
		if(menuId!=null&&menuId!=1){
			MenusEntity menus=adminServiceImpl.selMenuById(menuId);
			model.addAttribute("menu",menus);
			return "page/admin/menuForm";
		}else if(menuId==1){
			model.addAttribute("msg","不允许操作此菜单！");
			return "page/active";
		}else{
			model.addAttribute("msg","不允许操作！");
			return "page/active";
		}
	}
	
	@SysLog("维护菜单信息")
	@RequestMapping(value = "/menuForm",method = RequestMethod.POST)
	@RequiresPermissions(value={"sys:menu:save","sys:menu:update"})
	@ResponseBody
	public ResultUtil menuForm(MenusEntity menus,String flag){
		log.debug("参数的内容：" + menus + ",和" + flag);
		if(StringUtils.isBlank(flag)){
			//同级菜单名不相同
			List<MenusEntity> data=adminServiceImpl.checkTitleSameLevel(menus);
			MenusEntity m = adminServiceImpl.selMenuById(menus.getMenuId());
			Boolean f=false;
			if(m.getTitle().equals(menus.getTitle())){
				f=true;
			}
			if(!f||data.size()>1){
				return ResultUtil.error("同级菜单名不能相同！");
			}
			menus.setSpread("false");
			adminServiceImpl.updMenu(menus);
			return ResultUtil.ok("修改成功！");
		}else if(menus.getMenuId()!=1){
			menus.setParentId(menus.getMenuId());
			
			//规定只能3级菜单
			MenusEntity m=adminServiceImpl.selMenusById(menus.getMenuId());
			if(m!=null&&m.getParentId()!=0){
				MenusEntity m1=adminServiceImpl.selMenusById(m.getParentId());
				if(m1!=null&&m1.getParentId()!=0){
					return ResultUtil.error("此菜单不允许添加子菜单！");
				}
			}
			
			//同级菜单名不相同
			List<MenusEntity> data=adminServiceImpl.checkTitleSameLevel(menus);
			if(data.size()>0){
				return ResultUtil.error("同级菜单名不能相同！");
			}
			
			menus.setMenuId(null);
			menus.setSpread("false");
			adminServiceImpl.insMenu(menus);
			return ResultUtil.ok("添加成功！");
		}else{
			return ResultUtil.error("此菜单不允许操作！");
		}
	}
	
	//delMenuById
	@SysLog(value="删除菜单信息")
	@RequestMapping("/delMenuById/{menuId}")
	@RequiresPermissions("sys:menu:delete")
	@ResponseBody
	public ResultUtil delMenuById(@PathVariable("menuId")Long menuId) {
		try {
			if(menuId==1){
				return ResultUtil.error("此菜单不允许删除！");
			}
			//查询是否有子菜单，不允许删除
			List<MenusEntity> data=adminServiceImpl.selMenusById1(menuId);
			if(data!=null&&data.size()>0){
				return ResultUtil.error("包含子菜单，不允许删除！");
			}
			adminServiceImpl.delMenuById(menuId);
			return ResultUtil.ok("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error("系统错误！");
		}
	}
	
	/**
	 * 获取菜单信息
	 * @param menuId
	 * @param sorting
	 * @return
	 */
	@RequestMapping("/updMenuSortingById")
	@RequiresPermissions("sys:menu:update")
	@ResponseBody
	public ResultUtil updMenuSortingById(Long menuId,String sorting){
		MenusEntity menus=new MenusEntity();
		menus.setMenuId(menuId);
		try{
		    Long.parseLong(sorting);
		}catch(NumberFormatException e)
		{
			return ResultUtil.error("修改失败，只允许输入整数！");
		}
		menus.setSorting(Long.parseLong(sorting));
		adminServiceImpl.updMenuSortingById(menus);
		return ResultUtil.ok();
	}
}
