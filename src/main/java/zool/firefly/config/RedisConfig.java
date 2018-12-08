package zool.firefly.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import redis.clients.jedis.JedisPoolConfig;
import zool.firefly.util.RedisUtil;

import java.lang.reflect.Method;
import java.time.Duration;

@Configuration
@EnableCaching
@EnableConfigurationProperties({RedisSetting.class})
public class RedisConfig extends CachingConfigurerSupport {

    private RedisSetting redisSetting;

    public RedisConfig(RedisSetting redisSetting){
        this.redisSetting = redisSetting;
    }




    /**
     * 自定义生成redis-key
     *
     * @return
     */
    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                StringBuilder sb = new StringBuilder();
                sb.append(o.getClass().getName()).append(".");
                sb.append(method.getName()).append(".");
                for (Object obj : objects) {
                    sb.append(obj.toString());
                }
                System.out.println("keyGenerator=" + sb.toString());
                return sb.toString();
            }
        };
    }



    @Bean
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisSetting.getMaxIdle());
        jedisPoolConfig.setMinIdle(redisSetting.getMinIdle());
        jedisPoolConfig.setMaxTotal(redisSetting.getMaxActive());
        jedisPoolConfig.setMaxWaitMillis(redisSetting.getMaxWait());
        return jedisPoolConfig;
    }


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisSetting.getHost());
        redisStandaloneConfiguration.setDatabase(redisSetting.getDatabase());
        redisStandaloneConfiguration.setPort(redisSetting.getPort());
        JedisClientConfiguration.JedisClientConfigurationBuilder clientConfiguration = JedisClientConfiguration.builder();
        clientConfiguration.connectTimeout(Duration.ofMillis(redisSetting.getTimeout()));
        return  new JedisConnectionFactory(redisStandaloneConfiguration, clientConfiguration.build());
    }

    @Override
    @Bean
    public CacheManager cacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5));
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(jedisConnectionFactory()))
                .cacheDefaults(redisCacheConfiguration).build();
    }


    @Bean(value = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        // 开启事务支持
        redisTemplate.setEnableTransactionSupport(true);
        // 使用String格式序列化缓存键 shiro时 valid报错
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setDefaultSerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        return redisTemplate;
    }

    @Bean(value = "shiroRedisTemplate")
    public RedisTemplate<String, Object> shiroRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        // 开启事务支持
        redisTemplate.setEnableTransactionSupport(true);
        // 使用String格式序列化缓存键 shiro时 valid报错
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setDefaultSerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        return redisTemplate;
    }


    /**
     * 注入工具类
     */
    @Bean(name = "redisUtil")
    public RedisUtil redisUtil(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }

    /**
     * 配置使用lua脚本
     */
    @Bean
    public DefaultRedisScript<Boolean> redisScript() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(redisSetting.getLuaPath())));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

}
