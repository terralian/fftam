package com.github.terralian.fftam.map;

/**
 * 地图节点类型
 * 
 * @author terra.lian
 */
public enum MapNodeTypeEnum {

    /**
     * 街道
     */
    Street(0, "街道"),

    /**
     * 山脉
     */
    Mountain(1, "山脉"),

    /**
     * 森林
     */
    Forest(2, "森林"),

    /**
     * 沙漠
     */
    Desert(3, "沙漠"),

    /**
     * 平原
     */
    Steppes(4, "平原"),

    /**
     * 洞穴
     */
    Cave(5, "洞穴"),

    /**
     * 河流
     */
    River(6, "河流"),
    
    /**
     * 死街
     */
    DeadStreet(7, "死街"),
    ;

    /**
     * 节点编码，与数组索引相关
     */
    private Integer typeCode;
    /**
     * 节点名称
     */
    private String typeName;

    /**
     * 节点数组索引
     */
    private static final MapNodeTypeEnum[] mapNodeTypeArray;
    static {
        MapNodeTypeEnum[] values = MapNodeTypeEnum.values();
        mapNodeTypeArray = new MapNodeTypeEnum[values.length];
        for (MapNodeTypeEnum mapNodeType : values) {
            mapNodeTypeArray[mapNodeType.getTypeCode()] = mapNodeType;
        }
    }

    private MapNodeTypeEnum(Integer typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    /**
     * 获取节点编码
     */
    public Integer getTypeCode() {
        return typeCode;
    }

    /**
     * 获取节点名称
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * 根据编码获取节点的枚举
     * 
     * @param code 编码
     */
    public static MapNodeTypeEnum getEnumByCode(Integer code) {
        return code != null ? mapNodeTypeArray[code] : null;
    }


    @Override
    public String toString() {
        return typeName;
    }
}
