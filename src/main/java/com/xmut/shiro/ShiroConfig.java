package com.xmut.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.jboss.logging.Param;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: Shiro配置类，创建三个对象
 * @author: whf
 * @date: 2019-8-14 10:38:24
 */
@Configuration
public class ShiroConfig {

    /*
     * @Description: 创建ShiroFilterFactoryBean
     * @Author: whf
     * @Date: 2019/8/14
     * @Param: [defaultSecurityManager]
     * @Return: org.apache.shiro.spring.web.ShiroFilterFactoryBean
     */
    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        /*
         *  添加内置过滤器
         *      常用过滤器：
         *          anon: 无需认证（登录）可以访问
         *          authc: 必须认证才能访问
         *          user: 如果使用remberMe的功能可以直接访问
         *          perms: 该资源必须得到资源权限才可以访问
         *          role: 该资源必须得到角色权限才可以访问
         */
        Map<String, String> filterMap = new LinkedHashMap<String, String>();
        // 注意添加的顺序，要把放行的放在上面，因为LinkedHashMap是从上到下执行，优先匹配第一个
        filterMap.put("/testThymeleaf", "anon");
        filterMap.put("/login", "anon");
        // 授权过滤器
        filterMap.put("/add", "perms[user:add]");
        filterMap.put("/update", "perms[user:update]");

        filterMap.put("/*", "authc");

        // 修改跳转页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        // 设置未授权提示页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    /*
     * @Description: 创建DefaultWebSecurityManager
     * @Author: whf
     * @Date: 2019/8/14
     * @Param: [userRealm]
     * @Return: org.apache.shiro.mgt.DefaultSecurityManager
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 关联Realm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    /*
     * @Description: 创建Reaml
     * @Author: whf
     * @Date: 2019/8/14
     * @Param: []
     * @Return: com.xmut.shiro.UserRealm
     */
    @Bean(name = "userRealm")
    public UserRealm getRealm() {
        return new UserRealm();
    }

    /*
     * @Description: 配置ShiroDialect，用于thymeleaf和shiro标签配合使用
     * @Author: whf
     * @Date: 2019/8/14
     * @Param: []
     * @Return: at.pollux.thymeleaf.shiro.dialect.ShiroDialect
     */
    @Bean(name = "shiroDialect")
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }

}
