package com.github.terralian.fftam.search.sample;

import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.map.MapNodeTypeEnum;

/**
 * 搜索出的优秀摆放
 * 
 * @author terra.lian
 */
public class SampleString1Searcher extends AbstractSampleSearcher {

    /**
     * <li>加斯特拉斐迪弓
     * <li>原质宽剑
     * <li>源氏之盔
     * <li>源氏之盾
     * <li>源氏护腕
     * <li>艾斯托莱阿之剑
     * <li>五指短剑
     * <li>加尔米亚之靴
     */
    @Override
    public void search() {
        // 35383
        String mapIndexString = "6,6,2,7,2,7,3,7,0,3,2,2,1,5,0,0,3,1,4,1,5,3,0,4,1,4,1,0,0";
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
