package com.github.terralian.fftam.map;

import java.util.List;

/**
 * 地图的节点
 * 
 * @author terra.lian
 */
public interface IMapNode {

    /**
     * 获取地图节点的索引
     */
    int getIndex();

    /**
     * 获取该地图节点的类型枚举
     */
    MapNodeTypeEnum getType();

    /**
     * 设置该地图节点的类型枚举
     */
    void setType(MapNodeTypeEnum type);

    /**
     * 获取连接的地图节点，最多4个
     */
    List<IMapNode> getLinks();

    /**
     * 设置连接的地图节点
     * 
     * @param mapNodes 地图节点，支持多个
     */
    void setLinks(IMapNode... mapNodes);

    /**
     * 清空变化的量，如地图节点
     */
    void clear();
}
