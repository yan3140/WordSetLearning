package com.fd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fd.ResponseResult;
import com.fd.domain.entity.Book;
import com.fd.domain.vo.BookDetailVo;
import com.fd.domain.vo.BookVo;
import com.fd.mapper.BookMapper;
import com.fd.service.BookService;
import com.fd.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BookMapper bookMapper;
    @Override
    public ResponseResult listAll() {
        List<Book> books = list();
        List<BookVo> bookVos = BeanCopyUtils.copyBeanList(books, BookVo.class);
        return ResponseResult.okResult(bookVos);
    }

    @Override
    public ResponseResult detail(Long bookId) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<Book>();
        queryWrapper.eq(Book::getBookId, bookId);
        Book book = bookMapper.selectOne(queryWrapper);
        BookDetailVo bookDetailVo = BeanCopyUtils.copyBean(book, BookDetailVo.class);
        return ResponseResult.okResult(bookDetailVo);
    }
}

