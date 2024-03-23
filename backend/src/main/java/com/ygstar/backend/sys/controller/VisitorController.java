package com.ygstar.backend.sys.controller;

import com.ygstar.backend.sys.entity.Visitor;
import com.ygstar.backend.sys.service.IVisitorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/sys/visitor")
public class VisitorController {
    @Autowired
    IVisitorService visitorService;

    @GetMapping("/getVisitorByUserMail")
    public List<Visitor> getVisitorByUserMail(String userMail){
        return visitorService.getVisitorByUserMail(userMail);
    }

    @GetMapping("/insertVisitor")
    @ApiOperation(value = "增加新的游客")
    public void insertVisitor(String userMail, String visitorName, String idCardNumber){
        if(visitorService.isVisitorExist(idCardNumber,userMail)!=0){
            visitorService.realiveVisitor(idCardNumber,userMail);
            return;
        }
        visitorService.insertVisitor(userMail,visitorName,idCardNumber);
    }

    @GetMapping("/deleteVisitor")
    @ApiOperation(value = "删除游客")
    public void deleteVisitor(String idCardNumber,String userMail){
        visitorService.deleteVisitor(idCardNumber,userMail);
    }

    @GetMapping("/isVisitorExist")
    @ApiOperation(value = "查看游客是否存在")
    public Integer isVisitorExist(String idCardNumber,String userMail){
        return visitorService.isVisitorExist(idCardNumber,userMail);
    }

    @GetMapping("/realiveVisitor")
    @ApiOperation(value = "更新游客delete_flag为0")
    public void realiveVisitor(String idCardNumber,String userMail){
        visitorService.realiveVisitor(idCardNumber,userMail);
    }
}
