package com.ygstar.backend.interceptor;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygstar.backend.util.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HashMap<String, String> map=new HashMap<>();
        //从http请求头获取token
        String token = request.getHeader("token");
        try {
            //如果验证成功放行请求
            DecodedJWT verify = JwtUtil.verify(token);
            return true;
        }
        catch (Exception exception)
        {
            map.put("msg","验证失败："+exception);
        }
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json:charset=UTF=8");
        response.getWriter().println(json);
        return false;
    }
}
