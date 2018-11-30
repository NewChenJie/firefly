package zool.firefly.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import zool.firefly.domain.entity.shiro.User;
import zool.firefly.mapper.shiro.UserMapper;



/**
 * 登录次数限制
 */
@Slf4j
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    public static final String DEFAULT_RETRYLIMIT_CACHE_KEY_PREFIX = "shiro:cache:retrylimit:";
    private String keyPrefix = DEFAULT_RETRYLIMIT_CACHE_KEY_PREFIX;
    private static final Integer LOCK = 1;
    private static final Integer NORMAL = 0;
    private static final Integer LOCK_NUM = 5;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisManager;

    public RetryLimitHashedCredentialsMatcher() {

    }

    private String getRedisKickoutKey(String username) {
        return this.keyPrefix + username;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //获取用户名
        String username = (String)token.getPrincipal();
        //获取用户登录次数
        Integer retryCount = NORMAL;
        if (redisManager.hasKey(getRedisKickoutKey(username))){
            retryCount = Integer.valueOf((String) redisManager.opsForValue().get(getRedisKickoutKey(username)));
        }
        if (retryCount > LOCK_NUM) {
            //如果用户登陆失败次数大于5次 抛出锁定用户异常  并修改数据库字段
            User user = userMapper.findUserByUserName(username);
            if (user != null && user.getState().equals(NORMAL)) {
                //数据库字段 默认为 0  就是正常状态 所以 要改为1
                //修改数据库的状态字段为锁定
                user.setState(LOCK);
                userMapper.updateByPrimaryKey(user);
            }
            log.info("---------------------锁定用户" + user.getUsername()+"--------------------------");
            //抛出用户锁定异常
            throw new LockedAccountException();
        }
        //判断用户账号和密码是否正确
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //如果正确,从缓存中将用户登录计数 清除
            redisManager.delete(getRedisKickoutKey(username));
        }else{
            redisManager.opsForValue().increment(getRedisKickoutKey(username),1);
        }
        return matches;
    }

    /**
     * 根据用户名 解锁用户
     * @param username
     * @return
     */
    public void unlockAccount(String username){
        User user = userMapper.findUserByUserName(username);
        if (user != null){
            //修改数据库的状态字段为锁定
            user.setState(NORMAL);
            userMapper.updateByPrimaryKey(user);
            redisManager.delete(getRedisKickoutKey(username));
        }
    }


}
