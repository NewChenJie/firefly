package zool.firefly.config.durid;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class DruidStateViewServlet
 */
@WebServlet(
        urlPatterns= {"/druid/*"},
        initParams= {
                @WebInitParam(name="allow",value="*"),
                @WebInitParam(name="loginUsername",value="admin"),
                @WebInitParam(name="loginPassword",value="123456"),
                @WebInitParam(name="resetEnable",value="true")// 允许HTML页面上的“Reset All”功能

        }
)
public class DruidStatViewServlet extends StatViewServlet implements Servlet {
    private static final long serialVersionUID = 1L;


}
