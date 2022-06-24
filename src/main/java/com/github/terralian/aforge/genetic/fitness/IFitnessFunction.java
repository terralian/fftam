// AForge Genetic Library
// AForge.NET framework
// http://www.aforgenet.com/framework/
//
// Copyright � Andrew Kirillov, 2006-2009
// andrew.kirillov@aforgenet.com
//
package com.github.terralian.aforge.genetic.fitness;

import com.github.terralian.aforge.genetic.chromosomes.IChromosome;

/**
 * 适应度函数接口.
 * <p>
 * 该接口应该由所有适应度函数实现，这些函数用于计算染色体的适应度值.
 * 所有适应度函数都应该返回正值（<b>大于0</b>），适应度用于评估染色体有多好 - 值越大，染色体越好
 */
@FunctionalInterface
public interface IFitnessFunction {

    /**
     * 评估染色体.
     * <p>
     * 该方法对指定染色体计算适应度
     * 
     * @param chromosome 需要评估的染色体.
     * @return Returns 染色体的适应度.
     */
    double evaluate(IChromosome chromosome);
}
