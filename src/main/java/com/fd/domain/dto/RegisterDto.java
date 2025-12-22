package com.fd.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    //用户名
    private String userName;
    //昵称
    private String nickName;
    //邮箱
    private String email;
    //密码
    private String password;

}
