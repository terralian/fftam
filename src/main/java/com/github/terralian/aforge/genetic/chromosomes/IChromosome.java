// AForge Genetic Library
// AForge.NET framework
// http://www.aforgenet.com/framework/
//
// Copyright � Andrew Kirillov, 2006-2009
// andrew.kirillov@aforgenet.com
//
package com.github.terralian.aforge.genetic.chromosomes;

import com.github.terralian.aforge.genetic.fitness.IFitnessFunction;

/**
 * 染色体接口.
 * <p>
 * 该接口应该由实现特定染色体类型的所有类实现.
 */
public interface IChromosome extends Comparable<IChromosome> {

    /**
     * 获得染色体的适应度.
     * <p>
     * 适应度的值表示染色体是否有用，值越大，表示越有用.
     */
    double getFitness();

    /**
     * 生成随机染色体值.
     * <p>
     * 使用随机数生成器重新生成染色体的值
     */
    void generate();
    
    /**
     * 使用相同的参数创建一个新的随机数染色体（工厂方法）.
     * <p>
     * 该方法创建相同类型的染色体，但是随机初始化. 该方法作为工厂方法，用于某些处理染色体接口，但是不管指定染色体类型的类.
     */
    IChromosome createNew();

    /**
     * 克隆染色体.
     * <p>
     * 该方法返回一个精确的染色体克隆.
     * 
     * @return 返回该染色体的克隆.
     */
    IChromosome clone();

    /**
     * 突变操作.
     * <p>
     * 该方法执行染色体突变，随机的改变其部分.
     */
    void mutate();

    /**
     * 交叉操作.
     * <p>
     * 该方法对两个染色体执行交叉操作，互换两者的一部分.
     * 
     * @param pair 需要交换的配对的染色体.
     */
    void crossover(IChromosome pair);

    /**
     * 用指定的适应度函数计算染色体.
     * <p>
     * 使用指定的适应度函数计算染色体的适应度.
     * 
     * @param function 用于染色体评估的适应度函数.
     */
    void evaluate(IFitnessFunction function);
}
