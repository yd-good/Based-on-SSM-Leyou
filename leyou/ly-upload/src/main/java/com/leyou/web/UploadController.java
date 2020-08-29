/*
信息:
*/
package com.leyou.web;

import com.leyou.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam(value = "file",required = true)MultipartFile file){
        return ResponseEntity.ok(uploadService.saveImage(file));

    }
}
