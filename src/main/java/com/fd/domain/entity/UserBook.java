package com.fd.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户词书关联表(UserBook)表实体类
 *
 * @author makejava
 * @since 2025-12-24 16:52:15
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_book")
public class UserBook{
 
    //用户ID
    @TableId
    private Long userId;
    //词书ID
    @TableField("book_id")
    private Long bookId;
    //逻辑删除标记：0-未删除 1-已删除
    private Integer isDeleted;





}

