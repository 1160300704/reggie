package com.zhouhao.config;

import com.zhouhao.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringWebMVCConfig implements WebMvcConfigurer {
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        //创建一个注册过滤器对象
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        //设置自定义过滤器
        registrationBean.setFilter(new LoginFilter());
        //设置过滤拦截匹配规则,/*是匹配所有
        registrationBean.addUrlPatterns("/*");
        //存在多个过滤器时，设置执行顺序，值越大，执行顺序越靠后
        registrationBean.setOrder(1);
        //返回这个注册过滤器对象
        return registrationBean;
    }
}
