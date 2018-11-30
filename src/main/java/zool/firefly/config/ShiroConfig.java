package zool.firefly.config;





import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import zool.firefly.constant.RedisKey;
import zool.firefly.service.ShiroService;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private static final String REMEMBER_ME_COOKIE = "remenbermeCookie";
    private static final String COOKIE_NAME = "shiro:cookie:";
    private static final long SESSION_TIME=1000*60*30;


    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    /**
     * shiro管理生命周期的东西
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public  LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * anon（匿名）、logout（登出）、authc（认证）
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, ShiroService shiroService) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/user/no/login");
        shiroFilter.setSuccessUrl("/user/success");
        shiroFilter.setUnauthorizedUrl("/user/fail");
        Map<String, Filter> filterMap = new LinkedHashMap<>(1);
        //自定义角色过滤器
        filterMap.put("roles", rolesAuthorizationFilter());
        shiroFilter.setFilters(filterMap);
        // 过滤链可以使用注解形式实现
        shiroFilter.setFilterChainDefinitionMap(shiroService.loadFilterChainDefinitions());
        return shiroFilter;
    }

    /**
     * 自定义角色过滤器
     */
    @Bean
    public CustomRolesAuthorizationFilter rolesAuthorizationFilter() {
        return new CustomRolesAuthorizationFilter();
    }


    /**
     * 安全管理模块，所有的manager在此配置
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("shiroRedisTemplate")RedisTemplate redisTemplate) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //自定义realm
        securityManager.setRealm(myShiroRealm(redisTemplate));

        //自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager(redisTemplate));

        //自定义缓存实现 使用redis
        securityManager.setCacheManager(redisCacheManager());

        //注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());

        return securityManager;
    }

    /**
     * 领域
     */
    @Bean(name = "myShiroRealm")
    @DependsOn(value = {"lifecycleBeanPostProcessor", "ShiroRedisCacheManager"})
    public MyShiroRealm myShiroRealm(@Qualifier("shiroRedisTemplate")RedisTemplate redisTemplate) {
        MyShiroRealm shiroRealm = new MyShiroRealm();
        //设置缓存管理器
        shiroRealm.setCacheManager(redisCacheManager());
        shiroRealm.setCachingEnabled(true);
        //设置认证密码算法及迭代复杂度
        shiroRealm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher());
        //认证
        shiroRealm.setAuthenticationCachingEnabled(true);
        //授权
        shiroRealm.setAuthorizationCachingEnabled(true);
        return shiroRealm;
    }

    /**
     * 自定义配置密码比较器
     *
     */
    @Bean("credentialsMatcher")
    public RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher(){
        RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher();
        //如果密码加密,可以打开下面配置
        //加密算法的名称
        retryLimitHashedCredentialsMatcher.setHashAlgorithmName("md5");
        //配置加密的次数
        retryLimitHashedCredentialsMatcher.setHashIterations(2);
        //是否存储为16进制
        retryLimitHashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return retryLimitHashedCredentialsMatcher;
    }


    /**
     * 缓存管理器的配置
     */
    @Bean(name = "ShiroRedisCacheManager")
    public ShiroRedisCacheManager redisCacheManager() {
        ShiroRedisCacheManager redisCacheManager = new ShiroRedisCacheManager();
        redisCacheManager.createCache(RedisKey.CACHE_KEY);
        return redisCacheManager;
    }

    /**
     *  配置sessionmanager，由redis存储数据
     */
    @Bean(name = "sessionManager")
    @DependsOn(value = "lifecycleBeanPostProcessor")
    public ShiroSessionManager sessionManager(@Qualifier("shiroRedisTemplate") RedisTemplate redisTemplate) {
        //自定义sessionManage解决单次请求需要多次访问redis
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        //redisSessionDao
        MyRedisSessionDao redisSessionDao = new MyRedisSessionDao(SESSION_TIME,redisTemplate);
        sessionManager.setSessionDAO(redisSessionDao);
        //删除无效会话
        sessionManager.setDeleteInvalidSessions(true);
        //设置对应的cookie
        SimpleCookie cookie = new SimpleCookie();
        cookie.setName(COOKIE_NAME);
        cookie.setMaxAge(180000);
        sessionManager.setSessionIdCookie(cookie);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    /**
     *
     * remenberMeCookie是一个实现了将用户名保存在客户端的一个cookie，与登陆时的cookie是两个simpleCookie。
     * 登陆时会根据权限去匹配，如是user权限，则不会先去认证模块认证，而是先去搜索cookie中是否有rememberMeCookie，
     * 如果存在该cookie，则可以绕过认证模块，直接寻找授权模块获取角色权限信息。
     * 如果权限是authc,则仍会跳转到登陆页面去进行登陆认证.
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie(REMEMBER_ME_COOKIE);
        simpleCookie.setHttpOnly(true);
        //30天
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    /**
     * cookie管理对象;记住我功能
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥  默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("6ZmI6I2j5Y+R5aSn5ZOlAA=="));
        return cookieRememberMeManager;
    }




}
