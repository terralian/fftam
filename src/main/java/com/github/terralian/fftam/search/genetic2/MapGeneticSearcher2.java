package com.github.terralian.fftam.search.genetic2;

import java.text.MessageFormat;
import java.util.List;

import com.github.terralian.fftam.item.IMapItem;
import com.github.terralian.fftam.map.IGameMap;
import com.github.terralian.fftam.search.IMapSearcher;

/**
 * 版本2的遗传算法查找最佳摆放
 * <p>
 * 该版本对使用版本1发现的问题进行优化
 * <ul>
 * <li>使用代的概念，令年轻和中年代存活的几率加大，而老年代的存活几率逐步减小
 * <li>变异和交叉的含义在该版本的意义重定义，更支持排序这个场景
 * <li>定向突变，令突变的价值更高，相当于剪枝，减少搜索空间
 * </ul>
 * 
 * @author terra.lian
 */
public class MapGeneticSearcher2 implements IMapSearcher {

    @Override
    public void search() {
        GameMapFitnessFunction fitnessFunction = new GameMapFitnessFunction();

        GameMapPopulation population = new GameMapPopulation(100, //
                new GameMapChromosome(200, true), //
                fitnessFunction, //
                new GameMapSelectionMethod());
        population.setMutationRate(1);
        population.setAutoShuffling(true);

        GameMapChromosome totalBest = null;

        int countdown = 40000;
        while (countdown > 0) {
            population.runEpoch();

            GameMapChromosome best = (GameMapChromosome) population.getBestChromosome().clone();
            if (totalBest == null) {
                totalBest = best;
            } else if (best.getFitness() > totalBest.getFitness()) {
                totalBest = best;
            }

            System.out.println(MessageFormat.format("{0}. 总体最好的分数：{1}，当前最好的分数：{2}, 当前种群大小：{3}", //
                    countdown, //
                    String.valueOf(totalBest.getFitness()), //
                    String.valueOf(best.getFitness()), //
                    String.valueOf(population.getSize()))); //

            countdown--;
        }

        IGameMap gameMap = totalBest.getValue();
        List<IMapItem> mapItems = gameMap.getMatchCacheList();
        System.out.println("========================================================");
        System.out.println("查找的地图最佳摆放：");
        System.out.println("分数：" + String.valueOf(totalBest.getFitness()));
        System.out.println("地图摆放：" + gameMap.toString());
        System.out.println("地图映射值：" + gameMap.toNodeTypeString());
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
