package com.xmut.shiro;

import com.xmut.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * @description: 自定义Realm
 * @author: whf
 * @date:
 */
public class UserRealm extends AuthorizingRealm {
    
    /*
     * @Description: 执行授权逻辑
     * @Author: whf
     * @Date: 2019/8/14
     * @Param: [principalCollection]
     * @Return: org.apache.shiro.authz.AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权逻辑");

        // 给资源授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 获取数据库中的授权字符串
        Subject subject = SecurityUtils.getSubject();
        // 这里的subject.getPrincipal()是获取认证逻辑方法里的SimpleAuthenticationInfo的第一个参数
        User user = (User) subject.getPrincipal();

        // 添加资源的授权字符串
        info.addStringPermission(user.getPerms());

        return info;
    }

    /*
     * @Description: 执行认证逻辑
     * @Author: whf
     * @Date: 2019/8/14
     * @Param: [authenticationToken]
     * @Return: org.apache.shiro.authc.AuthenticationInfo
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行认证逻辑");

        // 假设数据库的用户名和密码
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123");
        user.setPerms("user:add");

        // 编写shiro判断逻辑
        // 1. 判断用户名
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        if (!token.getUsername().equals(user.getUsername())) {
            // 用户名不存在
            return null; // shiro底层会抛出UnknownAccountException
        }
        // 2. 判断密码
        return new SimpleAuthenticationInfo(user, user.getPassword(), "");
    }
}
