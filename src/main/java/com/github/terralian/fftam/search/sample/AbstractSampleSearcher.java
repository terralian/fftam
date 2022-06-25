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
        System.out.println("========================================================");
        System.out.println("查找的地图最佳摆放：");
        System.out.println("分数：" + IMapItem.sumItemPoint(mapItems));
        System.out.println("地图摆放：" + gameMap.toString());
        System.out.println("地图映射值：" + gameMap.toNodeTypeString());
        System.out.println("宝物一览：");
        System.out.println();
        mapItems.sort((a, b) -> {
            int compare = b.itemPoint().compareTo(a.itemPoint());
            if (compare == 0) {
                return a.itemName().compareTo(b.itemName());
            }
            return compare;
        });
        mapItems.forEach(k -> System.out.println(k.toString()));
    }
}
