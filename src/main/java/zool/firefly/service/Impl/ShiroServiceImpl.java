package zool.firefly.service.Impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import zool.firefly.domain.entity.shiro.Authority;
import zool.firefly.mapper.shiro.AuthorityMapper;
import zool.firefly.service.ShiroService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShiroServiceImpl implements ShiroService{

    @Autowired
    private AuthorityMapper authorityMapper;

    /**
     * 初始化权限 anon（匿名）、logout（登出）、authc（认证）
     */
    @Override
    public Map<String, String> loadFilterChainDefinitions() {
        List<Authority> authorities = authorityMapper.selectAll();
        // 权限控制map.从数据库获取
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(authorities)){
            for (Authority authority : authorities) {
                if (StringUtils.isEmpty(authority.getPermission())) {
                    continue;
                }
                String uris = authority.getUri();
                List<String> uriList = Lists.newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(uris));
                uriList.forEach(x-> filterChainDefinitionMap.put(x, authority.getPermission()));
            }
        }
        filterChainDefinitionMap.put("/user/no/login", "anon");
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/user/logout", "logout");
        filterChainDefinitionMap.put("/test/*", "anon");
//        filterChainDefinitionMap.put("/redis/*", "roles[admin]");
        filterChainDefinitionMap.put("/**", "anon");
        return filterChainDefinitionMap;
    }

    /**
     * 在对角色进行增删改操作时，需要调用此方法进行动态刷新
     * @param shiroFilterFactoryBean
     */
    @Override
    public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        synchronized (this) {
            AbstractShiroFilter shiroFilter;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
            // 清空老的权限控制,设置新的
            manager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                manager.createChain(url, chainDefinition);
            }
        }
    }
}
