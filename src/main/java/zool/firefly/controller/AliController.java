package zool.firefly.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zool.firefly.mapper.AliNewCustomerMapper;
import zool.firefly.service.AliService;
import zool.firefly.util.KeyValue;

import java.util.List;

/**
 * @author : zoolye
 * @date : 2018-11-03 12:15
 * @describe :
 */
@RestController
@RequestMapping("/ali")
public class AliController {

    @Autowired
    AliService aliService;

    @Autowired
    AliNewCustomerMapper aliNewCustomerMapper;

    @RequestMapping("/customer")
    public KeyValue selectAliCustomer(String name,Page page) {
        return KeyValue.ok();
    }

}
