package zool.firefly.service;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import java.util.Map;

public interface ShiroService {
    public Map<String, String> loadFilterChainDefinitions();

    public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean);
}

