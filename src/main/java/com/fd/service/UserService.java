package com.fd.service;
 import com.baomidou.mybatisplus.extension.service.IService;
 import com.fd.domain.ResponseResult;
 import com.fd.domain.dto.RegisterDto;
 import com.fd.domain.dto.UserInfoDto;
 import com.fd.domain.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2025-12-22 13:09:49
 */
 public interface UserService extends IService<User> {
 ResponseResult getUserInfo();

 ResponseResult updateUserInfo(UserInfoDto userInfoDto);


 ResponseResult registerUser(RegisterDto registerDto);
}
