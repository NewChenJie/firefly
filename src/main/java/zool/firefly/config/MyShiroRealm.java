package zool.firefly.config;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import zool.firefly.domain.entity.shiro.Authority;
import zool.firefly.domain.entity.shiro.Role;
import zool.firefly.domain.entity.shiro.User;
import zool.firefly.mapper.shiro.AuthorityMapper;
import zool.firefly.mapper.shiro.RoleMapper;
import zool.firefly.mapper.shiro.UserMapper;
import zool.firefly.util.ByteSourceUtils;
import zool.firefly.util.MySimpleByteSource;

import java.util.List;


public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthorityMapper authorityMapper;



    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        System.out.println(user.getUsername() + "进行授权操作");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Integer roleId = user.getRoleId();
        Role role = roleMapper.selectByPrimaryKey(roleId);
        info.addRole(role.getRoleName());
        List<Authority> authorities = authorityMapper.selectByRoleId(roleId);
        return CollectionUtils.isEmpty(authorities) ? null : info;
    }


    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        //根据用户名查询密码，由安全管理器负责对比查询出的数据库中的密码和页面输入的密码是否一致
        User user = userMapper.findUserByUserName(username);
        if (user == null) {
            return null;
        }
        if (1==user.getState()) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        }

        String password = user.getPassword();
        //最后的比对需要交给安全管理器,三个参数进行初步的简单认证信息对象的包装,由安全管理器进行包装运行
        return new SimpleAuthenticationInfo(user, password, new MySimpleByteSource(ByteSourceUtils.serialize(user.getUsername())),getName());
    }


}
