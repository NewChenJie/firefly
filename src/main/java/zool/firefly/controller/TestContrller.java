package zool.firefly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zool.firefly.util.KeyValue;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/test")
public class TestContrller {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/one")
    public KeyValue one(HttpSession session){
        session.setAttribute("test", new Date());
        return KeyValue.ok("ok");
    }
}
