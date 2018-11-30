package zool.firefly.config;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import zool.firefly.constant.RedisKey;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ShiroRedisCacheManager  extends AbstractCacheManager {

    @Autowired
    @Qualifier("shiroRedisTemplate")
    private RedisTemplate  redisTemplate;

    private static String cachePrefix = RedisKey.CACHE_KEY;

    public ShiroRedisCacheManager(){
    }

    @Override
    protected Cache createCache(String name) throws CacheException {
        return new ShiroRedisCache(120, redisTemplate);
    }

    public static class ShiroRedisCache <K,V> implements Cache<K,V>{
        /**
         * 缓存的超时时间，单位为s
         */
        private long expireTime = 120;

        private RedisTemplate<K, V> redisTemplate;

        public ShiroRedisCache() {
            super();
        }

        public ShiroRedisCache(long expireTime, RedisTemplate<K, V> redisTemplate) {
            this.expireTime = expireTime;
            this.redisTemplate = redisTemplate;
        }
        private K makeKey(K name){
            return (K) (cachePrefix+name);
        }

        /**
         * 通过key来获取对应的缓存对象
         * 通过源码我们可以发现，shiro需要的key的类型为Object，V的类型为AuthorizationInfo对象
         */
        @Override
        public V get(K key) throws CacheException {
            return redisTemplate.opsForValue().get(makeKey(key));
        }

        /**
         * 将权限信息加入缓存中
         */
        @Override
        public V put(K key, V value) throws CacheException {
            redisTemplate.opsForValue().set(makeKey(key), value, this.expireTime, TimeUnit.SECONDS);
            return value;
        }

        /**
         * 将权限信息从缓存中删除
         */
        @Override
        public V remove(K key) throws CacheException {
            V v = redisTemplate.opsForValue().get(makeKey(key));
            redisTemplate.opsForValue().getOperations().delete(makeKey(key));
            return v;
        }

        @Override
        public void clear() throws CacheException {
            redisTemplate.getConnectionFactory().getConnection().flushDb();
        }

        @Override
        public int size() {
            return redisTemplate.getConnectionFactory().getConnection().dbSize().intValue();
        }



        @Override
        public Set<K> keys() {
            Set<K> keys = redisTemplate.keys((K) ( cachePrefix+"*"));
            Set<K> sets = new HashSet<>();
            for (K key : keys) {
                sets.add(key);
            }
            return sets;
        }

        @Override
        public Collection<V> values() {
            Set<K> keys = keys();
            List<V> values = new ArrayList<>(keys.size());
            for(K k :keys){
                values.add(get(k));
            }
            return values;
        }
    }

}
