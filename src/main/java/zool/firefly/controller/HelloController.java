package zool.firefly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : zoolye
 * @date : 2018-11-03 11:17
 * @describe :
 */
@Controller
public class HelloController {

    //@Value("${my.name}")
    private String name;

    //@Value("${my.age}")
    private Integer age;

    @RequestMapping("/")
    public String index() {
        return "nus";
    }

}
