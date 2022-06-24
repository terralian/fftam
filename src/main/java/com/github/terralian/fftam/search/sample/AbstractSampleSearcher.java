package com.github.terralian.fftam.search.sample;

import java.util.List;

import com.github.terralian.fftam.item.IMapItem;
import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.search.IMapSearcher;

/**
 * 样本查询的公共父类
 * 
 * @author terra.lian
 * @since 2022年6月16日
 */
public abstract class AbstractSampleSearcher implements IMapSearcher {

    /**
     * 打印结果
     * 
     * @param gameMap
     */
    protected static void output(GameMap gameMap) {
        List<IMapItem> mapItems = gameMap.getMatchCacheList();
        mapItems.forEach(k -> System.out.println(k));
        System.out.println("分数：" + IMapItem.sumItemPoint(mapItems));
    }
}
