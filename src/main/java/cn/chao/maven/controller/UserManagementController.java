package cn.chao.maven.controller;

import cn.chao.maven.annotation.SysLog;
import cn.chao.maven.entity.UsersEntity;
import cn.chao.maven.service.UserService;
import cn.chao.maven.utils.ResultUtil;
import cn.chao.maven.vo.UserSearch;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("user/")
@Log4j2
public class UserManagementController {
	
	@Autowired
	private UserService userServiceImpl;
	
	@RequestMapping("addUser")
	@RequiresPermissions("user:user:save")
	public String userAdd(){
		return "page/user/addUser";
	}
	
	@RequestMapping("userList")
	@RequiresPermissions("user:user:list")
	public String userList(){
		return "page/user/userList";
	}
	
	@RequestMapping("checkUserByEmail")
	@ResponseBody
	public ResultUtil checkUserEmail(String eMail, Long uid){
		UsersEntity user = userServiceImpl.selUserByEmail(eMail,uid);
		if(user!=null){
			return new ResultUtil(500,"邮箱已存在，请重新填写！");
		}
		return new ResultUtil(0);
	}
	
	@RequestMapping("checkUserByNickname/{nickname}")
	@ResponseBody
	public ResultUtil checkNickname(@PathVariable("nickname")String nickname,Long uid){
		UsersEntity user = userServiceImpl.selUserByNickname(nickname,uid);
		if(user!=null){
			return new ResultUtil(501,"昵称已存在，请重新填写！");
		}
		return new ResultUtil(0);
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@SysLog(value="添加用户")
	@RequestMapping("insUser")
	@RequiresPermissions("user:user:save")
	@ResponseBody
	public ResultUtil insUser(UsersEntity user){
		//防止浏览器提交
		UsersEntity u1 = userServiceImpl.selUserByEmail(user.geteMail(),null);
		UsersEntity u2 = userServiceImpl.selUserByNickname(user.getNickname(),null);
		if(u1!=null){
			return new ResultUtil(500,"邮箱已存在，请重新填写！");
		}
		if(u2!=null){
			return new ResultUtil(501,"昵称已存在，请重新填写！");
		}
		try {
			userServiceImpl.insUserService(user);
			return ResultUtil.ok();
		} catch (Exception e) {
			//e.printStackTrace();
			return new ResultUtil(502,"邮件发送错误，请检查邮箱！");
		}
	}

	/**
	 * 添加用户
	 * @param file
	 * @return
	 */
	@SysLog(value="添加用户")
	@RequestMapping("insBatchUser")
	@RequiresPermissions("user:user:save")
	@ResponseBody
	public ResultUtil insBatchUser(MultipartFile file){
		//	开始执行 Excel 解析
		//  将上传到的MultipartFile转为输入流，然后交给POI去解析即可。
		//	1、创建 HSSFWorkbook 对象
		try {
			log.debug("接收到前台传来的 Excel 文件，" + file.getOriginalFilename());
			HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
			// 2、获取一共有多少 sheet ，然后遍历
			int numberOfSheets = workbook.getNumberOfSheets();
			log.debug("成功读取到文件中包含\t"+numberOfSheets+"\t个 Sheet 表格。");
			for (int i = 0; i < numberOfSheets; i++) {
				log.debug("正在执行读取第\t"+i+"/"+numberOfSheets+"\t个 Sheet 表格的内容。");
				HSSFSheet sheet = workbook.getSheetAt(i);
				//...
				//	3、获取 sheet 中一共有多少行，遍历行（ 注意第一行是标题 ）
				int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
				log.debug("成功读取到文件中包含\t"+numberOfSheets+"\t个 Sheet 表格。");
				//  只设置实体类，不开辟堆空间
				for (int j = 0; j < physicalNumberOfRows; j++) {
					if (j == 0) {
						continue;	//	标题行	每个 Sheet 中的第一行应该设置为 标题行。
					}
					HSSFRow row = sheet.getRow(j);
					// 4、获取每一行有多少单元格，遍历单元格
					int physicalNumberOfCells = row.getPhysicalNumberOfCells();
					//  这个位置确定有元素后，开始 new 实体类。开辟堆空间。
					for (int k = 0; k < physicalNumberOfCells; k++) {
						HSSFCell cell = row.getCell(k);
						//...
					}
				}
			}
		} catch (IOException e) {
			log.debug("请检查文件，file.getInputStream()出现异常。");
			e.printStackTrace();
			return new ResultUtil(500,"请求的文件出现异常，请您检查！");
		}
		return null;
	}
	
	@RequestMapping("getUserList")
	@RequiresPermissions("user:user:list")
	@ResponseBody
	public ResultUtil getUserList(Integer page, Integer limit, UserSearch search){
		return userServiceImpl.selUsers(page,limit,search);
	}
	
	/**
	 * 批量删除用户
	 * @param userStr
	 * @return
	 */
	@SysLog(value="批量删除用户")
	@RequestMapping("delUsers/{userStr}")
	@RequiresPermissions("user:user:delete")
	@ResponseBody
	public ResultUtil delUsers(@PathVariable("userStr")String userStr){
		try {
			userServiceImpl.delUsersService(userStr);
			return ResultUtil.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error();
		}
	}
	
	/**
	 * 根据ID删除用户
	 * @param uid
	 * @return
	 */
	@SysLog(value="根据ID删除用户")
	@RequestMapping("delUserByUid/{uid}")
	@RequiresPermissions("user:user:delete")
	@ResponseBody
	public ResultUtil delUserByUid(@PathVariable("uid")String uid){
		try {
			userServiceImpl.delUserByUid(uid);;
			return ResultUtil.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error();
		}
	}
	
	@RequestMapping("editUser/{uid}")
	@RequiresPermissions("user:user:save")
	public String editUser(@PathVariable("uid")String uid,Model model){
		UsersEntity user=userServiceImpl.selUserByUid(Long.parseLong(uid));
		model.addAttribute("user", user);
		return "page/user/editUser";
	}
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	@SysLog(value="更新用户信息")
	@RequestMapping("updUser")
	@RequiresPermissions("user:user:update")
	@ResponseBody
	public ResultUtil updUser(UsersEntity user){
		try {
			userServiceImpl.updUserService(user);
			return ResultUtil.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error();
		}
	}
}
