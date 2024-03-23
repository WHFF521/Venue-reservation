package com.ygstar.backend.sys.controller;

import com.ygstar.backend.sys.dao.BanlogUserDAO;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IBanlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@RestController
@RequestMapping("/sys/banlog")
@CrossOrigin
public class BanlogController {
    @Autowired
    private IBanlogService banlogService;
    @GetMapping("/getBanlog" )
    public PageBean getAllBanlog(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")Integer pageSize) {
        return banlogService.getAllBanlog(page,pageSize);
    }
}
