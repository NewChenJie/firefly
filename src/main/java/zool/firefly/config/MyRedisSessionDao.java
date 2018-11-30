package zool.firefly.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;
import zool.firefly.constant.RedisKey;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MyRedisSessionDao extends AbstractSessionDAO {
    /**
     *Session超时时间，单位为毫秒
     */
    private long expireTime = 120000;


    private RedisTemplate redisTemplate;

    public MyRedisSessionDao() {
        super();
    }

    public MyRedisSessionDao(long expireTime, RedisTemplate redisTemplate) {
        super();
        this.expireTime = expireTime;
        this.redisTemplate = redisTemplate;
    }




    @Override
    public void update(Session session) throws UnknownSessionException {
        System.out.println("===============update================");
        if (session == null || session.getId() == null) {
            return;
        }
        session.setTimeout(expireTime);
        redisTemplate.opsForValue().set(RedisKey.SESSION_PREFIX+session.getId(), session, expireTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public void delete(Session session) {
        System.out.println("===============delete================");
        if (null == session) {
            return;
        }
        redisTemplate.opsForValue().getOperations().delete(RedisKey.SESSION_PREFIX+session.getId());
    }

    /**
     *  获取活跃的session，可以用来统计在线人数，如果要实现这个功能，可以在将session加入redis时指定一个session前缀，
     * 统计的时候则使用keys("session-prefix*")的方式来模糊查找redis中所有的session集合
     */
    @Override
    public Collection<Session> getActiveSessions() {
        System.out.println("==============getActiveSessions=================");
        Set<String> keys = redisTemplate.keys(RedisKey.SESSION_PREFIX + "*");
        List<Session> sessions = Lists.newArrayList();
        for (String key : keys) {
            sessions.add((Session) redisTemplate.opsForValue().get(key));
        }
        return sessions;
    }

    @Override
    protected Serializable doCreate(Session session) {
        System.out.println("===============doCreate================");
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        redisTemplate.opsForValue().set(RedisKey.SESSION_PREFIX+session.getId(), session, expireTime, TimeUnit.MILLISECONDS);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("==============doReadSession=================");
        if (sessionId == null) {
            return null;
        }
        return (Session) redisTemplate.opsForValue().get(RedisKey.SESSION_PREFIX+sessionId);
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


}
