package com.lee.ssxy.sys.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lee.ssxy.model.sys.Ware;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 仓库表 Mapper 接口
 * </p>
 *
 * @author phil
 * @since 2023-06-13
 */
@Mapper
public interface WareMapper extends BaseMapper<Ware> {

}
