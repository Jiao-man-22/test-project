package com.jiao.testproject.testproject.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


public class FileDto {

     private   Integer fileId;

     public Integer getFileId() {
          return fileId;
     }

     public void setFileId(Integer fileId) {
          this.fileId = fileId;
     }

     public String getFileName() {
          return fileName;
     }

     public void setFileName(String fileName) {
          this.fileName = fileName;
     }

     public String getFilePath() {
          return filePath;
     }

     public void setFilePath(String filePath) {
          this.filePath = filePath;
     }

     public String getFileSize() {
          return fileSize;
     }

     public void setFileSize(String fileSize) {
          this.fileSize = fileSize;
     }

     public String getUploadTime() {
          return uploadTime;
     }

     public void setUploadTime(String uploadTime) {
          this.uploadTime = uploadTime;
     }

     public Integer getUserId() {
          return userId;
     }

     public void setUserId(Integer userId) {
          this.userId = userId;
     }

     private   String fileName;

     private   String filePath;

     private   String fileSize;

     private   String uploadTime;

     private  Integer userId;


}
