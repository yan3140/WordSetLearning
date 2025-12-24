package com.fd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fd.domain.entity.Book;
import com.fd.domain.entity.UserBook;
import com.fd.mapper.BookMapper;
import com.fd.mapper.UserBookMapper;
import com.fd.service.UserBookService;
import org.springframework.stereotype.Service;

@Service
public class UserBookServiceImpl extends ServiceImpl<UserBookMapper, UserBook> implements UserBookService {
}
