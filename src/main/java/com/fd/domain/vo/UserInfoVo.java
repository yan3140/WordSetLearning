package com.fd.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {
    //用户ID
    private Long userId;
    //用户名
    private String userName;
    //昵称
    private String nickName;
    //性别
    private String sex;
    //邮箱
    private String email;
    //头像
    private String avatar;
}
