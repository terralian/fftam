package com.github.terralian.fftam.search.genetic1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.github.terralian.aforge.genetic.chromosomes.IChromosome;
import com.github.terralian.aforge.genetic.selection.ISelectionMethod;
import com.github.terralian.csharp.LangUtil;

/**
 * 选择算法
 * 
 * @author terra.lian
 */
class GameMapSelectionMethod implements ISelectionMethod {

    // 随机数生成器
    private static ThreadLocalRandom rand = ThreadLocalRandom.current();

    @Override
    public void applySelection(List<IChromosome> chromosomes, int size) {
        // new population, initially empty
        List<IChromosome> newPopulation = new ArrayList<>();
        // size of current population
        int currentSize = chromosomes.size();

        // sort current population
        chromosomes.sort(LangUtil.defaultSort());

        // calculate amount of ranges in the wheel
        double ranges = currentSize * (currentSize + 1) / 2;

        // create wheel ranges
        double[] rangeMax = new double[currentSize];
        double s = 0;

        for (int i = 0, n = currentSize; i < currentSize; i++, n--) {
            s += ((double) n / ranges);
            rangeMax[i] = s;
        }

        // select chromosomes from old population to the new population
        for (int j = 0; j < size; j++) {
            // get wheel value
            double wheelValue = rand.nextDouble();
            // find the chromosome for the wheel value
            for (int i = 0; i < currentSize; i++) {
                if (wheelValue <= rangeMax[i]) {
                    // add the chromosome to the new population
                    newPopulation.add(((IChromosome) chromosomes.get(0)).clone());
                    break;
                }
            }
        }

        // empty current population
        chromosomes.clear();

        // move elements from new to current population
        chromosomes.addAll(newPopulation);
    }

}
