package com.github.terralian.fftam.item;

import java.util.List;

import com.github.terralian.fftam.map.IMapNode;

/**
 * 地图探索宝物匹配器
 * 
 * @author terra.lian
 */
public interface IMapItemMatcher {

    /**
     * 匹配节点产生的对应宝物
     * <p>
     * 以节点为中心，将其连接的所有节点类型作为条件，判断中心会生成的宝物
     * 
     * @param mapNode 地图节点
     */
    List<IMapItem> match(IMapNode mapNode);

    /**
     * 匹配节点产生的对应宝物
     * <p>
     * 该方法需要根据新节点来筛除已判断过的临接节点，仅判断新节点产生的影响
     * 
     * @param mapNode 地图节点
     * @param newNode 新的地图节点
     */
    List<IMapItem> match(IMapNode mapNode, IMapNode newNode);
}
