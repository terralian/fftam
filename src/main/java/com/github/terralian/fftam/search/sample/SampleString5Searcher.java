package com.github.terralian.fftam.search.sample;

import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.map.MapNodeTypeEnum;

/**
 * 样例5，五指短剑和敏武宝石特化
 * <p>
 * 多次运行取到的最佳值，看起来应该是含这个组合的最大情况
 * 
 * @author terra.lian
 */
public class SampleString5Searcher extends AbstractSampleSearcher {

    /**
     * <li>五指短剑
     * <li>敏武宝石
     * <li>加斯特拉斐迪弓
     * <li>原质宽剑
     * <li>源氏之盾
     * <li>艾斯托莱阿之剑
     * <li>塔鲁瓦鲁
     * <li>群星之弓
     * <li>加尔米亚之靴
     */
    @Override
    public void search() {
        // 31293
        String mapIndexString = "3,2,3,3,3,2,7,1,0,7,2,2,1,5,0,0,7,1,6,0,5,1,0,4,1,6,4,4,4,0";
        String[] mapIndex = mapIndexString.split(",");
        GameMap gameMap = new GameMap();
        for (int i = 0; i < 30; i++) {
            if (gameMap.isReadOnlyNode(i)) {
                continue;
            }
            gameMap.setNode(i, MapNodeTypeEnum.getEnumByCode(Integer.valueOf(mapIndex[i])));
        }
        gameMap.match();

        output(gameMap);
    }

}

