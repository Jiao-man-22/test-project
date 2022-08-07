package com.jiao.testproject.testproject.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileWithParam {
    MultipartFile file;
    FileViewVo fileViewVo;
}
