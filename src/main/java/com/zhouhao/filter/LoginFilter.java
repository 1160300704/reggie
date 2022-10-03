package com.zhouhao.filter;

import com.alibaba.fastjson.JSON;
import com.zhouhao.common.BaseContext;
import com.zhouhao.common.R;
import com.zhouhao.entity.Employee;
import com.zhouhao.entity.User;
import org.springframework.util.AntPathMatcher;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    private String[] excludedUris = {"/backend/**",
            "/employee/login",
            "/employee/logout",
            "/front/**",
            "/user/login",
            "/user/sendMsg"};
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpSession session = request.getSession();
        Employee employee = (Employee)session.getAttribute("employee");
        User user = (User)session.getAttribute("user");

        //放行资源
        for(String uri: excludedUris){
            if(PATH_MATCHER.match(uri, request.getRequestURI())){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        if(employee == null && user == null){
//            System.out.println("进来了");
//            response.sendRedirect("/backend/page/login/login.html");  ajax不能重定向
            response.getWriter().print(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        }

        //登录成功的例外
        if(employee != null){
            BaseContext.set(employee.getId());
        }
        if(user != null){
            BaseContext.set(user.getId());
        }
        filterChain.doFilter(servletRequest, servletResponse);
        BaseContext.remove();
    }
}