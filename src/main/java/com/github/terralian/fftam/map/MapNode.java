package com.github.terralian.fftam.map;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图节点实例
 * 
 * @author terra.lian
 */
public class MapNode implements IMapNode {

    /**
     * 连接的地图节点
     */
    private List<IMapNode> links;
    /**
     * 节点的类型
     */
    private MapNodeTypeEnum type;
    /**
     * 编号
     */
    private Integer no;

    /**
     * 实例化地图节点
     * 
     * @param no 地图节点编号
     */
    public MapNode(Integer no) {
        links = new ArrayList<>(4);
        this.no = no;
    }

    /**
     * 工厂构建地图节点
     * 
     * @param no 地图节点编号
     */
    public static MapNode empty(Integer no) {
        return new MapNode(no);
    }

    @Override
    public int getIndex() {
        return no;
    }

    @Override
    public MapNodeTypeEnum getType() {
        return type;
    }

    @Override
    public void setType(MapNodeTypeEnum type) {
        if (this.type != null) {
            throw new IllegalStateException("当前节点已有类型，请检查代码");
        }
        this.type = type;
    }

    @Override
    public List<IMapNode> getLinks() {
        return links;
    }

    @Override
    public void setLinks(IMapNode... mapNodes) {
        for (IMapNode mapNode : mapNodes) {
            links.add(mapNode);
        }
    }

    @Override
    public void clear() {
        type = null;
    }

    @Override
    public String toString() {
        String type = this.type == null ? "空" : this.type.toString();
        return "[" + no + "]" + type;
    }
}
