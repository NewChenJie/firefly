package zool.firefly.mapper.shiro;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import zool.firefly.domain.entity.shiro.User;
@Repository
public interface UserMapper extends Mapper<User> {
    User findUserByUserName(String username);
}