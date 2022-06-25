package com.github.terralian.fftam.search.genetic2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import com.github.terralian.aforge.genetic.chromosomes.ChromosomeBase;
import com.github.terralian.aforge.genetic.chromosomes.IChromosome;
import com.github.terralian.common.lang.CollectionUtil;
import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.map.IGameMap;
import com.github.terralian.fftam.map.IMapNode;
import com.github.terralian.fftam.map.MapNodeTypeEnum;

/**
 * 版本2的代概念染色体
 * <p>
 * 该染色体含有年龄和代的概念，年龄越小死亡的概率越低，到老年代死亡几率突升，直到寿命结束。这样可以保证一个染色体有足够的时间来探索排序
 * <p>
 * 该染色体重新定义了突变和交叉
 * <p>
 * 
 * 对于突变，定义为随机取一个价值较小的点，找到一个对于该点价值较大的点进行交换.
 * <ul>
 * <li>价值较小的点：对产生探索宝物的价值较小，即因为它而生成的宝物的价值较小
 * <li>价值较大的点：交换后的连接点产生较大价值的点，即因为它交换可以产生较大的价值，但是由于该点不好找，因此定义为，交换后和它相同的连接点为2个为最佳，1个为最差
 * </ul>
 * 
 * <p>
 * 对于交叉，定义为，随机取两个相同连接数的点，交换其所有连接点，以此来探索不同形状在不同点的价值可能性
 * <ul>
 * <li>该行为仅会在成年后（中年代）发生，在少年和老年则不会
 * <li>少年代由于排列太过随机价值太小，而老年代则由于太过集中，可能以丧失变化的可能，不过不确定，需要实际程序运行后确认
 * </ul>
 * 备用设定也可以是，生命越接近结束，则越想留下后代，其交叉概率越高
 * <p>
 * 
 * @author terra.lian
 */
class GameMapChromosome extends ChromosomeBase implements IGenerationChromosome {

    /**
     * 默认排序的地图的节点
     * <p>
     * 使用时将对这个数组进行洗牌，再一一存放到{@link GameMap}中，获取其价值
     */
    private static final List<MapNodeTypeEnum> DEFAULT_MAP_NODE;
    static {
        DEFAULT_MAP_NODE = new ArrayList<>();
        BiConsumer<MapNodeTypeEnum, Integer> consumer = (node, size) -> {
            for (int i = 0; i < size; i++) {
                DEFAULT_MAP_NODE.add(node);
            }
        };
        consumer.accept(MapNodeTypeEnum.Street, 4);
        consumer.accept(MapNodeTypeEnum.Mountain, 4);
        consumer.accept(MapNodeTypeEnum.Forest, 4);
        consumer.accept(MapNodeTypeEnum.Desert, 4);
        consumer.accept(MapNodeTypeEnum.Steppes, 3);
        consumer.accept(MapNodeTypeEnum.Cave, 2);
        consumer.accept(MapNodeTypeEnum.River, 2);
        consumer.accept(MapNodeTypeEnum.DeadStreet, 3);
    }

    /**
     * 年龄
     */
    private int age;

    /**
     * 最大年龄
     */
    private int maxAge;

    /**
     * 地图模型来代表其值
     */
    private IGameMap val;

    /**
     * 随机数发生器
     */
    protected ThreadLocalRandom rand = ThreadLocalRandom.current();

    /**
     * 部分交叉概率，即仅交换单个节点中相同的部分
     */
    private double crossoverPartRate = 0.0;

    /**
     * 实例化一个代地图染色体
     */
    public GameMapChromosome() {
        this(100);
    }

    /**
     * 根据最大年龄实例化一个代地图染色体
     * 
     * @param maxAge 最大年龄
     */
    public GameMapChromosome(int maxAge) {
        this.maxAge = maxAge;
        this.age = 0;
        val = new GameMap();
        generate();
    }

    /**
     * 根据最大年龄实例化一个代地图染色体
     * 
     * @param maxAge 最大年龄
     */
    public GameMapChromosome(int maxAge, boolean randomAge) {
        this.maxAge = maxAge;
        this.age = randomAge ? rand.nextInt(maxAge) : 0;
        val = new GameMap();
        generate();
    }

    /**
     * 根据一个地图染色体来创建地图染色体
     * 
     * @param source 来源
     */
    protected GameMapChromosome(GameMapChromosome source) {
        this.maxAge = source.maxAge;
        this.age = 0;
        val = source.val.clone();
        fitness = source.fitness;
    }

    @Override
    public void generate() {
        // 对默认的地图节点旗子进行洗牌，改变其顺序
        List<MapNodeTypeEnum> shuffledMapNodes = CollectionUtil.newArrayList(DEFAULT_MAP_NODE);
        Collections.shuffle(shuffledMapNodes);

        int i = 0;
        while (val.hasNext()) {
            IMapNode mapNode = val.next();
            mapNode.setType(shuffledMapNodes.get(i));
            i++;
        }
        val.match();
    }

    @Override
    public IChromosome createNew() {
        return new GameMapChromosome(this.maxAge);
    }

    @Override
    public IChromosome clone() {
        return new GameMapChromosome(this);
    }

    /**
     * 定向突变，价值越小交换几率越高，交换点为归集点（即包含相同类型的点，以只有一个点的价值最高，0个点的价值最低）
     */
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

    /**
     * 交换两个随机相同连接数节点的所有连接点，或者交换其数量相同的一部分
     */
    @Override
    public void crossover(IChromosome pair) {
        double r = rand.nextDouble();

        // 连接节点数量有效的为[2, 4]
        int linkSize = rand.nextInt(2) + 2;
        List<Integer> origins = val.getNodeIndexByLinkSize(linkSize);
        // 过滤其中的不可交换节点
        List<Integer> originLinks = origins.stream() //
                .filter(k -> !val.hasLinkReadOnlyNode(k)) //
                .collect(Collectors.toList());

        // 交换失败
        if (originLinks.size() < 2) {
            return;
        }

        // 进行洗牌，所选目标则为洗牌后的第一个节点
        Collections.shuffle(originLinks);
        Integer indexA = originLinks.get(0);

        // 节点的全连接交换
        if (r > crossoverPartRate) {
            // 交换节点为第二个节点
            Integer indexB = originLinks.get(1);
            val.switchLink(indexA, indexB);
        }

        // 交换其中部分节点对到同样数量的节点对（类型不同）
        else {
            // 取目标A点，为其所有节点进行洗牌
            IMapNode nodeA = val.getNode(indexA);
            List<IMapNode> linkA = nodeA.getLinks();
            Collections.shuffle(new ArrayList<>(linkA));

            // 取第一个节点类型，将所有该节点的连接点中包含这些节点的进行过滤
            MapNodeTypeEnum sampleNodeType = linkA.get(0).getType();
            List<IMapNode> mapNodeAs = linkA.stream() //
                    .filter(k -> k.getType() == sampleNodeType) //
                    .collect(Collectors.toList());

            // 从其他节点中，过滤其中合适的部分，随机选择其中一个合适的类型，交换其中相等的部分
            List<IMapNode> mapNodeBs = new ArrayList<>();
            int loopDownCount = 30;
            while (mapNodeBs.size() > 0 && loopDownCount > 0) {
                loopDownCount--;

                // 选30中的一个点，且这个点不能等于A，将这个作为B的索引
                int bSeed = rand.nextInt(30);
                if (bSeed == nodeA.getIndex()) {
                    continue;
                }

                // 取B的连接，过滤掉所有不合适的连接
                List<IMapNode> links = val.getNode(bSeed).getLinks();
                links = CollectionUtil.filterToList(links, k -> val.hasLinkReadOnlyNode(k.getIndex()) && k.getType() != sampleNodeType);
                // 按类型进行分组，将分组后的值存为List，过滤其中数量不正确的部分
                Map<MapNodeTypeEnum, List<IMapNode>> enumMap = CollectionUtil.groupBy(links, k -> k.getType());
                List<List<IMapNode>> linkMapValues = new ArrayList<>(enumMap.values());
                linkMapValues = CollectionUtil.filterToList(linkMapValues, k -> k.size() > mapNodeAs.size());
                if (linkMapValues.size() == 0) {
                    continue;
                }
                // 对其进行洗牌，取其中的第一个节点，为数组进行洗牌
                Collections.shuffle(linkMapValues);
                List<IMapNode> targetNodes = linkMapValues.get(0);
                Collections.shuffle(targetNodes);

                // 将头部相等的部分集合加入到需要交换的集合
                for (int i = 0; i < mapNodeAs.size(); i++) {
                    mapNodeBs.add(targetNodes.get(i));
                }
            }

            // 多次内拿不到需要的，交换失败
            if (mapNodeBs.isEmpty()) {
                return;
            }

            // 进行交换
            for (int i = 0; i < mapNodeAs.size(); i++) {
                val.switchNode(mapNodeAs.get(i).getIndex(), mapNodeBs.get(i).getIndex());
            }
        }

        val.match();
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public int ageAdd() {
        age++;
        return age;
    }

    @Override
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }

    /**
     * 获取其值
     */
    public IGameMap getValue() {
        return val;
    }
}
