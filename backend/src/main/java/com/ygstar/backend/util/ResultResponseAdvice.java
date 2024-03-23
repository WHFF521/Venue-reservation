package com.ygstar.backend.util;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//@RestControllerAdvice(basePackages = "com.ygstar.backend.sys.controller")
public class ResultResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType,final Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getGenericParameterType().equals(Response.class);
    }

    @Override
    public Object beforeBodyWrite(final Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if(body == null || body instanceof Response){
            return body;
        }
        final Response<Object> result = new Response<>();
        result.setCode(200);
        result.setDesc("操作成功");
        result.setData(body);
        if(returnType.getGenericParameterType().equals(String.class)){
            ObjectMapper objectMapper = new ObjectMapper();
            try{
                return objectMapper.writeValueAsString(result);
            }catch (JsonProcessingException e){
                throw new RuntimeException("将 Response 对象序列化为 json 字符串时发生异常",e);
            }
        }
        return result;
    }
}
