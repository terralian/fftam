// AForge Genetic Library
// AForge.NET framework
// http://www.aforgenet.com/framework/
//
// Copyright © Andrew Kirillov, 2006-2009
// andrew.kirillov@aforgenet.com
//
package com.github.terralian.aforge.genetic.chromosomes;

import com.github.terralian.aforge.genetic.fitness.IFitnessFunction;

/**
 * 染色体基类.
 * <p>
 * 该基类提供了一些{@link IChromosome}接口的通用方法实现和通用属性，这些属性和方法在所有染色体间一致.
 */
public abstract class ChromosomeBase implements IChromosome {

    /** 染色体的适应度值. */
    protected double fitness = 0;

    @Override
    public void evaluate(IFitnessFunction function) {
        fitness = function.evaluate(this);
    }

    /**
     * 比较两个染色体.
     * 
     * @param o 需要比较的二元染色体（Binary chromosome）.
     * @return 返回比较结果，两者适应度值相等返回0，此染色体小于参数返回1，大于则返回-1
     */
    @Override
    public int compareTo(IChromosome o) {
        double f = ((ChromosomeBase) o).fitness;
        return (fitness == f) ? 0 : (fitness < f) ? 1 : -1;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public IChromosome clone() {
        throw new UnsupportedOperationException();
    }
}
