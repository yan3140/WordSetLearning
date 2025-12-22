package com.fd.utils;

import org.springframework.beans.BeanUtils;


import java.util.List;


public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    public static <V> V copyBean(Object source, Class<V> clazz) {
        V target = null;
        try {
            target = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <K, V> List<V> copyBeanList(List<K> source, Class<V> clazz) {
        return source.stream()
                .map(s -> copyBean(s, clazz))
                .toList();
    }
}
