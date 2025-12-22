package com.fd.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 词书表(Book)表实体类
 *
 * @author makejava
 * @since 2025-12-22 10:30:48
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("book")
public class Book{
    //词书ID
    @TableId
    private Long bookId;
 
    //词书名称
    private String bookName;
    //封面URL
    private String coverUrl;
    //简介
    private String description;
    //创建时间
    private Date createTime;





}

