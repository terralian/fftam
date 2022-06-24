package com.github.terralian.fftam.search;

/**
 * 地图查找接口
 * <p>
 * 该接口的实现类用于实际查询其地图组合及其对应的探索物
 * <p>
 * 该类没有返回值，输出通过打印进行
 * 
 * @author terra.lian
 */
public interface IMapSearcher {

    /**
     * 执行查询
     */
    void search();
}
