package com.ygstar.backend.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygstar.backend.sys.entity.User;
import com.ygstar.backend.sys.mapper.UserMapper;
import com.ygstar.backend.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
// 原来的邮箱验证码发送类，现在被集成到user接口
@RestController
@Slf4j
public class SendMailController {




}
