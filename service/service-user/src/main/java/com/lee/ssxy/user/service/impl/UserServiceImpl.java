package com.lee.ssxy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.ssxy.model.user.Leader;
import com.lee.ssxy.model.user.LeaderUser;
import com.lee.ssxy.model.user.User;
import com.lee.ssxy.model.user.UserDelivery;
import com.lee.ssxy.user.mapper.LeaderMapper;
import com.lee.ssxy.user.mapper.UserDeliveryMapper;
import com.lee.ssxy.user.mapper.UserMapper;
import com.lee.ssxy.user.service.UserService;
import com.lee.ssxy.vo.user.LeaderAddressVo;
import com.lee.ssxy.vo.user.UserLoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserDeliveryMapper userDeliveryMapper;
    @Resource
    private LeaderMapper leaderMapper;
    @Override
    public User getUserByOpenId(String openid) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, openid));
        return user;
    }

    @Override
    public LeaderAddressVo getAddressByLeaderId(Long id) {
        UserDelivery userDelivery = userDeliveryMapper.selectOne(new LambdaQueryWrapper<UserDelivery>().eq(UserDelivery::getUserId, id)
                .eq(UserDelivery::getIsDefault,1));
        if(userDelivery == null){
            return null;
        }
        Leader leader = leaderMapper.selectById(userDelivery.getLeaderId());
        LeaderAddressVo leaderAddressVo = new LeaderAddressVo();
        BeanUtils.copyProperties(leader,leaderAddressVo);
        leaderAddressVo.setUserId(id);
        leaderAddressVo.setLeaderId(leader.getId());
        leaderAddressVo.setLeaderName(leader.getName());
        leaderAddressVo.setLeaderPhone(leader.getPhone());
        leaderAddressVo.setWareId(userDelivery.getWareId());
        leaderAddressVo.setStorePath(leader.getStorePath());
        return leaderAddressVo;
    }

    @Override
    public UserLoginVo getUserLoginVo(Long id) {
        User user = baseMapper.selectById(id);
        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setUserId(id);
        userLoginVo.setNickName(user.getNickName());
        userLoginVo.setPhotoUrl(user.getPhotoUrl());
        userLoginVo.setIsNew(user.getIsNew());
        user.setOpenId(user.getOpenId());

        UserDelivery userDelivery = userDeliveryMapper.selectOne(new LambdaQueryWrapper<UserDelivery>()
                .eq(UserDelivery::getUserId, id)
                .eq(UserDelivery::getIsDefault,1));
        if(userDelivery != null){
            userLoginVo.setLeaderId(userDelivery.getLeaderId());
            userLoginVo.setWareId(userDelivery.getWareId());
        }else{
            userLoginVo.setLeaderId(1L);
            userLoginVo.setWareId(1L);
        }
        return userLoginVo;
    }
}
