package com.github.terralian.fftam.map;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.terralian.fftam.item.IMapItem;
import com.github.terralian.fftam.item.IMapItemMatcher;

/**
 * 游戏地图接口
 * <p>
 * 最终幻想战略版游戏地图的模型，可对其操作设置移除节点，以计算某个组合下的探索物
 * <p>
 * 该接口支持迭代器{@link Iterator}，从0开始进行设置节点，其返回值会跳过固定部分
 * 
 * @author terra.lian
 */
public interface IGameMap extends Iterator<IMapNode> {

    /**
     * 根据索引获取对应的节点
     * 
     * @param index 节点索引
     */
    IMapNode getNode(int index);

    /**
     * 设置地图节点的类型
     * 
     * @param index 地图索引
     * @param nodeType 节点类型
     */
    void setNode(int index, MapNodeTypeEnum nodeType);

    /**
     * 交换两个节点的地图类型
     * 
     * @param indexA 地图节点A
     * @param indexB 地图节点B
     */
    void switchNode(int indexA, int indexB);

    /**
     * 交换两个节点的连接点类型
     * 
     * @param indexA 地图节点A
     * @param indexB 地图节点B
     */
    void switchLink(int indexA, int indexB);

    /**
     * 根据连接数获取相同连接数的所有节点索引
     * 
     * @param linkSize 连接数
     */
    List<Integer> getNodeIndexByLinkSize(Integer linkSize);

    /**
     * 清空某个节点的类型
     * 
     * @param index 地图索引
     */
    void clearNode(int index);

    /**
     * 清空所有节点，同时清空其计算缓存的地图宝物，还原节点
     * <p>
     * 通过该方法可以还原实例
     */
    void clearAll();

    /**
     * 计算该组合下的宝物探索
     * <p>
     * 一般该方法仅在所有节点设置完成后调用，每次调用都会重新计算
     * <p>
     * 若在一次匹配之外还需要再获取结果，可使用{@link #getMatchCacheList()}方法
     */
    Map<Integer, List<IMapItem>> match();

    /**
     * 返回当前匹配的缓存值，未匹配情况下也不会报错，会返回空集合
     * <p>
     * 其结果会自动排序（按最优宝物到最低宝物）
     */
    List<IMapItem> getMatchCacheList();


    /**
     * 返回当前匹配的缓存值，未匹配情况下也不会报错，会返回空集合
     */
    Map<Integer, List<IMapItem>> getMatchCacheMap();

    /**
     * 是否含下一个节点，是返回True，否返回False
     */
    @Override
    boolean hasNext();

    /**
     * 返回其下一个节点
     */
    @Override
    IMapNode next();

    /**
     * 还原迭代器，还原其下一个节点从0开始
     */
    void clearIterator();

    /**
     * 设置新宝物探索匹配器，以替换默认的实现
     * 
     * @param itemMatcher 新的宝物探索匹配器
     */
    void setItemMatcher(IMapItemMatcher itemMatcher);

    /**
     * 获取可操作节点的大小
     */
    int editableNodeSize();

    /**
     * 通过索引找可操作节点的实际节点索引
     * <p>
     * 可操作节点一般用一个数组记录，过滤其中不可操作的节点，所以两者并非一一对应
     * 
     * @param index 索引
     */
    int editableNodeIndex(int index);

    /**
     * 是否只读节点
     * 
     * @param index 节点
     */
    boolean isReadOnlyNode(int index);

    /**
     * 该节点是否连接着只读的固定节点
     * 
     * @param index 节点索引
     */
    boolean hasLinkReadOnlyNode(int index);

    /**
     * 允许克隆
     */
    IGameMap clone();

    /**
     * 将节点索引拼接打印字符串
     */
    String toNodeTypeString();
}
