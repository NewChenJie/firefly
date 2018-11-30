package zool.firefly.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zool.firefly.domain.entity.shiro.User;
import zool.firefly.util.KeyValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;


@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private DefaultWebSessionManager sessionManager;


    @GetMapping("/login")
    public KeyValue login(@Validated User user, Boolean rememberMe, BindingResult result, HttpServletRequest request){
        String mySessionId = request.getSession().getId();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword());
        token.setRememberMe(rememberMe);
        try {
            SecurityUtils.getSubject().login(token);
            //处理session
            //获取当前已登录的用户session列表(除自己外)
            Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
            User temp;
            for(Session session : sessions) {
                //清除该用户以前登录时保存的session，强制退出
                //用户信息存储的key
                Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                if (attribute == null) {
                    continue;
                }
                //获取对象信息
                temp = (User) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
                if (user.getUsername().equals(temp.getUsername()) && !session.getId().equals(mySessionId)) {
                    sessionManager.getSessionDAO().delete(session);
                }
            }
            return KeyValue.ok("登录成功");
        } catch (IncorrectCredentialsException e) {
            return  KeyValue.err("密码错误");
        } catch (LockedAccountException e) {
            return  KeyValue.err("登录失败，该用户已被冻结");
        } catch (AuthenticationException e) {
            return   KeyValue.err("登录失败，该用户不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return KeyValue.err("操作异常");
        }

    }

    @GetMapping("/no/login")
    public KeyValue noLogin(){
        return KeyValue.forbidden("未登录，请先登录");
    }

    @GetMapping("/fail")
    public KeyValue fail(){
        return KeyValue.err("fail");
    }
    @GetMapping("/success")
    public KeyValue success(){
        return KeyValue.ok("ok");
    }

    @GetMapping("/logout")
    public KeyValue logout(){
        return KeyValue.ok("退出成功");
    }
}
