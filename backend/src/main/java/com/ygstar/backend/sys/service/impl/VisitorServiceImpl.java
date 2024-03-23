package com.ygstar.backend.sys.service.impl;

import com.ygstar.backend.sys.entity.Visitor;
import com.ygstar.backend.sys.mapper.VisitorMapper;
import com.ygstar.backend.sys.service.IVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements IVisitorService {
    @Autowired
    VisitorMapper visitorMapper;
    @Override
    public List<Visitor> getVisitorByUserMail(String userMail) {
        return visitorMapper.getVisitorByUserMail(userMail);
    }

    @Override
    public void insertVisitor(String userMail, String visitorName, String idCardNumber) {
        visitorMapper.insertVisitor(userMail,visitorName,idCardNumber);
    }

    @Override
    public void deleteVisitor(String idCardNumber,String userMail) {
        visitorMapper.deleteVisitor(idCardNumber,userMail);
    }

    @Override
    public Integer isVisitorExist(String idCardNumber, String userMail) {
        return visitorMapper.isVisitorExist(idCardNumber,userMail);
    }

    @Override
    public void realiveVisitor(String idCardNumber, String userMail) {
        visitorMapper.realiveVisitor(idCardNumber,userMail);
    }
}
