package com.github.terralian.aforge.genetic.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.github.terralian.aforge.genetic.chromosomes.IChromosome;
import com.github.terralian.csharp.LangUtil;

/**
 * Rank selection method.
 * <p>
 * The algorithm selects chromosomes to the new generation depending on their fitness values - the better fitness value chromosome has, the
 * more chances it has to become member of the new generation. Each chromosome can be selected several times to the new generation.
 * <p>
 * This algorithm is similar to {@link RouletteWheelSelection} algorithm, but the difference is in "wheel" and its sectors' size calculation
 * method. The size of the wheel equals to <b>size * ( size + 1 ) / 2</b>, where <b>size</b> is the current size of population. The worst
 * chromosome has its sector's size equal to 1, the next chromosome has its sector's size equal to 2, etc.
 */
public class RankSelection implements ISelectionMethod {

    /**
     * random number generator
     */
    protected ThreadLocalRandom rand = ThreadLocalRandom.current();

    /**
     * Initializes a new instance of the {@link RankSelection} class.
     */
    public RankSelection() {}

    /**
     * Apply selection to the specified population.
     * <p>
     * Filters specified population keeping only those chromosomes, which won "roulette" game.
     * 
     * @param chromosomes Population, which should be filtered.
     * @param size The amount of chromosomes to keep.
     * 
     */
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
                    newPopulation.add(((IChromosome) chromosomes.get(i)).clone());
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
