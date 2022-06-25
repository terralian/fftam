package com.github.terralian.fftam.search.recurssion;

import java.util.List;
import java.util.PriorityQueue;

import com.github.terralian.fftam.item.IMapItem;
import com.github.terralian.fftam.map.GameMap;
import com.github.terralian.fftam.map.IMapNode;
import com.github.terralian.fftam.map.MapNodeTypeEnum;
import com.github.terralian.fftam.search.IMapSearcher;

/**
 * 递归查询
 * <p>
 * 其排序数过多，无法遍历
 * 
 * @author terra.lian
 */
public class RecurssionSearcher implements IMapSearcher {

    /**
     * 作为参数的节点
     */
    private int[] mapNodeTypes;

    /**
     * 最佳队列，存100
     */
    private PriorityQueue<MapItemResult> minQueue;

    /**
     * 递归数统计
     */
    private int loopCount = 0;

    /**
     * 每循环多少次执行一次输出
     */
    private int outputBatchLoop = 10000;

    /**
     * 构建递归查询实例
     * <p>
     * 该方法会进行初始化
     */
    public RecurssionSearcher() {
        minQueue = new PriorityQueue<>(101, (a, b) -> {
            return a.getPoint().compareTo(b.getPoint());
        });

        mapNodeTypes = new int[MapNodeTypeEnum.values().length];
        mapNodeTypes[MapNodeTypeEnum.Street.getTypeCode()] = 4;
        mapNodeTypes[MapNodeTypeEnum.Mountain.getTypeCode()] = 4;
        mapNodeTypes[MapNodeTypeEnum.Forest.getTypeCode()] = 4;
        mapNodeTypes[MapNodeTypeEnum.Desert.getTypeCode()] = 4;
        mapNodeTypes[MapNodeTypeEnum.Steppes.getTypeCode()] = 3;
        mapNodeTypes[MapNodeTypeEnum.Cave.getTypeCode()] = 2;
        mapNodeTypes[MapNodeTypeEnum.River.getTypeCode()] = 2;
        mapNodeTypes[MapNodeTypeEnum.DeadStreet.getTypeCode()] = 3;
    }

    @Override
    public void search() {
        GameMap gameMap = new GameMap();
        loopSetMapNode(gameMap, 0);
    }

    /**
     * 遍历循环设置地图节点
     * 
     * @param gameMap 游戏地图
     * @param mapNodeIndex 当前地图节点索引
     */
    public void loopSetMapNode(GameMap gameMap, int mapNodeIndex) {
        // 部分固定项不用管
        if (mapNodeIndex == 8 || mapNodeIndex == 12 || mapNodeIndex == 14) {
            mapNodeIndex++;
        }

        // 结束当前循环
        else if (mapNodeIndex >= 30) {
            loopCount++;
            gameMap.match();
            List<IMapItem> items = gameMap.getMatchCacheList();
            if (items != null && items.size() > 0) {
                MapItemResult result = new MapItemResult(items);
                minQueue.offer(result);
                if (minQueue.size() > 100) {
                    minQueue.poll();
                }
            }

            if (loopCount > outputBatchLoop && (loopCount % outputBatchLoop == 0)) {
                IMapNode mapNode = gameMap.getMapNode(0);
                System.out.println(loopCount + " nfn:" + mapNode.getType() + " nfc:" + mapNode.getType().getTypeCode() + " bp:"
                        + minQueue.peek().point);
                System.out.println("re:" + gameMap.toString());
            }

            return;
        }

        for (int i = 0; i < mapNodeTypes.length; i++) {
            if (mapNodeTypes[i] > 0) {
                mapNodeTypes[i]--;
                gameMap.setNode(mapNodeIndex, MapNodeTypeEnum.getEnumByCode(i));
                loopSetMapNode(gameMap, mapNodeIndex + 1);
                gameMap.clearNode(mapNodeIndex);
                mapNodeTypes[i]++;
            }
        }
    }

    public static class MapItemResult {
        private List<IMapItem> mapItems;
        private Integer point;

        public MapItemResult(List<IMapItem> mapItems) {
            this.mapItems = mapItems;
            this.point = IMapItem.sumItemPoint(mapItems);
        }

        public List<IMapItem> getMapItems() {
            return mapItems;
        }

        public Integer getPoint() {
            return point;
        }
    }
}
