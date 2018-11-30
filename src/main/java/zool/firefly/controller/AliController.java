package zool.firefly.controller;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zool.firefly.exception.EntityValidException;
import zool.firefly.mapper.AliNewCustomerMapper;
import zool.firefly.util.KeyValue;

/**
 * @author : zoolye
 * @date : 2018-11-03 12:15
 * @describe :
 */
@RestController
@RequestMapping("/ali")
public class AliController {

    @Autowired
    AliNewCustomerMapper aliNewCustomerMapper;

    @RequestMapping("/customer")
    public KeyValue selectAliCustomer(String name,Page page) {
        throw new EntityValidException("test");
    }

}
