# fftam

该程序用于计算游戏《GBA 最终幻想战略版》的地图的最佳摆放。

最终方案基于遗传算法实现，项目内部保留了旧版本的代码作为留念。

对该项目的讲解博客参照：[博客](https://www.cnblogs.com/syui-terra/p/16408803.html)

### 环境

    jdk 1.8

### 包结构

`com.github.terralian`

- `aforge.genetic` AForge.NET 的 Java 版本移植的代码 copy
- `common.lang` 个人使用的工具类，copy
- `csharp` 为了兼容 c#和 java 代码的差异，部分 API 实现，也是 copy
- `fftam` 实际项目
  - `item` 宝物的定义，宝物匹配
  - `map` 地图实例，地图的节点
  - `search` 摆放搜索算法实现
    - `genetic1` 遗传算法版本 1
    - `genetic2` 遗传算法版本 2
    - `genetic3` 遗传算法版本 3，也是该项目的唯一成功版本
    - `recurssion` 暴力递归版本
    - `sample` 一些摆放样本，用做目标，也用于保存。
  - `App.java` 应用入口

### 部分重要类

- `MapItemMatcher` 该方法内的方法`initItemMatchMapList`
  每个宝物的分值将在这里设计，若你对某些宝物特别关注，那么将它的分值调高，若一定要出现，那么为分值加 0

### 外部库：

- [aforget](https://github.com/TerraLian/aforget)，该库是[AForge.NET](https://github.com/andrewkirillov/AForge.NET) 的 Java 版本的移植，本项目为了项目独立（因为没有发 maven 仓库），将使用的代码 copy 进来了。

### 其他

代码相当多地方都不是最优，但是能用，那就这样了（
