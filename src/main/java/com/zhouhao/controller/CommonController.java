package com.zhouhao.controller;

import com.zhouhao.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RequestMapping("common")
@RestController
public class CommonController {
    @Value("${parentPath}")
    String parentPath;

    @PostMapping("upload")
    public R<String> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.indexOf("."));
        String name = UUID.randomUUID() + suffix;

        File dir = new File(ResourceUtils.getURL("classpath:").getPath() + parentPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File myfile = new File(dir, name);
        file.transferTo(myfile);
        return R.success(name);
    }

    @GetMapping("download")
    public void download(HttpServletResponse response, String name) throws IOException {
        response.setContentType("image/jpg");
        ServletOutputStream outputStream = response.getOutputStream();

        File picture = new File(ResourceUtils.getURL("classpath:").getPath() + parentPath + name);
        FileInputStream fileInputStream = new FileInputStream(picture);

        byte[] bytes = new byte[100];
        int len = 0;

        while((len = fileInputStream.read(bytes)) != -1){
            outputStream.write(bytes, 0, len);
        }

        outputStream.close();
        fileInputStream.close();
    }
}
