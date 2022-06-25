package com.github.terralian.fftam.search.sample;

import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.map.MapNodeTypeEnum;

/**
 * 搜索出的优秀摆放
 * 
 * @author terra.lian
 */
public class SampleString4Searcher extends AbstractSampleSearcher {

    /**
     * <li>加斯特拉斐迪弓
     * <li>原质宽剑
     * <li>敏武宝石
     * <li>杀意剑
     * <li>源氏之盔
     * <li>源氏之盾
     * <li>艾斯托莱阿之剑
     */
    @Override
    public void search() {
        // 35183
        String mapIndexString = "3,3,6,3,6,3,2,1,0,2,2,2,1,5,0,0,7,1,4,1,5,7,0,4,0,4,1,0,7";
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

