package com.github.terralian.fftam.search.genetic2;

import java.util.List;

import com.github.terralian.aforge.genetic.chromosomes.IChromosome;
import com.github.terralian.aforge.genetic.fitness.IFitnessFunction;
import com.github.terralian.fftam.item.IMapItem;
import com.github.terralian.fftam.map.IGameMap;

/**
 * 游戏地图最终组合的适应函数，计算的结果是其分数
 * 
 * @author terra.lian
 */
class GameMapFitnessFunction implements IFitnessFunction {

    @Override
    public double evaluate(IChromosome chromosome) {
        GameMapChromosome g = (GameMapChromosome) chromosome;
        IGameMap gameMap = g.getValue();
        List<IMapItem> mapItems = gameMap.getMatchCacheList();
        return IMapItem.sumItemPoint(mapItems);
    }

}