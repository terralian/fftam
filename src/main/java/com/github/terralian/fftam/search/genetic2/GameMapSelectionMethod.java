package com.github.terralian.fftam.search.genetic2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.github.terralian.aforge.genetic.chromosomes.IChromosome;
import com.github.terralian.aforge.genetic.selection.ISelectionMethod;
import com.github.terralian.common.lang.CollectionUtil;
import com.github.terralian.fftam.search.genetic2.IGenerationChromosome.Generation;

/**
 * 选择算法
 * <p>
 * 基于代进行选择
 * <ul>
 * <li>寿命为0的个体将会被去除
 * <li>按不同代，选择不同的死亡率，根据其适应度来减小死亡率，按死亡率随机选择是否死亡。
 * </ul>
 * 
 * @author terra.lian
 */
class GameMapSelectionMethod implements ISelectionMethod {

    // 随机数生成器
    private static ThreadLocalRandom rand = ThreadLocalRandom.current();

    @Override
    public void applySelection(List<IChromosome> chromosomes, int size) {
        List<GameMapChromosome> newPopulation = new ArrayList<>();
        for(IChromosome chromosome: chromosomes) {
            newPopulation.add((GameMapChromosome) chromosome);
        }
        Collections.sort(newPopulation, (a, b) -> new Double(b.getFitness()).compareTo(a.getFitness()));
        
        // 寿命已尽的个体清除
        newPopulation = CollectionUtil.filterToList(newPopulation, k -> k.getAge() < k.getMaxAge());

        chromosomes.clear();
        // 不同代的按不同死亡比例，根据适应度来减小其意外死亡的记录
        for (GameMapChromosome popilation : newPopulation) {
            double deathRate = getDeathRate(popilation.getGeneration(), popilation.getFitness());

            double r = rand.nextDouble();

            if (r > deathRate) {
                chromosomes.add(popilation);
            }
        }
    }

    /**
     * 获取死亡率
     * 
     * @param generation 代别
     */
    private double getDeathRate(Generation generation, double fitness) {
        double deathRate = 0.0;
        if (generation == Generation.OLDER) {
            // 老年代提升到30%的死亡
            deathRate = 0.3;
        }

        // 中年代为10%的死亡率
        else if (generation == Generation.MIDDLE) {
            deathRate = 0.1;
        }

        if (deathRate <= 0)
            return deathRate;

        double tmp = Math.max(0, fitness - 500) / 5000;
        return Math.max(0, deathRate - tmp);
    }
}
