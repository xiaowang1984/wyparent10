package com.neuedu.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.neuedu.util.ResultData;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashSet;

@RestController
@RequestMapping("/ums-admin")
public class UpLoadController {
    @Resource
    FastFileStorageClient fastClient;
    @PostMapping("/fileupload")
    ResultData upload(MultipartFile file) throws IOException {
        FastImageFile imageFile = new FastImageFile(
                file.getInputStream(),
                file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()),
                new HashSet<>()
        );
        StorePath storePath = fastClient.uploadImage(imageFile);
        return ResultData.success(storePath.getFullPath());
    }
}
