package com.fd.controller;

import com.fd.domain.ResponseResult;
import com.fd.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/word")
public class WordController {

    @Autowired
    private WordService wordService;

    @GetMapping("/listRememberedWords")
    public ResponseResult listRememberedWords(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam Long bookId,@RequestParam Integer status) {
        return wordService.listWords(pageNum, pageSize, bookId,status);
    }

    @GetMapping("/{id}")
    public ResponseResult getWordDetails(@PathVariable Long id) {
        return wordService.getWordDetails(id);
    }
}
