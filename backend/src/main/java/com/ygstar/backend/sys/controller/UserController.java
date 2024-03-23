package com.ygstar.backend.sys.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ygstar.backend.sys.dto.UserDTO;
import com.ygstar.backend.sys.entity.User;
import com.ygstar.backend.sys.mapper.UserMapper;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IUserService;
import com.ygstar.backend.util.JwtUtil;
import com.ygstar.backend.util.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@RestController
@RequestMapping("/sys/user")
@CrossOrigin

public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private UserMapper userMapper;
    // 邮箱验证码缓存
    Cache<String, String> localCache = CacheBuilder
            .newBuilder().maximumSize(1000)
            .expireAfterAccess(5, TimeUnit.MINUTES).build();
    @GetMapping("/login")
    public Map<String,Object> login(User user){
        Map<String,Object> map=new HashMap<>();
        try{
            User login = userService.login(user);
            Map<String, String> payload = new HashMap<>();
            payload.put("userMail", String.valueOf(login.getUserMail()));
            payload.put("password", login.getPassword());
            // 生成token
            String token = JwtUtil.getToken(payload);

            map.put("status", true);
            map.put("msg", "登录成功");
            map.put("token", token); // 响应token，存储在客户端
        }catch (Exception e) {
            map.put("status", false);
            map.put("msg", "登录失败");
        }

        return map;
    }
    @PostMapping("/test")
    public Map<String, Object> test(HttpServletRequest request){
        String token = request.getHeader("token");
        DecodedJWT verify = JwtUtil.verify(token);
        String userMail = verify.getClaim("userMail").asString();
        String password = verify.getClaim("password").asString();

        Map<String,Object> map=new HashMap<>();
        map.put("status", true);
        map.put("msg", "请求成功");
        return map;
    }

    @GetMapping("/sendEmail")
    public Response<Integer> sendEmail(String userMail){
        Response<Integer> response = new Response<>();
        if (userMail!=null)System.out.println("not null");
        User foundUser=userMapper.selectOne(new QueryWrapper<User>().eq("user_mail", userMail));
        if(foundUser!=null){
            response.setCode(300);
            response.setDesc("邮箱已经被注册");
            return response;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        Random random = new Random();
        int emailCode = random.nextInt(899999)+100000;
        message.setFrom("756044528@qq.com");
        message.setTo(userMail);
        message.setSubject("秧歌Star验证码");
        message.setText("邮箱验证码为："+emailCode+",请勿发送给他人");

        try{
            mailSender.send(message);
            response.setCode(200);
            response.setDesc("验证码发送成功");
            localCache.put(userMail,Integer.toString(emailCode));
        }catch (Exception e){
            response.setCode(500);
            response.setDesc("验证码发送异常");
            e.printStackTrace();
        }
        return response;
    }

    @PostMapping("/register")
    public Response<Integer> register(@RequestBody UserDTO userDTO){
        Response<Integer> response = new Response<>();
        String emailCode = localCache.getIfPresent(userDTO.getUserMail());
        if(emailCode==null){
            response.setCode(300);
            response.setDesc("验证证码失效");
        } else if(!emailCode.equals(userDTO.getEmailCode())){
            System.out.println(userDTO.getEmailCode());
            response.setCode(300);
            response.setDesc("验证证码错误");
        } else if(!userDTO.getPassword().equals(userDTO.getConfirmKey())){
            response.setCode(300);
            response.setDesc("两次密码输入不一致");
        } else if(!userService.register(userDTO)){
            response.setCode(500);
            response.setDesc("注册失败");
        } else{
            response.setCode(200);
            response.setDesc("注册成功");
        }
        return response;
    }

    @GetMapping("/updateUserName")
    @ApiOperation(value = "更新用户名")
    public ResponseEntity<String> updateUserName(String newUserName, String userMail){
        try{
            userService.updateUserName(newUserName,userMail);
            return ResponseEntity.status(200).body("更新成功");
        }catch (Exception e){
            return ResponseEntity.status(500).body("更新失败");
        }
    }

    @GetMapping("/getUserInfo")
    @ApiOperation(value = "获取用户信息")
    public ResponseEntity<User> getUserInfo(String userMail){
        try{
            return ResponseEntity.status(200).body(userService.getUserInfo(userMail));
        }catch (Exception e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/updateUserPhoneNumber")
    @ApiOperation(value = "更新用户手机号")
    public ResponseEntity<String> updateUserPhoneNumber(String newPhoneNumber, String userMail){
        try{
            userService.updateUserPhoneNumber(newPhoneNumber,userMail);
            return ResponseEntity.status(200).body("更新成功");
        }catch (Exception e){
            return ResponseEntity.status(500).body("更新失败");
        }
    }

    @GetMapping("/getBanedUser")
    @ApiOperation("获取在黑名单中的用户")
    public PageBean getBanedUser(Integer page, Integer pageSize){
        return userService.getBanedUser(page,pageSize);
    }
    @PutMapping("/updateUserStatus")
    @ApiOperation("将用户解除黑名单")
    public void updateUserStatus(@RequestBody Map<String, String> requestBody){
        String userMail = requestBody.get("userMail");
        System.out.println("userMail is:"+ userMail);
        userService.updateUserStatus(userMail);
    }
}
