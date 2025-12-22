package com.fd.service;
 import com.baomidou.mybatisplus.extension.service.IService;
 import com.fd.ResponseResult;
 import com.fd.domain.entity.Book;

/**
 * 词书表(Book)表服务接口
 *
 * @author makejava
 * @since 2025-12-22 10:31:06
 */
 public interface BookService extends IService<Book> {
 ResponseResult listAll();

 ResponseResult detail(Long bookId);
}
