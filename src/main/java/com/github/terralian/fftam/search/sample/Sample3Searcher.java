package com.github.terralian.fftam.search.sample;

import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.map.MapNodeTypeEnum;

/**
 * 样本3
 * <p>
 * 分数31048
 * <p>
 * <li>加斯特拉斐迪弓
 * <li>原质宽剑
 * <li>杀意剑
 * <li>源氏之盾
 * <li>艾斯托莱阿之剑
 * <li>五指短剑
 * <li>加尔米亚之靴
 * <li>公主守护杖
 * <li>宙斯锡杖
 * 
 * @author terra.lian
 */
public class Sample3Searcher extends AbstractSampleSearcher {

    @Override
    public void search() {
        GameMap gameMap = new GameMap();
        gameMap.setNode(0, MapNodeTypeEnum.Desert);
        gameMap.setNode(1, MapNodeTypeEnum.Forest);
        gameMap.setNode(2, MapNodeTypeEnum.Mountain);
        gameMap.setNode(3, MapNodeTypeEnum.Forest);
        gameMap.setNode(4, MapNodeTypeEnum.Mountain);
        gameMap.setNode(5, MapNodeTypeEnum.Desert);
        gameMap.setNode(6, MapNodeTypeEnum.Steppes);
        gameMap.setNode(7, MapNodeTypeEnum.Mountain);
        // gameMap.setNode(8, MapNodeType.Street);
        gameMap.setNode(9, MapNodeTypeEnum.Steppes);
        gameMap.setNode(10, MapNodeTypeEnum.Desert);
        gameMap.setNode(11, MapNodeTypeEnum.Desert);
        // gameMap.setNode(12, MapNodeType.Mountain);
        gameMap.setNode(13, MapNodeTypeEnum.Forest);
        // gameMap.setNode(14, MapNodeType.Street);
        gameMap.setNode(15, MapNodeTypeEnum.Street);
        gameMap.setNode(16, MapNodeTypeEnum.Steppes);
        gameMap.setNode(17, MapNodeTypeEnum.Mountain);
        gameMap.setNode(18, MapNodeTypeEnum.River);
        gameMap.setNode(19, MapNodeTypeEnum.Street);
        gameMap.setNode(20, MapNodeTypeEnum.Forest);
        gameMap.setNode(21, MapNodeTypeEnum.Street);
        gameMap.setNode(22, MapNodeTypeEnum.Street);
        gameMap.setNode(23, MapNodeTypeEnum.DeadStreet);
        gameMap.setNode(24, MapNodeTypeEnum.DeadStreet);
        gameMap.setNode(25, MapNodeTypeEnum.River);
        gameMap.setNode(26, MapNodeTypeEnum.Cave);
        gameMap.setNode(27, MapNodeTypeEnum.Cave);
        // gameMap.setNode(28, MapNodeTypeEnum.Steppes);
        gameMap.setNode(29, MapNodeTypeEnum.DeadStreet);

        gameMap.match();

        output(gameMap);
    }
}
