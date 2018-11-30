package zool.firefly.mapper.shiro;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import zool.firefly.domain.entity.shiro.Authority;

import java.util.List;

@Repository
public interface AuthorityMapper extends Mapper<Authority> {
    List<Authority> selectByRoleId(Integer roleId);
}