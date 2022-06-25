package com.github.terralian.fftam.search.sample;

import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.map.MapNodeTypeEnum;

/**
 * 样本2
 * <p>
 * 分数16098
 * <p>
 * <li>原质宽剑
 * <li>源氏之凯
 * <li>五指短剑
 * <li>丝带
 * <li>公主守护杖
 * <li>宙斯锡杖
 * 
 * @author terra.lian
 */
public class Sample2Searcher extends AbstractSampleSearcher {

    @Override
    public void search() {
        GameMap gameMap = new GameMap();
        gameMap.setNode(0, MapNodeTypeEnum.Cave);
        gameMap.setNode(1, MapNodeTypeEnum.Forest);
        gameMap.setNode(2, MapNodeTypeEnum.Steppes);
        gameMap.setNode(3, MapNodeTypeEnum.Cave);
        gameMap.setNode(4, MapNodeTypeEnum.Steppes);
        gameMap.setNode(5, MapNodeTypeEnum.Forest);
        gameMap.setNode(6, MapNodeTypeEnum.Mountain);
        gameMap.setNode(7, MapNodeTypeEnum.Street);
        // gameMap.setNode(8, MapNodeType.Street);
        gameMap.setNode(9, MapNodeTypeEnum.Mountain);
        gameMap.setNode(10, MapNodeTypeEnum.DeadStreet);
        gameMap.setNode(11, MapNodeTypeEnum.DeadStreet);
        // gameMap.setNode(12, MapNodeType.Mountain);
        gameMap.setNode(13, MapNodeTypeEnum.Forest);
        // gameMap.setNode(14, MapNodeType.Street);
        gameMap.setNode(15, MapNodeTypeEnum.Street);
        gameMap.setNode(16, MapNodeTypeEnum.Mountain);
        gameMap.setNode(17, MapNodeTypeEnum.Street);
        gameMap.setNode(18, MapNodeTypeEnum.Desert);
        gameMap.setNode(19, MapNodeTypeEnum.Street);
        gameMap.setNode(20, MapNodeTypeEnum.Forest);
        gameMap.setNode(21, MapNodeTypeEnum.Mountain);
        gameMap.setNode(22, MapNodeTypeEnum.DeadStreet);
        gameMap.setNode(23, MapNodeTypeEnum.River);
        gameMap.setNode(24, MapNodeTypeEnum.River);
        gameMap.setNode(25, MapNodeTypeEnum.Desert);
        gameMap.setNode(26, MapNodeTypeEnum.Desert);
        gameMap.setNode(27, MapNodeTypeEnum.Desert);
        // gameMap.setNode(28, MapNodeTypeEnum.Steppes);
        gameMap.setNode(29, MapNodeTypeEnum.Steppes);

        gameMap.match();

        output(gameMap);
    }
}
