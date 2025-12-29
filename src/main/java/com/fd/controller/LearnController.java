package com.fd.controller;

import com.fd.domain.ResponseResult;
import com.fd.service.LearnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/words")
public class LearnController {

    @Autowired
    private LearnService learnService;

    @PostMapping("/{bookId}/{count}/{status}/recite")
    public ResponseResult reciteWords(@PathVariable Long bookId, @PathVariable Integer count,@PathVariable Integer status){
        return learnService.reciteWords(bookId,count,status);
    }
    @PutMapping("/{wordId}/{status}")
    public ResponseResult updateStatus(@PathVariable Long wordId,@PathVariable Integer status){
        return learnService.updateStatus(wordId,status);
    }
}
