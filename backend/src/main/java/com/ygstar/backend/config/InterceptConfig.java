package com.ygstar.backend.config;


import com.ygstar.backend.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        registry.addInterceptor(new JwtInterceptor())
                //拦截的路径 需要进行token验证的路径
                .addPathPatterns("")
                //放行的路径
                .excludePathPatterns("/sys/user/login", "/swagger-ui.html", "/swagger-resources/**","/sys/file/**","/sys/admin/login");
                //先放行所有接口以便开发，后期再改
    }
}
