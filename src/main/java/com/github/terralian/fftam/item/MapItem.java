package com.github.terralian.fftam.item;

/**
 * 地图宝物实例
 * 
 * @author terra.lian
 */
public class MapItem implements IMapItem {
    /**
     * 分数
     */
    private Integer point;
    /**
     * 宝物名称
     */
    private String name;

    /**
     * 构造一个地图宝物
     * 
     * @param point 宝物的分数
     * @param name 宝物的名称
     */
    public MapItem(Integer point, String name) {
        this.point = point;
        this.name = name;
    }

    /**
     * 通过工厂方法构建一个宝物
     * 
     * @param point 宝物的分数
     * @param name 宝物的名称
     */
    public static MapItem from(Integer point, String name) {
        return new MapItem(point, name);
    }

    @Override
    public String itemName() {
        return name;
    }

    @Override
    public Integer itemPoint() {
        return point;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MapItem //
                ? ((MapItem) obj).itemName().equals(this.itemName())
                : false;
    }

    @Override
    public int compareTo(IMapItem o) {
        int compare = o.itemPoint().compareTo(this.itemPoint());
        if (compare == 0) {
            return this.itemName().compareTo(o.itemName());
        }
        return compare;
    }
}
