package com.fd.controller;

import com.fd.domain.ResponseResult;
import com.fd.domain.dto.RegisterDto;
import com.fd.domain.dto.UserInfoDto;
import com.fd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    public ResponseResult getUserInfo(){
        return userService.getUserInfo();
    }

    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody UserInfoDto userInfoDto){
        return userService.updateUserInfo(userInfoDto);
    }

    @PostMapping("/register")
    public ResponseResult registerUser(@RequestBody RegisterDto registerDto){
        return userService.registerUser(registerDto);
    }
}
