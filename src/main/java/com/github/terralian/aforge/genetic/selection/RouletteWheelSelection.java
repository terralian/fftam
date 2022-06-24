package com.github.terralian.aforge.genetic.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.github.terralian.aforge.genetic.chromosomes.IChromosome;

/**
 * Roulette wheel selection method.
 * <p>
 * The algorithm selects chromosomes to the new generation according to their fitness values - the more fitness value chromosome has, the
 * more chances it has to become member of new generation. Each chromosome can be selected several times to the new generation.
 * <p>
 * The "roulette's wheel" is divided into sectors, which size is proportional to the fitness values of chromosomes - the size of the wheel
 * is the sum of all fitness values, size of each sector equals to fitness value of chromosome.
 */
public class RouletteWheelSelection implements ISelectionMethod {

    /**
     * random number generator
     */
    protected ThreadLocalRandom rand = ThreadLocalRandom.current();

    /**
     * Initializes a new instance of the {@link RouletteWheelSelection} class.
     */
    public RouletteWheelSelection() {}

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
        List<IChromosome> newPopulation = new ArrayList<IChromosome>();
        // size of current population
        int currentSize = chromosomes.size();

        // calculate summary fitness of current population
        double fitnessSum = 0;
        for (IChromosome c : chromosomes) {
            fitnessSum += c.getFitness();
        }

        // create wheel ranges
        double[] rangeMax = new double[currentSize];
        double s = 0;
        int k = 0;

        for (IChromosome c : chromosomes) {
            // cumulative normalized fitness
            s += (c.getFitness() / fitnessSum);
            rangeMax[k++] = s;
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
