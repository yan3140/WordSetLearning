package com.fd.controller;

import com.fd.domain.ResponseResult;
import com.fd.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/word")
public class WordController {

    @Autowired
    private WordService wordService;

    @GetMapping("/listAll")
    public ResponseResult listAll(Integer pageNum, Integer pageSize, Long id) {
        return wordService.listAll(pageNum, pageSize, id);
    }

    @GetMapping("/{id}")
    public ResponseResult getWordDetails(@PathVariable Long id) {
        return wordService.getWordDetails(id);
    }
}
