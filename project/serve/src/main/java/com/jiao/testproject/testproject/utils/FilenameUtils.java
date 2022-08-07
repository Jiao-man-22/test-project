package com.jiao.testproject.testproject.utils;

public class FilenameUtils {
    static String suiffx=null;
    /*截取后缀*/
    public static String getExtension(String filename){
        try {
            int indexOf = filename.lastIndexOf(".");
             suiffx = filename.substring(indexOf + 1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return suiffx;
    }
}
