package zool.firefly.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.*;
import org.springframework.cache.annotation.Cacheable;
import zool.firefly.util.KeyValue;
import zool.firefly.util.RedisUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/redis")
@Slf4j
public class RedisController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    DefaultRedisScript<Boolean> redisScript;

    /**
     * 测试redis  lua
     * @return
     */
    @GetMapping("/lua")
    public Object testredislua() {
        String key = "lua";
        redisTemplate.opsForValue().set(key, "source");
        String s = (String) redisTemplate.opsForValue().get(key);
        System.out.println(s);
        redisTemplate.execute(redisScript, Collections.singletonList(key), "source", "target");
        s = (String) redisTemplate.opsForValue().get(key);
        System.out.println(s);
        return null;
    }

    @GetMapping("/pipline")
    public KeyValue testRedis() {
        Long time = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            redisTemplate.opsForValue().increment("pipline", 1);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (int i = 0; i < 10000; i++) {
                redisTemplate.opsForValue().increment("pipline", 1L);
            }
            return null;
        });
        System.out.println("耗时：" + (System.currentTimeMillis() - time));
        return null;
    }

    @GetMapping("/hash")
    public KeyValue testHash(){
       redisTemplate.opsForHash().put("hashkey","name","jack");
       redisTemplate.opsForHash().put("hashkey","sex","man");
       redisTemplate.opsForHash().put("hashkey","age",18);
       redisTemplate.opsForHash().put("hashkey","money",998);
        Map<Object, Object> map = redisTemplate.opsForHash().entries("hashkey");
        System.out.println(map);
        List<Object> list = redisTemplate.opsForHash().values("hashkey");
        System.out.println(list);
        Object o = redisTemplate.opsForHash().get("hashkey", "sex");
        System.out.println(o);
         return null;

    }

    @GetMapping("/list")
    public KeyValue testList(){
        redisTemplate.opsForList().leftPush("list","a");
        redisTemplate.opsForList().leftPush("list","b");
        redisTemplate.opsForList().leftPush("list","c");
        String listValue = redisTemplate.opsForList().index("list",1) + "";
        System.out.println("通过index(K key, long index)方法获取指定位置的值:" + listValue);
        List<Object> list =  redisTemplate.opsForList().range("list",0,-1);
        System.out.println(list);
        return null;
    }

    @GetMapping("/set")
    public KeyValue testSet(){
        redisTemplate.opsForSet().add("setValue","A","B","C","B","D","E","F");
        Set set = redisTemplate.opsForSet().members("setValue");
        System.out.println("通过members(K key)方法获取变量中的元素值:" + set);
        List randomMembers = redisTemplate.opsForSet().randomMembers("setValue",2);
        System.out.println("通过randomMembers(K key, long count)方法随机获取变量中指定个数的元素:" + randomMembers);
        set = redisTemplate.opsForSet().distinctRandomMembers("setValue",2);
        System.out.println("通过distinctRandomMembers(K key, long count)方法获取去重的随机元素:" + set);
        return null;
    }

    @GetMapping("/zset")
    public KeyValue tesetZset(){
        redisTemplate.opsForZSet().add("zSetValue","A",1);
        redisTemplate.opsForZSet().add("zSetValue","B",3);
        redisTemplate.opsForZSet().add("zSetValue","C",2);
        redisTemplate.opsForZSet().add("zSetValue","D",5);
        System.out.println( redisTemplate.opsForZSet().rank("zSetValue", "D"));
        Set zSetValue = redisTemplate.opsForZSet().range("zSetValue",0,-1);
        System.out.println("通过range(K key, long start, long end)方法获取指定区间的元素:" + zSetValue);
        zSetValue = redisTemplate.opsForZSet().rangeByScore("zSetValue",1,5,1,3);
        System.out.println("通过rangeByScore(K key, double min, double max, long offset, long count)方法根据设置的score获取区间值:" + zSetValue);
        Cursor<ZSetOperations.TypedTuple<Object>> cursor = redisTemplate.opsForZSet().scan("zSetValue", ScanOptions.NONE);
        while (cursor.hasNext()) {
            ZSetOperations.TypedTuple<Object> item = cursor.next();
            System.out.println(item.getValue() + ":" + item.getScore());
        }
        return  null;
    }


    @GetMapping("/cache")
    @Cacheable(value = "test")
    public String cache(){
        System.out.println("32222222222222222222222222222");
        return "I am cache";
    }

}