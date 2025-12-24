package com.fd.service.impl;
 import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
 import com.fd.domain.entity.UserWordStatus;
 import com.fd.mapper.UserWordStatusMapper;
 import com.fd.service.UserWordStatusService;
 import org.springframework.stereotype.Service;
 /**
 * 用户单词学习状态表(UserWordStatus)表服务实现类
 *
 * @author makejava
 * @since 2025-12-24 17:45:56
 */
 @Service("userWordStatusService")
public class UserWordStatusServiceImpl extends ServiceImpl<UserWordStatusMapper, UserWordStatus> implements UserWordStatusService {
 }

