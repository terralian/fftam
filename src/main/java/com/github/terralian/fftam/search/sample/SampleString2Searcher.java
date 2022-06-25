package com.github.terralian.fftam.search.sample;

import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.map.MapNodeTypeEnum;

/**
 * 搜索出的优秀摆放
 * 
 * @author terra.lian
 */
public class SampleString2Searcher extends AbstractSampleSearcher {

    /**
     * <li>加斯特拉斐迪弓
     * <li>原质宽剑
     * <li>源氏之盔
     * <li>源氏之盾
     * <li>源氏护腕
     * <li>艾斯托莱阿之剑
     * <li>五指短剑
     * <li>加尔米亚之靴
     * <li>公主守护杖
     */
    @Override
    public void search() {
        // 35648
        String mapIndexString = "7,6,2,7,3,6,2,3,0,3,1,1,1,5,0,0,1,3,4,0,5,1,0,4,0,4,2,2,7";
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

