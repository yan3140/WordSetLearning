package com.fd.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDetailVo {
    //词书ID
    private Long bookId;
    //词书名称
    private String bookName;
    //封面URL
    private String coverUrl;
    //简介
    private String description;
    //适用人群
    private String appCrowd;
}
