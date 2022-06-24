package com.github.terralian.fftam.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.github.terralian.fftam.item.IMapItem;
import com.github.terralian.fftam.item.IMapItemMatcher;
import com.github.terralian.fftam.item.MapItemMatcher;

/**
 * 表示一个地图实例
 * 
 * @author terra.lian
 */
public class GameMap implements IGameMap {

    /**
     * 地图的节点，共30个，按斜上进行标号
     * <p>
     * 其中固定的王宫为8，最终山脉为12，初始街道14
     */
    private List<IMapNode> mapNodes;
    /**
     * 地图节点的连接数索引
     * <p>
     * K为连接数，V为相同连接数的地图节点，用于快速查找所需相同连接数的节点
     */
    private Map<Integer, List<Integer>> linkSizeIndexMap;

    /**
     * 除8,12,14外其余都是可编辑节点
     */
    public static final int[] EDITABLE_INDEX =
            new int[] {0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 13, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29};

    /**
     * 地图探索宝物匹配器
     */
    private IMapItemMatcher itemMatcher;

    // 可变值
    /**
     * 地图宝物，以地图节点的索引映射
     */
    private Map<Integer, List<IMapItem>> mapItemMap;

    /**
     * 迭代器索引
     */
    private int iteratorIndex;

    /**
     * 实例化地图
     */
    public GameMap() {
        // 初始化地图
        this.initMap();
        // 初始化地图宝物
        mapItemMap = new HashMap<>();
        // 初始化宝物匹配器
        itemMatcher = new MapItemMatcher();
        // 迭代器索引设置为0
        iteratorIndex = 0;
    }

    @Override
    public IMapNode getNode(int index) {
        return mapNodes.get(index);
    }

    /**
     * 设置节点
     * 
     * @param index 地图索引
     * @param nodeType 节点类型
     */
    @Override
    public void setNode(int index, MapNodeTypeEnum nodeType) {
        // 设置该节点类型
        IMapNode targetNode = mapNodes.get(index);
        targetNode.setType(nodeType);
    }

    /**
     * 交换两个节点
     * 
     * @param indexFrom 从节点
     * @param indexTo 到节点
     */
    @Override
    public void switchNode(int indexFrom, int indexTo) {
        assertNotReadOnlyNode(indexFrom);
        assertNotReadOnlyNode(indexTo);

        MapNodeTypeEnum nodeTypeFrom = getMapNode(indexFrom).getType();
        clearNode(indexFrom);
        MapNodeTypeEnum nodeTypeTo = getMapNode(indexTo).getType();
        clearNode(indexTo);

        setNode(indexFrom, nodeTypeTo);
        setNode(indexTo, nodeTypeFrom);
    }

    @Override
    public void switchLink(int indexA, int indexB) {
        IMapNode nodeA = getMapNode(indexA);
        IMapNode nodeB = getMapNode(indexB);

        int linkSize = nodeA.getLinks().size();
        if (linkSize != nodeB.getLinks().size()) {
            throw new IllegalArgumentException("需要交换连接点类型的目标参数点的连接数不相等，A: " + linkSize + " B：" + nodeB.getLinks().size());
        }

        for (int i = 0; i < linkSize; i++) {
            switchNode(nodeA.getLinks().get(i).getIndex(), nodeB.getLinks().get(i).getIndex());
        }
    }

    /**
     * 根据连接数获取相同连接数的所有节点索引
     * 
     * @param linkSize 连接数
     */
    @Override
    public List<Integer> getNodeIndexByLinkSize(Integer linkSize) {
        return linkSizeIndexMap.get(linkSize);
    }

    /**
     * 移除节点
     * 
     * @param index 节点索引
     */
    @Override
    public void clearNode(int index) {
        IMapNode targetNode = mapNodes.get(index);
        targetNode.clear();
    }

    /**
     * 清空所有节点，同时清空其计算缓存的地图宝物
     */
    @Override
    public void clearAll() {
        // 清空地图宝物
        mapItemMap.clear();
        // 清除所有地图类型
        for (IMapNode mapNode : mapNodes) {
            mapNode.clear();
        }
        // 设置固定的节点
        setConstantNode();
        // 还原迭代器索引
        clearIterator();
    }

    /**
     * 获取对应的地图节点
     * 
     * @param index 索引
     */
    public IMapNode getMapNode(int index) {
        return mapNodes.get(index);
    }

    /**
     * 为地图执行探索宝物
     */
    @Override
    public Map<Integer, List<IMapItem>> match() {
        mapItemMap.clear();
        for (int i = 0; i < 30; i++) {
            IMapNode targetNode = mapNodes.get(i);
            List<IMapItem> items = itemMatcher.match(targetNode);
            mapItemMap.put(i, items);
        }
        return mapItemMap;
    }

    /**
     * 获取地图生成的宝物集合
     * <p>
     * 其结果会自动排序（按最优宝物到最低宝物）
     */
    @Override
    public List<IMapItem> getMatchCacheList() {
        List<IMapItem> mapItems = new ArrayList<>();
        for (List<IMapItem> value : mapItemMap.values()) {
            mapItems.addAll(value);
        }
        mapItems.sort((a, b) -> a.compareTo(b));

        return mapItems;
    }

    @Override
    public Map<Integer, List<IMapItem>> getMatchCacheMap() {
        return mapItemMap;
    }

    /**
     * 是否含下一个节点，是返回True，否返回False
     */
    @Override
    public boolean hasNext() {
        return iteratorIndex < EDITABLE_INDEX.length;
    }

    /**
     * 返回其下一个节点
     */
    @Override
    public IMapNode next() {
        IMapNode mapNode = getMapNode(EDITABLE_INDEX[iteratorIndex]);
        iteratorIndex++;
        return mapNode;
    }

    /**
     * 还原迭代器，还原其下一个节点从0开始
     */
    @Override
    public void clearIterator() {
        this.iteratorIndex = 0;
    }

    /**
     * 设置新宝物探索匹配器，以替换默认的实现
     * 
     * @param itemMatcher 新的宝物探索匹配器
     */
    @Override
    public void setItemMatcher(IMapItemMatcher itemMatcher) {
        this.itemMatcher = itemMatcher;
    }

    /**
     * 获取可操作节点的大小
     */
    @Override
    public int editableNodeSize() {
        return EDITABLE_INDEX.length;
    }

    /**
     * 通过索引找可操作节点的实际节点索引
     * <p>
     * 可操作节点一般用一个数组记录，过滤其中不可操作的节点，所以两者并非一一对应
     * 
     * @param index 索引
     */
    @Override
    public int editableNodeIndex(int index) {
        return EDITABLE_INDEX[index];
    }

    @Override
    public boolean isReadOnlyNode(int index) {
        return index == 8 || index == 12 || index == 14;
    }

    @Override
    public boolean hasLinkReadOnlyNode(int index) {
        IMapNode mapNode = getMapNode(index);
        List<IMapNode> links = mapNode.getLinks();
        return links.stream().anyMatch(k -> isReadOnlyNode(k.getIndex()));
    }

    /**
     * 打印地图，会将其的排序作为值
     */
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",");
        for (IMapNode cs : mapNodes) {
            joiner.add(cs.toString());
        }
        return joiner.toString();
    }

    @Override
    public String toNodeTypeString() {
        StringJoiner joiner = new StringJoiner(",");
        for (IMapNode cs : mapNodes) {
            joiner.add(String.valueOf(cs.getType().getTypeCode()));
        }
        return joiner.toString();
    }

    @Override
    public IGameMap clone() {
        IGameMap gameMap = new GameMap();
        for (int i = 0; i < editableNodeSize(); i++) {
            int actualIndex = editableNodeIndex(i);
            gameMap.setNode(actualIndex, this.getMapNode(actualIndex).getType());
        }
        gameMap.match();
        return gameMap;
    }

    // ------------------------------------------------------
    // 私有方法
    // ------------------------------------------------------

    /**
     * 初始化地图
     */
    private void initMap() {
        mapNodes = new ArrayList<>(30);
        for (int i = 0; i < 30; i++) {
            mapNodes.add(MapNode.empty(i));
        }
        linkSizeIndexMap = new HashMap<>();

        // 设置连接
        // 第一排
        setMapLink(0, 1, 4);
        setMapLink(1, 0, 2, 4);
        setMapLink(2, 1, 5);

        // 第二排
        setMapLink(3, 4, 7, 9);
        setMapLink(4, 0, 1, 3, 5);
        setMapLink(5, 2, 4, 6, 9);
        setMapLink(6, 5, 10, 11);

        // 第三排
        setMapLink(7, 3, 8, 13);
        setMapLink(8, 7, 14);
        setMapLink(9, 3, 5, 10);
        setMapLink(10, 9, 6, 16);
        setMapLink(11, 6, 12, 16);
        setMapLink(12, 11);

        // 第四排
        setMapLink(13, 7, 14, 17, 19);
        setMapLink(14, 8, 13, 15, 20);
        setMapLink(15, 14, 16, 21);
        setMapLink(16, 10, 11, 15, 22);

        // 第五排
        setMapLink(17, 13, 18, 23);
        setMapLink(18, 17, 19, 24);
        setMapLink(19, 13, 18, 20, 25);
        setMapLink(20, 14, 19, 26, 27);
        setMapLink(21, 15, 22, 27);
        setMapLink(22, 16, 21);

        // 第6排
        setMapLink(23, 17, 28);
        setMapLink(24, 18, 25, 28);
        setMapLink(25, 19, 24, 26);
        setMapLink(26, 20, 25, 29);
        setMapLink(27, 20, 21, 29);

        // 第七排
        setMapLink(28, 23, 24, 29);
        setMapLink(29, 26, 27, 28);

        // 设置固定节点
        setConstantNode();
    }

    /**
     * 设置地图连接
     */
    private void setMapLink(int nodeIndex, int... linkIndexs) {
        IMapNode mapNode = mapNodes.get(nodeIndex);
        for (int i : linkIndexs) {
            mapNode.setLinks(mapNodes.get(i));
        }

        // 索引
        setLinkSizeIndex(nodeIndex, linkIndexs.length);
    }

    /**
     * 设置地图的连接数索引
     * 
     * @param nodeIndex 节点的索引
     * @param linkSize 节点的连接数
     */
    private void setLinkSizeIndex(Integer nodeIndex, Integer linkSize) {
        List<Integer> list = linkSizeIndexMap.get(linkSize);
        if (list == null) {
            list = new ArrayList<>();
            linkSizeIndexMap.put(linkSize, list);
        }
        list.add(nodeIndex);
    }

    /**
     * 设置固定节点
     */
    private void setConstantNode() {
        mapNodes.get(8).setType(MapNodeTypeEnum.Street);
        mapNodes.get(12).setType(MapNodeTypeEnum.Mountain);
        mapNodes.get(14).setType(MapNodeTypeEnum.Street);
    }


    private void assertNotReadOnlyNode(int index) {
        if (index == 8 || index == 12 || index == 14) {
            throw new IllegalArgumentException("节点" + index + "是固定节点");
        }
    }
}
