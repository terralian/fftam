package com.github.terralian.fftam.search.genetic1;

import java.text.MessageFormat;
import java.util.List;

import com.github.terralian.aforge.genetic.Population;
import com.github.terralian.fftam.item.IMapItem;
import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.search.IMapSearcher;

/**
 * 版本一的基于遗传算法来查找最佳摆放
 * 
 * @author terra.lian
 */
public class MapGeneticSearcher1 implements IMapSearcher {

    @Override
    public void search() {
        GameMapFitnessFunction fitnessFunction = new GameMapFitnessFunction();

        Population population = new Population(100, //
                new GameMapChromosome(10), //
                fitnessFunction, //
                new GameMapSelectionMethod());
        population.setRandomSelectionPortion(0.4);
        population.setMutationRate(1);
        population.setAutoShuffling(true);

        GameMapChromosome totalBest = null;

        int countdown = 10000;
        while (countdown > 0) {
            population.runEpoch();

            GameMapChromosome best = (GameMapChromosome) population.getBestChromosome();
            if(totalBest == null) {
                totalBest = best;
            } else if (best.getFitness() > totalBest.getFitness()) {
                totalBest = best;
            }
            
            System.out.println(MessageFormat.format("{0}. 总体最好的分数：{1}，当前最好的分数：{2}", countdown, totalBest.getFitness(), best.getFitness()));

            countdown--;
        }

        GameMap gameMap = totalBest.getValue();
        List<IMapItem> mapItems = gameMap.getMatchCacheList();
        System.out.println("========================================================");
        System.out.println("查找的地图最佳摆放：");
        System.out.println("分数：" + totalBest.getFitness());
        System.out.println("地图摆放：" + gameMap.toString());
        System.out.println("宝物一览：");
        System.out.println();
        mapItems.sort((a, b) -> {
            int compare = b.itemPoint().compareTo(a.itemPoint());
            if (compare == 0) {
                return a.itemName().compareTo(b.itemName());
            }
            return compare;
        });
        mapItems.forEach(k -> System.out.println(k.toString()));
        System.out.println();
        System.out.println("========================================================");
    }
}
