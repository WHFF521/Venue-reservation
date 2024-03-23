package com.ygstar.backend.sys.mapper;

import com.ygstar.backend.sys.entity.Visitor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@Mapper
public interface VisitorMapper extends BaseMapper<Visitor> {
    List<Visitor> getVisitorByUserMail(@Param("userMail") String userMail);

    //增加新的游客
    void insertVisitor(@Param("userMail") String userMail, @Param("visitorName") String visitorName, @Param("idCardNumber") String idCardNumber);

    //删除游客
    void deleteVisitor(@Param("idCardNumber") String idCardNumber,@Param("userMail") String userMail);

    //查看游客是否存在
    Integer isVisitorExist(@Param("idCardNumber") String idCardNumber,@Param("userMail") String userMail);

    //更新游客delete_flag为0
    void realiveVisitor(@Param("idCardNumber") String idCardNumber,@Param("userMail") String userMail);
}
