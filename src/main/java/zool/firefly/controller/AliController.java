package zool.firefly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zool.firefly.service.AliService;

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

    @RequestMapping("/customer")
    public List selectAliCustomer(String name){

        return aliService.selectAliCustomer(name);

    }

}
