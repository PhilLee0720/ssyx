package com.lee.ssxy.acl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lee.ssxy.model.acl.Role;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapper extends BaseMapper<Role> {
}
