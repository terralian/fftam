package com.github.terralian.fftam.search.sample;

import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.map.MapNodeTypeEnum;

/**
 * 搜索出的优秀摆放
 * 
 * @author terra.lian
 */
public class SampleString3Searcher extends AbstractSampleSearcher {

    /**
     * <li>加斯特拉斐迪弓
     * <li>原质宽剑
     * <li>敏武宝石
     * <li>源氏之凯
     * <li>源氏之盾
     * <li>源氏护腕
     * <li>艾斯托莱阿之剑
     * <li>塔鲁瓦鲁
     */
    @Override
    public void search() {
        // 35608
        String mapIndexString = "3,3,7,3,7,3,6,2,0,6,1,1,1,5,0,0,0,2,2,0,5,7,0,1,4,2,4,4,4,1";
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

