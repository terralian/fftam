package com.github.terralian.fftam.search.genetic3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.github.terralian.aforge.genetic.chromosomes.ChromosomeBase;
import com.github.terralian.aforge.genetic.chromosomes.IChromosome;
import com.github.terralian.common.lang.CollectionUtil;
import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.map.IGameMap;
import com.github.terralian.fftam.map.IMapNode;
import com.github.terralian.fftam.map.MapNodeTypeEnum;

/**
 * 游戏地图染色体
 * <p>
 * 该种染色体不支持交叉操作，仅通过突变来进化
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
    private IGameMap val;

    /**
     * Random number generator for chromosoms generation, crossover, mutation, etc.
     */
    protected ThreadLocalRandom rand = ThreadLocalRandom.current();

    /**
     * 实例化地图游戏地图染色体
     * 
     * @param operateSize 突变等操作的次数，相当于每次的步长
     */
    public GameMapChromosome() {
        val = new GameMap();
        generate();
    }

    /**
     * 根据一个地图染色体来创建地图染色体
     * 
     * @param source 来源
     */
    protected GameMapChromosome(GameMapChromosome source) {
        val = source.val.clone();
        fitness = source.fitness;
    }

    @Override
    public void generate() {
        // 对默认的地图节点旗子进行洗牌，改变其顺序
        List<MapNodeTypeEnum> shuffledMapNodes = CollectionUtil.newArrayList(DEFAULT_MAP_NODE);
        Collections.shuffle(shuffledMapNodes);

        val.clearIterator();
        int j = 0;
        while (val.hasNext()) {
            val.setNode(val.next().getIndex(), shuffledMapNodes.get(j));
            j++;
        }
        val.match();
    }

    @Override
    public IChromosome createNew() {
        return new GameMapChromosome();
    }

    @Override
    public void mutate() {
        // 取得收益最小的部分，即单个不成型的目标
        List<IMapNode> minValueNodes = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            // 为该节点的连接节点按类型分组，获取其中数量为1的部分，且需要单个的不是固定的节点
            IMapNode targetNode = val.getNode(i);
            List<IMapNode> links = targetNode.getLinks();
            Map<MapNodeTypeEnum, List<IMapNode>> enumMap = CollectionUtil.groupBy(links, k -> k.getType());
            List<List<IMapNode>> linkMapValues = new ArrayList<>(enumMap.values());

            linkMapValues = CollectionUtil.filterToList(linkMapValues, k -> k.size() == 1 && !val.isReadOnlyNode(k.get(0).getIndex()));
            if (linkMapValues.size() == 0) {
                continue;
            }

            for (List<IMapNode> list : linkMapValues) {
                minValueNodes.add(list.get(0));
            }
        }

        // 若无这种节点，则突变失败
        if (minValueNodes.isEmpty()) {
            return;
        }

        // 洗牌，并拿到第一个点作为交换的最小值点
        Collections.shuffle(minValueNodes);
        IMapNode switchNodeA = minValueNodes.get(0);

        // 取的对交换最小值的点对应的最大价值的点
        // 即目标点的连接中含同类型的连接点，无论其数量是多少，只要有就会有分数（或者隐含的组合搭子）

        // 获取A的相同节点，对节点进行洗牌
        MapNodeTypeEnum nodeAType = switchNodeA.getType();
        List<IMapNode> sameTypeNodes = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            IMapNode targetNode = val.getNode(i);
            if (targetNode.getType() == nodeAType && switchNodeA.getIndex() != i) {
                sameTypeNodes.add(targetNode);
            }
        }
        Collections.shuffle(sameTypeNodes);

        // 取第一个相同节点，它的某个节点的某个连接节点将作为
        IMapNode sameTypeNodeB = sameTypeNodes.get(0);
        List<IMapNode> sameTypeNodeBLinks = new ArrayList<>(sameTypeNodeB.getLinks());
        Collections.shuffle(sameTypeNodeBLinks);

        IMapNode switchNodeB = null;
        int i = 0;
        while (switchNodeB == null && i < sameTypeNodeBLinks.size()) {
            // 选择
            IMapNode sameTypeNodeLink = sameTypeNodeBLinks.get(i);
            List<IMapNode> candidacys = sameTypeNodeLink.getLinks();
            candidacys = CollectionUtil.filterToList(candidacys, k -> k.getType() != nodeAType && !val.isReadOnlyNode(k.getIndex()));
            if (!candidacys.isEmpty()) {
                Collections.shuffle(candidacys);
                switchNodeB = candidacys.get(0);
            }
            i++;
        }

        // 找不到合适的B节点，交换失败
        if (switchNodeB == null) {
            return;
        }

        val.switchNode(switchNodeA.getIndex(), switchNodeB.getIndex());

        val.match();
    }

    @Override
    public void crossover(IChromosome pair) {
        throw new UnsupportedOperationException("地图节点染色体不支持交叉操作");
    }

    @Override
    public IChromosome clone() {
        return new GameMapChromosome(this);
    }

    @Override
    public String toString() {
        return "[" + getFitness() + "] " + val.toString();
    }

    /**
     * 获取其值
     */
    public IGameMap getValue() {
        return val;
    }
}
