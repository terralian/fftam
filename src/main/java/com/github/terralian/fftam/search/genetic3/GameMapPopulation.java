package com.github.terralian.fftam.search.genetic3;

import com.github.terralian.aforge.genetic.Population;
import com.github.terralian.aforge.genetic.chromosomes.IChromosome;
import com.github.terralian.aforge.genetic.fitness.IFitnessFunction;
import com.github.terralian.aforge.genetic.selection.ISelectionMethod;

/**
 * 游戏地图种群，不使用交叉行为
 * 
 * @author terra.lian
 */
class GameMapPopulation extends Population {

    public GameMapPopulation(int size, IChromosome ancestor, IFitnessFunction fitnessFunction, ISelectionMethod selectionMethod) {
        super(size, ancestor, fitnessFunction, selectionMethod);
    }

    /**
     * 不使用交叉操作
     */
    @Override
    public void crossover() {}
}
