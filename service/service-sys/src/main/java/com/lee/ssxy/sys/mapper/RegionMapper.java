package com.lee.ssxy.sys.mapper;

import com.lee.ssxy.model.sys.Region;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * <p>
 * 地区表 Mapper 接口
 * </p>
 *
 * @author phil
 * @since 2023-06-13
 */
@Mapper
public interface RegionMapper extends BaseMapper<Region> {

}
