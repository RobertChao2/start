package cn.chao.maven.shiro;

import cn.chao.maven.entity.AdminEntity;
import cn.chao.maven.entity.AdminEntityExample;
import cn.chao.maven.entity.MenusEntity;
import cn.chao.maven.mapper.AdminEntityMapper;
import cn.chao.maven.mapper.AdminMenusMapper;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Log4j
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private AdminEntityMapper tbAdminMapper;
    @Autowired
    private AdminMenusMapper adminMenusMapper;

    public MyShiroRealm() {
        log.debug("MyShiroRealm is reading .");
    }

    @Override
    public String getName() {
        return "MyShiroRealm";
    }

    /**
     * 用户的授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("doGetAuthorizationInfo :" + principalCollection.getPrimaryPrincipal());
        System.out.println("doGetAuthorizationInfo :" + principalCollection.getRealmNames());
        log.debug("用户的授权 ===============================");
        AdminEntity admin = (AdminEntity) principalCollection.getPrimaryPrincipal();
        Long roleId = admin.getRoleId();

        List<String> permsList = null;

        // 系统管理员，拥有最高权限
        List<MenusEntity> menuList = adminMenusMapper.getMenus(roleId);
        permsList = new ArrayList<>(menuList.size());
        for (MenusEntity menu : menuList) {
            if (menu.getPerms() != null && !"".equals(menu.getPerms())) {
                permsList.add(menu.getPerms());
            }
        }

        // 用户权限列表
        Set<String> permsSet = new HashSet<String>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 用户登录的身份认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("doGetAuthenticationInfo :" + authenticationToken.getPrincipal());
        System.out.println("doGetAuthenticationInfo :" + authenticationToken.getCredentials());
        log.debug("用户的身份认证 ======================================");

        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());

        // 查询用户信息
        AdminEntityExample example = new AdminEntityExample();
        AdminEntityExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<AdminEntity> admins = tbAdminMapper.selectByExample(example);
        // 账号不存在
        if (admins == null || admins.size() == 0) {
            throw new UnknownAccountException("账号不存在!");
        }
        password = new Md5Hash(password).toString();
        // 密码错误
        if (!password.equals(admins.get(0).getPassword())) {
            throw new IncorrectCredentialsException("账号或密码不正确!");
        }

        // 账号未分配角色
        if (admins.get(0).getRoleId() == null || admins.get(0).getRoleId() == 0) {
            throw new UnknownAccountException("账号未分配角色!");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(admins.get(0), password, getName());
        return info;
    }
}
