package com.example.zuulfiledemo;


/**
 * .
 *
 * @author yonoel 2021/05/17
 */

import java.io.File;
import java.io.IOException;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @RequestMapping(method={RequestMethod.POST},path={"/file/upload"})
    public String upfile(@RequestParam(value = "file") MultipartFile file)throws IOException {
        final byte[] bytes = file.getBytes();
        final File file1 = new File(file.getOriginalFilename());
        FileCopyUtils.copy(bytes,file1);
        return file1.getAbsolutePath();
    }
}
