package com.github.terralian.fftam.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.terralian.fftam.map.IMapNode;
import com.github.terralian.fftam.map.MapNodeTypeEnum;

/**
 * 地图探索宝物匹配器实例
 * 
 * @author terra.lian
 */
public class MapItemMatcher implements IMapItemMatcher {

    /**
     * 匹配的宝物
     */
    private static List<Map<Integer, IMapItem>> itemMatchMapList;
    static {
        initItemMatchMapList();
    }

    /**
     * 实例化
     */
    public MapItemMatcher() {
    }

    @Override
    public List<IMapItem> match(IMapNode targetNode) {
        // 用于统计所有种类的数量
        int mapNodeTypeLength = MapNodeTypeEnum.values().length;
        int[] nodeCountArray = new int[mapNodeTypeLength];
        List<IMapNode> links = targetNode.getLinks();
        for (IMapNode mapNode : links) {
            if (mapNode.getType() != null) {
                nodeCountArray[mapNode.getType().getTypeCode()]++;
            }
        }
        // 过滤数量为0的类型，取得第一个不为0的下标
        int i = 0;
        while (i < mapNodeTypeLength && nodeCountArray[i] <= 1) {
            i++;
        }

        // 若没有则返回空集合
        if (i >= mapNodeTypeLength) {
            return Collections.emptyList();
        }

        List<IMapItem> mapItems = new ArrayList<>();
        // 2，3，4个都会存在本身
        IMapItem mapItem = itemMatchMapList.get(i).get(nodeCountArray[i]);
        if (mapItem != null) {
            mapItems.add(mapItem);
        }

        // 2个的情况需要向后考虑是否含2个的组合
        if (nodeCountArray[i] == 2) {
            int targetIndex = i;
            i++;
            while (i < mapNodeTypeLength && nodeCountArray[i] <= 1) {
                i++;
            }

            // 由于本身是2个，且查到的数大于1，则这里必然为2
            // 取该节点
            if (i < mapNodeTypeLength) {
                Integer calcKey = calcMapNodeTypeCombine(targetIndex, i);
                mapItem = itemMatchMapList.get(targetIndex).get(calcKey);
                if (mapItem != null) {
                    mapItems.add(mapItem);
                }
            }
        }

        return mapItems;
    }

    @Override
    public List<IMapItem> match(IMapNode targetNode, IMapNode newNode) {
        // 用于统计所有种类的数量
        int mapNodeTypeLength = MapNodeTypeEnum.values().length;
        int[] nodeCountArray = new int[mapNodeTypeLength];
        List<IMapNode> links = targetNode.getLinks();
        for (IMapNode mapNode : links) {
            if (mapNode.getType() != null) {
                nodeCountArray[mapNode.getType().getTypeCode()]++;
            }
        }

        int i = newNode.getType().getTypeCode();
        List<IMapItem> mapItems = new ArrayList<>();
        // 2，3，4个都会存在本身
        IMapItem mapItem = itemMatchMapList.get(i).get(nodeCountArray[i]);
        if (mapItem != null) {
            mapItems.add(mapItem);
        }

        // 2个的情况需要向后考虑是否含2个的组合
        if (nodeCountArray[i] == 2) {
            int targetIndex = i;
            i = 0;
            while (i < mapNodeTypeLength  //
                    && ((nodeCountArray[i] == 2 && targetIndex == i) || nodeCountArray[i] != 2)) {
                i++;
            }

            // 由于本身是2个，且查到的数大于1，则这里必然为2
            // 取该节点
            if (i < mapNodeTypeLength) {
                Integer calcKey = calcMapNodeTypeCombine(targetIndex, i);
                Integer minIndex = Math.min(targetIndex, i);
                mapItem = itemMatchMapList.get(minIndex).get(calcKey);
                if (mapItem != null) {
                    mapItems.add(mapItem);
                }
            }
        }

        return mapItems;
    }

    /**
     * 初始化
     */
    private static void initItemMatchMapList() {
        itemMatchMapList = new ArrayList<>(MapNodeTypeEnum.values().length);

        // 街
        Map<Integer, IMapItem> streetMap = new HashMap<>();
        itemMatchMapList.add(MapNodeTypeEnum.Street.getTypeCode(), streetMap);
        // 山
        Map<Integer, IMapItem> mountainMap = new HashMap<>();
        itemMatchMapList.add(MapNodeTypeEnum.Mountain.getTypeCode(), mountainMap);
        // 森林
        Map<Integer, IMapItem> forestMap = new HashMap<>();
        itemMatchMapList.add(MapNodeTypeEnum.Forest.getTypeCode(), forestMap);
        // 沙漠
        Map<Integer, IMapItem> desertMap = new HashMap<>();
        itemMatchMapList.add(MapNodeTypeEnum.Desert.getTypeCode(), desertMap);
        // 平原
        Map<Integer, IMapItem> steppesMap = new HashMap<>();
        itemMatchMapList.add(MapNodeTypeEnum.Steppes.getTypeCode(), steppesMap);
        // 洞穴
        Map<Integer, IMapItem> caveMap = new HashMap<>();
        itemMatchMapList.add(MapNodeTypeEnum.Cave.getTypeCode(), caveMap);
        // 河流/湿地
        Map<Integer, IMapItem> riverMap = new HashMap<>();
        itemMatchMapList.add(MapNodeTypeEnum.River.getTypeCode(), riverMap);
        // 死街
        Map<Integer, IMapItem> deadStreetMap = new HashMap<>();
        itemMatchMapList.add(MapNodeTypeEnum.DeadStreet.getTypeCode(), deadStreetMap);

        // 两个相同地点
        streetMap.put(2, MapItem.from(28, "红色鞋"));
        mountainMap.put(2, MapItem.from(5, "金刚绿石"));
        forestMap.put(2, MapItem.from(25, "原质晶石"));
        desertMap.put(2, MapItem.from(30, "红宝石耳环"));
        steppesMap.put(2, MapItem.from(5, "特拉库奥之花"));
        caveMap.put(2, MapItem.from(5, "时律石"));
        riverMap.put(2, MapItem.from(5, "金花虫护身符"));
        deadStreetMap.put(2, MapItem.from(30, "星辰矿石"));

        // 三个相同地点
        streetMap.put(3, MapItem.from(30, "橡胶外衣"));
        mountainMap.put(3, MapItem.from(25, "白金头盔"));
        forestMap.put(3, MapItem.from(30, "发夹"));
        desertMap.put(3, MapItem.from(400, "加尔米亚之靴"));
        steppesMap.put(3, MapItem.from(5000, "加斯特拉斐迪弓"));
        deadStreetMap.put(3, MapItem.from(25, "破邪剑"));

        // 四个相同地点
        streetMap.put(4, MapItem.from(400, "丝带"));
        mountainMap.put(4, MapItem.from(20, "盖义乌斯卡里古"));
        forestMap.put(4, MapItem.from(4000, "银丝外套"));
        desertMap.put(4, MapItem.from(5000, "敏武宝石"));

        // 两个两个相同地点
        // 计算按一定顺序，所以只需要前面设置即可

        // 街
        Integer calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Street, MapNodeTypeEnum.Mountain);
        streetMap.put(calcKey, MapItem.from(5000, "艾斯托莱阿之剑"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Street, MapNodeTypeEnum.Forest);
        streetMap.put(calcKey, MapItem.from(5000, "原质宽剑"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Street, MapNodeTypeEnum.Desert);
        streetMap.put(calcKey, MapItem.from(380, "公主守护杖"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Street, MapNodeTypeEnum.Steppes);
        streetMap.put(calcKey, MapItem.from(480, "塔鲁瓦鲁"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Street, MapNodeTypeEnum.Cave);
        streetMap.put(calcKey, MapItem.from(5000, "源氏之盾"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Street, MapNodeTypeEnum.River);
        steppesMap.put(calcKey, MapItem.from(3900, "迪塔普特短剑"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Street, MapNodeTypeEnum.DeadStreet);
        steppesMap.put(calcKey, MapItem.from(28, "维加之凯"));
        
        // 山
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Mountain, MapNodeTypeEnum.Forest);
        mountainMap.put(calcKey, MapItem.from(400, "贤者之袍"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Mountain, MapNodeTypeEnum.Desert);
        mountainMap.put(calcKey, MapItem.from(320, "超级利刃"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Mountain, MapNodeTypeEnum.Steppes);
        mountainMap.put(calcKey, MapItem.from(340, "宙斯锡杖"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Mountain, MapNodeTypeEnum.Cave);
        mountainMap.put(calcKey, MapItem.from(15, "运动护腕"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Mountain, MapNodeTypeEnum.River);
        mountainMap.put(calcKey, MapItem.from(370, "死亡之爪"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Mountain, MapNodeTypeEnum.DeadStreet);
        mountainMap.put(calcKey, MapItem.from(20, "地之刻印"));

        // 森
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Forest, MapNodeTypeEnum.Desert);
        forestMap.put(calcKey, MapItem.from(4800, "五指短剑"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Forest, MapNodeTypeEnum.Steppes);
        forestMap.put(calcKey, MapItem.from(35, "仙人掌杖"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Forest, MapNodeTypeEnum.Cave);
        forestMap.put(calcKey, MapItem.from(5000, "源氏之凯"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Forest, MapNodeTypeEnum.River);
        forestMap.put(calcKey, MapItem.from(5000, "杀意剑"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Forest, MapNodeTypeEnum.DeadStreet);
        forestMap.put(calcKey, MapItem.from(28, "维纳斯之剑"));

        // 沙
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Desert, MapNodeTypeEnum.Steppes);
        desertMap.put(calcKey, MapItem.from(440, "岩兽长枪"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Desert, MapNodeTypeEnum.Cave);
        desertMap.put(calcKey, MapItem.from(20, "风之刻印"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Desert, MapNodeTypeEnum.River);
        desertMap.put(calcKey, MapItem.from(4000, "尖剑棱镜"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Desert, MapNodeTypeEnum.DeadStreet);
        desertMap.put(calcKey, MapItem.from(430, "群星之弓"));

        // 平
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Steppes, MapNodeTypeEnum.Cave);
        steppesMap.put(calcKey, MapItem.from(5000, "源氏之盔"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Steppes, MapNodeTypeEnum.River);
        steppesMap.put(calcKey, MapItem.from(20, "炎之刻印"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Steppes, MapNodeTypeEnum.DeadStreet);
        steppesMap.put(calcKey, MapItem.from(20, "水之刻印"));

        // 洞
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Cave, MapNodeTypeEnum.River);
        caveMap.put(calcKey, MapItem.from(5000, "敏武宝石"));
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.Cave, MapNodeTypeEnum.DeadStreet);
        caveMap.put(calcKey, MapItem.from(2000, "天堂之杖"));

        // 湿
        calcKey = calcMapNodeTypeCombine(MapNodeTypeEnum.River, MapNodeTypeEnum.DeadStreet);
        riverMap.put(calcKey, MapItem.from(5000, "源氏护腕"));
    }

    /**
     * 计算两个地图节点类型组合值（使用乘法）
     * 
     * @param type1 节点类型1
     * @param type2 节点类型2
     */
    private static Integer calcMapNodeTypeCombine(MapNodeTypeEnum type1, MapNodeTypeEnum type2) {
        return calcMapNodeTypeCombine(type1.getTypeCode(), type2.getTypeCode());
    }

    /**
     * 计算两个地图节点类型组合值（使用乘法）
     * 
     * @param type1 节点类型1
     * @param type2 节点类型2
     */
    private static Integer calcMapNodeTypeCombine(int type1, int type2) {
        return (type1 + 10) * (type2 + 10);
    }
}
