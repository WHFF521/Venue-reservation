package com.ygstar.backend.sys.mapper;

import com.ygstar.backend.sys.dao.BanlogUserDAO;
import com.ygstar.backend.sys.entity.Banlog;
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
public interface BanlogMapper extends BaseMapper<Banlog> {
    //黑名单列表展示
    List<BanlogUserDAO>getAllBanlog(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize);
    Long countGetAllBanlog();

    @Override
    int insert(Banlog entity);
}
