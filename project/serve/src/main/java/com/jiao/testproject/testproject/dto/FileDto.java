package com.jiao.testproject.testproject.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class FileDto {

     private   Integer fileId;

     private   String fileName;

     private   String filePath;

     private   String fileSize;

     private   String uploadTime;

     private  Integer userId;


}
