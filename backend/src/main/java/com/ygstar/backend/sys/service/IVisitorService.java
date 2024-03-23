package com.ygstar.backend.sys.service;

import com.ygstar.backend.sys.entity.Visitor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
public interface IVisitorService extends IService<Visitor> {
    List<Visitor> getVisitorByUserMail(String userMail);

    void insertVisitor(String userMail, String visitorName, String idCardNumber);
    void deleteVisitor(String idCardNumber,String userMail);

    //查看游客是否存在
    Integer isVisitorExist(String idCardNumber,String userMail);

    //更新游客delete_flag为0
    void realiveVisitor(String idCardNumber,String userMail);
}
