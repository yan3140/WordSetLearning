package com.fd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fd.ResponseResult;
import com.fd.domain.entity.Book;
import com.fd.domain.vo.BookVo;
import com.fd.mapper.BookMapper;
import com.fd.service.BookService;
import com.fd.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 词书表(Book)表服务实现类
 *
 * @author makejava
 * @since 2025-12-22 10:31:06
 */
@Service("bookService")
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Override
    public ResponseResult listAll() {
        List<Book> books = list();
        List<BookVo> bookVos = BeanCopyUtils.copyBeanList(books, BookVo.class);
        return ResponseResult.okResult(bookVos);
    }
}

