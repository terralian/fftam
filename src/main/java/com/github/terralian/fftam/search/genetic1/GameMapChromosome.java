package com.github.terralian.fftam.search.genetic1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.github.terralian.aforge.genetic.chromosomes.ChromosomeBase;
import com.github.terralian.aforge.genetic.chromosomes.IChromosome;
import com.github.terralian.common.lang.CollectionUtil;
import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.map.MapNodeTypeEnum;

/**
 * 地图基因，代表一个地图的排列，内含排列生成的所有探索物，探索物的价值与基因的适应度挂钩
 * 
 * @author terra.lian
 */
class GameMapChromosome extends ChromosomeBase {

    /**
     * 默认排序的地图的节点
     * <p>
     * 使用时将对这个数组进行洗牌，再一一存放到{@link GameMap}中，获取其价值
     */
    private static final List<MapNodeTypeEnum> DEFAULT_MAP_NODE;

    static {
        DEFAULT_MAP_NODE = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            DEFAULT_MAP_NODE.add(MapNodeTypeEnum.Street);
        }
        for (int i = 0; i < 4; i++) {
            DEFAULT_MAP_NODE.add(MapNodeTypeEnum.Mountain);
        }
        for (int i = 0; i < 4; i++) {
            DEFAULT_MAP_NODE.add(MapNodeTypeEnum.Forest);
        }
        for (int i = 0; i < 4; i++) {
            DEFAULT_MAP_NODE.add(MapNodeTypeEnum.Desert);
        }
        for (int i = 0; i < 4; i++) {
            DEFAULT_MAP_NODE.add(MapNodeTypeEnum.Steppes);
        }
        for (int i = 0; i < 2; i++) {
            DEFAULT_MAP_NODE.add(MapNodeTypeEnum.Cave);
        }
        for (int i = 0; i < 2; i++) {
            DEFAULT_MAP_NODE.add(MapNodeTypeEnum.River);
        }
        for (int i = 0; i < 3; i++) {
            DEFAULT_MAP_NODE.add(MapNodeTypeEnum.DeadStreet);
        }
    }

    /**
     * 地图模型来代表其值
     */
    private GameMap val;

    /**
     * Random number generator for chromosoms generation, crossover, mutation, etc.
     */
    protected ThreadLocalRandom rand = ThreadLocalRandom.current();

    private int operateSize;

    @Override
    public String toString() {
        return "[" + getFitness() + "] " + val.toString();
    }

    /**
     * 实例化地图游戏地图染色体
     * 
     * @param operateSize 突变等操作的次数，相当于每次的步长
     */
    public GameMapChromosome(int operateSize) {
        val = new GameMap();
        this.operateSize = operateSize;
        generate();
    }

    /**
     * 根据一个地图染色体来创建地图染色体
     * 
     * @param source 来源
     */
    protected GameMapChromosome(GameMapChromosome source) {
        val = source.val;
        fitness = source.fitness;
        operateSize = source.operateSize;
    }

    @Override
    public void generate() {
        // 对默认的地图节点旗子进行洗牌，改变其顺序
        List<MapNodeTypeEnum> shuffledMapNodes = CollectionUtil.newArrayList(DEFAULT_MAP_NODE);
        Collections.shuffle(shuffledMapNodes);

        // 按顺序存放节点
        int j = 0;
        for (int i = 0; i < 30; i++) {
            // 固定节点
            if (i == 8 || i == 12 || i == 14)
                continue;
            val.setNode(i, shuffledMapNodes.get(j));
            j++;
        }

        val.match();
    }

    @Override
    public IChromosome createNew() {
        return new GameMapChromosome(operateSize);
    }

    @Override
    public IChromosome clone() {
        return new GameMapChromosome(this);
    }

    @Override
    public void mutate() {
        // 随机调换其中的某个节点
        for (int i = 0; i < operateSize; i++) {
            int from = rand.nextInt(GameMap.EDITABLE_INDEX.length);
            int to = rand.nextInt(GameMap.EDITABLE_INDEX.length);
            while (from == to) {
                to = rand.nextInt(GameMap.EDITABLE_INDEX.length);
            }
            val.switchNode(GameMap.EDITABLE_INDEX[from], GameMap.EDITABLE_INDEX[to]);
        }
        val.match();
    }

    @Override
    public void crossover(IChromosome pair) {
        // 由于染色体基因是固定的，不同的是其排列顺序
        // 两个染色体间交换部分排列顺序理论上是做不到的
        // 当种群过于固定时，交叉的效率很低
        // 这里将交叉当做突变执行
        GameMapChromosome p = (GameMapChromosome) pair;

        for (int i = 0; i < operateSize; i++) {
            this.mutate();
            p.mutate();
        }
    }

    /**
     * 获取其值
     */
    public GameMap getValue() {
        return val;
    }
}
