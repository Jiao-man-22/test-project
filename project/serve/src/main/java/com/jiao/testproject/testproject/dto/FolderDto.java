package com.jiao.testproject.testproject.dto;
import lombok.Data;

import java.util.List;

@Data
public class FolderDto {

    //@JsonProperty("fileName")
    String name;
    String parent_name;
    /* 0:文件夹 1：表示 文件*/
    String absolutePath;
    int role;
    List<FolderDto> chlid_list;
}
