package com.github.terralian.fftam;

import com.github.terralian.fftam.search.IMapSearcher;
import com.github.terralian.fftam.search.genetic3.MapGeneticSearcher3;

/**
 * 应用主体
 * 
 * @author terra.lian
 */
public class App {

    /**
     * 主方法，执行该方法即会打印结果
     */
    public static void main(String[] args) {
        IMapSearcher searcher = new MapGeneticSearcher3();
        searcher.search();
    }
}
