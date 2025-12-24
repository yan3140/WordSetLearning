package com.fd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fd.domain.ResponseResult;
import com.fd.domain.constants.SystemConstants;
import com.fd.domain.dto.RegisterDto;
import com.fd.domain.dto.UserInfoDto;
import com.fd.domain.entity.*;
import com.fd.domain.vo.LoginUserVo;
import com.fd.domain.vo.UserInfoVo;
import com.fd.enums.AppHttpCodeEnum;
import com.fd.exception.SystemException;
import com.fd.mapper.UserBookMapper;
import com.fd.mapper.UserMapper;
import com.fd.service.UserBookService;
import com.fd.service.UserService;
import com.fd.service.UserWordStatusService;
import com.fd.service.WordService;
import com.fd.utils.BeanCopyUtils;
import com.fd.utils.JwtUtil;
import com.fd.utils.RedisCache;
import com.fd.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2025-12-22 13:09:49
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserBookService userBookService;

    @Autowired
    private WordService wordService;

    @Autowired
    private UserWordStatusService userWordStatusService;

    @Override
    public ResponseResult getUserInfo() {
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getUserId, userId);
        User user = userMapper.selectOne(queryWrapper);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(UserInfoDto userInfoDto) {
        if(!StringUtils.hasText(userInfoDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(userInfoDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(userInfoDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        User oldUser = SecurityUtils.getLoginUser().getUser();
        User newUser = BeanCopyUtils.copyBean(userInfoDto, User.class);
        newUser.setUserId(oldUser.getUserId());
        newUser.setPassword(oldUser.getPassword());
        newUser.setTargetBookId(oldUser.getTargetBookId());
        updateById(newUser);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult registerUser(RegisterDto registerDto) {
        if(!StringUtils.hasText(registerDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(registerDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(registerDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(registerDto.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(nickNameIsExist(registerDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emailIsExist(registerDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        String encode = passwordEncoder.encode(registerDto.getPassword());
        User user = BeanCopyUtils.copyBean(registerDto, User.class);
        user.setPassword(encode);
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult login(Authentication authenticate) {
        if(Objects.isNull(authenticate)){
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getUserId().toString();
        String token = JwtUtil.createJWT(userId);
        redisCache.setCacheObject("loginUser:"+userId,loginUser);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        LoginUserVo loginUserVo = new LoginUserVo(userInfoVo, token);
        return ResponseResult.okResult(loginUserVo);
    }

    @Override
    public ResponseResult logout() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUser().getUserId().toString();
        redisCache.deleteObject("loginUser:"+userId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addBook( Long bookId) {
        LambdaQueryWrapper<UserBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserBook::getBookId, bookId);
        Long userId = SecurityUtils.getUserId();
        queryWrapper.eq(UserBook::getUserId, userId);
        //判断是否重复添加
        if(userBookService.count(queryWrapper) > 0){
            throw new SystemException(AppHttpCodeEnum.BOOK_EXIST);
        }
        UserBook userBook = new UserBook(userId,bookId, SystemConstants.UNDELETED);
        userBookService.save(userBook);
        //将单词状态同步到word状态表
        LambdaQueryWrapper<Word>  queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Word::getBookId, bookId);
        int batchSize = 500;
        IPage<Word> wordPage = new Page<>(1, batchSize);
        do {
            // 分页查询单词
            wordPage = wordService.page(wordPage,queryWrapper1);
            List<Word> wordList = wordPage.getRecords();
            if (CollectionUtils.isEmpty(wordList)) {
                break; // 无数据则退出循环
            }
            List<UserWordStatus> batchStatusList = wordList.stream()
                    .map(Word::getWordId)
                    .map(wordId -> new UserWordStatus(userId, wordId, SystemConstants.WORD_STATUS_UNREMEMBERED, 0))
                    .toList();
            userWordStatusService.saveBatch(batchStatusList);
            wordPage.setCurrent(wordPage.getCurrent() + 1);
        } while (wordPage.getCurrent() <= wordPage.getPages()); // 直到所有页查询完毕
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteBook(Long bookId) {
        LambdaUpdateWrapper<UserBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserBook::getBookId, bookId);
        updateWrapper.eq(UserBook::getUserId, SecurityUtils.getUserId());
        updateWrapper.set(UserBook::getIsDeleted, SystemConstants.DELETED);
        userBookService.update(updateWrapper);
        return ResponseResult.okResult();
    }

    private boolean nickNameIsExist(String nickName){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);
        return count(queryWrapper) > 0;
    }

    private boolean emailIsExist(String email){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return count(queryWrapper) > 0;
    }
}

