package com.lee.ssxy.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.user.User;
import com.lee.ssxy.vo.user.LeaderAddressVo;
import com.lee.ssxy.vo.user.UserLoginVo;

public interface UserService extends IService<User>  {
    User getUserByOpenId(String openid);

    LeaderAddressVo getAddressByLeaderId(Long id);

    UserLoginVo getUserLoginVo(Long id);
}
