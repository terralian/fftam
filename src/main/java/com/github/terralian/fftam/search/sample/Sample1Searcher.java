package com.github.terralian.fftam.search.sample;

import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.map.MapNodeTypeEnum;

/**
 * 样本1
 * <p>
 * 分数21363
 * <li>加斯特拉斐迪弓
 * <li>原质宽剑
 * <li>敏武宝石
 * <li>五指短剑
 * <li>丝带
 * <li>加尔米亚之靴
 * <li>宙斯锡杖
 * <li>超级利刃
 * 
 * @author terra.lian
 */
public class Sample1Searcher extends AbstractSampleSearcher {

    @Override
    public void search() {

        GameMap gameMap = new GameMap();
        gameMap.setNode(0, MapNodeTypeEnum.Desert);
        gameMap.setNode(1, MapNodeTypeEnum.Forest);
        gameMap.setNode(2, MapNodeTypeEnum.Desert);
        gameMap.setNode(3, MapNodeTypeEnum.Forest);
        gameMap.setNode(4, MapNodeTypeEnum.Desert);
        gameMap.setNode(5, MapNodeTypeEnum.Desert);
        gameMap.setNode(6, MapNodeTypeEnum.Mountain);
        gameMap.setNode(7, MapNodeTypeEnum.Street);
        // gameMap.setNode(8, MapNodeType.Street);
        gameMap.setNode(9, MapNodeTypeEnum.Mountain);
        gameMap.setNode(10, MapNodeTypeEnum.Cave);
        gameMap.setNode(11, MapNodeTypeEnum.Cave);
        // gameMap.setNode(12, MapNodeType.Mountain);
        gameMap.setNode(13, MapNodeTypeEnum.Mountain);
        gameMap.setNode(14, MapNodeTypeEnum.Street);
        gameMap.setNode(15, MapNodeTypeEnum.River);
        gameMap.setNode(16, MapNodeTypeEnum.Street);
        gameMap.setNode(17, MapNodeTypeEnum.Street);
        gameMap.setNode(18, MapNodeTypeEnum.Steppes);
        // gameMap.setNode(19, MapNodeTypeEnum.Street);
        gameMap.setNode(20, MapNodeTypeEnum.Mountain);
        gameMap.setNode(21, MapNodeTypeEnum.DeadStreet);
        gameMap.setNode(22, MapNodeTypeEnum.River);
        gameMap.setNode(23, MapNodeTypeEnum.DeadStreet);
        gameMap.setNode(24, MapNodeTypeEnum.DeadStreet);
        gameMap.setNode(25, MapNodeTypeEnum.Steppes);
        gameMap.setNode(26, MapNodeTypeEnum.Forest);
        gameMap.setNode(27, MapNodeTypeEnum.Forest);
        // gameMap.setNode(28, MapNodeTypeEnum.Steppes);
        gameMap.setNode(29, MapNodeTypeEnum.Steppes);

        gameMap.match();

        output(gameMap);
    }
}
