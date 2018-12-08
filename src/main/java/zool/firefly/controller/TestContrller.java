package zool.firefly.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cj.dubbo.service.DubboService;
import org.springframework.amqp.core.AmqpTemplate;
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

//    @Reference
    private DubboService dubboService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @GetMapping("/rabbit")
    public KeyValue rabbit(){
        rabbitTemplate.convertAndSend("base.queue","what?");
        return KeyValue.ok("发送成功");

    }

    @GetMapping("/one")
    public KeyValue one(HttpSession session){
        session.setAttribute("test", new Date());
        return KeyValue.ok("ok");
    }
    @GetMapping("/dubbo")
    public KeyValue dubbo(){
        return KeyValue.ok(dubboService.useDubbo());
    }
}
