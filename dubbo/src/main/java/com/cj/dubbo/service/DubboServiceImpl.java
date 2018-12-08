package com.cj.dubbo.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = DubboService.class)
public class DubboServiceImpl implements DubboService {
    @Override
    public String useDubbo() {
        return "just use dubbo tese";
    }
}
