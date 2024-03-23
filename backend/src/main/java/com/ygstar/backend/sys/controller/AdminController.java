package com.ygstar.backend.sys.controller;

import com.ygstar.backend.sys.dao.AdminVenueDAO;
import com.ygstar.backend.sys.dto.AdminRequestDTO;
import com.ygstar.backend.sys.entity.Admin;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IAdminService;
import com.ygstar.backend.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@RestController
@RequestMapping("/sys/admin")
@CrossOrigin
public class AdminController {
    @Autowired
    private IAdminService adminService;
    @GetMapping("/login")
    public Map<String,Object> login(Admin admin){
        System.out.println(admin.getUsername());
            Map<String,Object> map=new HashMap<>();
        try{
            Admin login = adminService.login(admin);
            Map<String, String> payload = new HashMap<>();
            payload.put("username", String.valueOf(login.getUsername()));
            payload.put("userPassword", login.getUserPassword());
            // 生成token
            String token = JwtUtil.getToken(payload);

            map.put("status", true);
            map.put("msg", "登录成功");
            map.put("permission", login.getPermission());
            map.put("venueId", login.getVenueId());
            map.put("token", token); // 响应token，存储在客户端

            System.out.println(token);
        }catch (Exception e) {
            map.put("status", false);
            map.put("msg", "登录失败");
        }

        return map;
    }

    @GetMapping("/getAllAdmin")
    @ApiOperation("获取所有（场馆）管理员信息")
    public PageBean getAllAdmin(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return adminService.getAllAdmin(page, pageSize);
    }

    @PostMapping("/addAdmin")
    @ApiOperation("新建管理员")
    public void addAdmin(@RequestBody AdminRequestDTO adminRequestDTO) {
        // 这里可以进行一些请求参数验证等操作
        // 调用 Service 层方法添加 Admin
        adminService.newAdmin(adminRequestDTO.getUsername(), adminRequestDTO.getUserPassword(), adminRequestDTO.getVenueId());
    }

    @PutMapping("/updatePermission")
    @ApiOperation("修改管理员权限（冻结或解冻）")
    public void updatePermission( @RequestBody Admin admin) {
        if (admin.getPermission()!=2){
            adminService.deleteAdmin(admin.getUsername());
        }else{
            adminService.regainAdmin(admin.getUsername());
        }
    }





}
