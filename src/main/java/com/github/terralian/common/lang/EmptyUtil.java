package com.github.terralian.common.lang;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 字符串，数组，字符串判空工具类，防止抛出空指针异常
 * 
 * @author 作者: Terra.Lian
 * @date 创建时间：2020年2月27日 上午9:43:23
 */
public final class EmptyUtil {
    private EmptyUtil() {}

    /**
     * 判断字符串是否为空
     * 
     * @param value 字符串
     */
    public static boolean empty(String value) {
        return value == null || value.length() < 1;
    }

    /**
     * 判断字符串是否非空
     * 
     * @param value 字符串
     */
    public static boolean notEmpty(String value) {
        return !empty(value);
    }

    /**
     * 判断字符串是否为空或者为空白行
     * 
     * @param value 字符串
     */
    public static boolean trimEmpty(String value) {
        return value == null || value.trim().length() < 1;
    }

    /**
     * 判断一个字符串不是非空且非空白行
     * 
     * @param value 字符串
     */
    public static boolean notBlank(String value) {
        return !trimEmpty(value);
    }

    /**
     * 判断数组是否全为空
     * 
     * @param array 字符串数组
     */
    public static boolean allEmpty(String[] array) {
        if (array == null)
            return true;
        for (int i = 0; i < array.length; i++) {
            if (!empty(array[i]))
                return false;
        }
        return true;
    }

    /**
     * 判断数组是否存在至少一个为空
     * 
     * @param array 字符串数组
     */
    public static boolean anyEmpty(String[] array) {
        if (array == null)
            return true;
        for (int i = 0; i < array.length; i++) {
            if (empty(array[i]))
                return true;
        }
        return false;
    }

    /**
     * 判断一个数组是否为空
     * 
     * @param array 数组
     */
    public static boolean empty(Object[] array) {
        return array == null || array.length < 1;
    }

    /**
     * 判断一个数组是否不为空
     * 
     * @param array 数组
     */
    public static boolean notEmpty(Object[] array) {
        return !empty(array);
    }

    /**
     * 判断一个集合是否为空
     * 
     * @param collection 集合
     * @return
     */
    public static boolean empty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断一个集合是否不为空
     * 
     * @param collection 集合
     */
    public static boolean notEmpty(Collection<?> collection) {
        return !empty(collection);
    }

    /**
     * 判断一个Map是否为空
     * 
     * @param map Map
     */
    public static boolean empty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断一个Map是否不为空
     * 
     * @param map Map
     */
    public static boolean notEmpty(Map<?, ?> map) {
        return !empty(map);
    }

    /**
     * 判断一个迭代器是否为空
     * 
     * @param iterator 迭代器
     */
    public static boolean empty(Iterator<?> iterator) {
        return iterator == null || !iterator.hasNext();
    }

    /**
     * 判断一个迭代器是否不为空
     * 
     * @param iterator 迭代器
     */
    public static boolean notEmpty(Iterator<?> iterator) {
        return !empty(iterator);
    }
}
