package com.jiao.testproject.testproject.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class FieldTranfUtils {

    private static final Logger log = LoggerFactory.getLogger(FieldTranfUtils.class);
    /*
    *@param source
    *@param target
    * 按照 source 字段的 顺序 给 target 字段赋值
    * */
    public static void reflectField(Object source,Object target) {
        try{
            Class<?> class_s = source.getClass();
            java.lang.Class<?> class_t = target.getClass();
            Field[] field_s = class_s.getDeclaredFields();
            Field[] field_t = class_t.getDeclaredFields();
            if (field_s.length> field_t.length){
                for (int i=0;i<field_t.length;i++){
                    if (field_s[i] != null && field_t[i] !=null){
                        field_s[i].setAccessible(true);
                        field_t[i].setAccessible(true);
                        field_t[i].set(target,field_s[i].get(source));
                    }
                }
            }else {
                for (int i=0;i<field_s.length;i++){
                    if (field_s[i] != null && field_t[i] !=null){
                        field_s[i].setAccessible(true);
                        field_t[i].setAccessible(true);
                        field_t[i].set(target,field_s[i].get(source));
                    }else{
                        return;
                    }
                }
            }

        }catch (Exception e){
            log.error("项目复制工具类出错");
        }
    }
}
