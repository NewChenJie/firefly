package zool.firefly.mapper.shiro;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import zool.firefly.domain.entity.shiro.Role;
@Repository
public interface RoleMapper extends Mapper<Role> {
}