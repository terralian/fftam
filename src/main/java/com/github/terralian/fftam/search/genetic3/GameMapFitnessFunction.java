package com.github.terralian.fftam.search.genetic3;

import java.util.List;

import com.github.terralian.aforge.genetic.chromosomes.IChromosome;
import com.github.terralian.aforge.genetic.fitness.IFitnessFunction;
import com.github.terralian.fftam.item.IMapItem;
import com.github.terralian.fftam.map.IGameMap;

/**
 * 游戏地图摆放的适应度函数评估，值越大越好
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