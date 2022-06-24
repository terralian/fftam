package com.github.terralian.fftam.item;

import java.util.List;

/**
 * 地图摆放可探索的宝物
 * 
 * @author terra.lian
 */
public interface IMapItem extends Comparable<IMapItem> {

    /**
     * 宝物名称
     */
    String itemName();

    /**
     * 宝物的主观评分
     * <p>
     * 根据该数值可评价一个宝物的好坏，评分越高越好
     * <p>
     * 可以如下规定评分细则：
     * <ul>
     * <li>垃圾：0 ~ 9， 如盖义乌斯卡里古靴
     * <li>一般： 10 ~ 19，不稀有的任务道具，装备等
     * <li>稀有： 20 ~ 29， 比较稀有的道具
     * <li>特稀： 30 ~ 39， 特别稀有的道具
     * <li>传说：40 ~ 49，难获得，获取仅出产于地图探索，极好的装备道具
     * </ul>
     */
    Integer itemPoint();

    /**
     * 统计宝物的总评分
     * 
     * @param items 宝物集合
     */
    static Integer sumItemPoint(List<IMapItem> items) {
        // 过滤重复项，令不同的好装备才能提升适应度
        return items.stream().distinct().mapToInt(IMapItem::itemPoint).sum();
        // return items.stream().mapToInt(IMapItem::itemPoint).sum();
    }
}
