package zool.firefly.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zoolye
 * @date : 2018-11-03 11:17
 * @describe :
 */
@RestController
public class HelloController {

    //@Value("${my.name}")
    private String name;

    //@Value("${my.age}")
    private Integer age;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/personInfo")
    public String getPersonInfo(){
        return name+" : "+age;
    }

}
