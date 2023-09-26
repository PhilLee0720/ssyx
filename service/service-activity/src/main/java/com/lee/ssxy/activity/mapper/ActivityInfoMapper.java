package com.lee.ssxy.activity.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lee.ssxy.model.activity.ActivityInfo;
import com.lee.ssxy.model.activity.ActivityRule;
import com.lee.ssxy.model.activity.ActivitySku;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 活动表 Mapper 接口
 * </p>
 *
 * @author phil
 * @since 2023-06-22
 */
@Repository
public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {

    public List<Long> selectSkuIdListExist(@Param("skuIdList")List<Long> skuIdList);

    List<ActivityRule> findActivityRule(Long item);

    List<ActivitySku> selectCartActivity(@Param("skuIdList") List<Long> skuIdList);
}
