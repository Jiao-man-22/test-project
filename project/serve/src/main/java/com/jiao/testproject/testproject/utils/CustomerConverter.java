package com.jiao.testproject.testproject.utils;

import cn.hutool.core.convert.Converter;

public class CustomerConverter implements Converter<String> {
    @Override
    public String convert(Object value, String s) throws IllegalArgumentException {
        return "Custom: " + value.toString();
    }
}
