package com.lee.ssxy.user.api;

import com.lee.ssxy.model.user.User;
import com.lee.ssxy.user.service.UserService;
import com.lee.ssxy.vo.user.LeaderAddressVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@RequestMapping("/api/user/leader")
public class LeaderAddressApiController {
    @Resource
    private UserService userService;


    @GetMapping("/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressById(@PathVariable("userId")Long userId){
        LeaderAddressVo addressByLeaderId = userService.getAddressByLeaderId(userId);
        return addressByLeaderId;
    }
}
