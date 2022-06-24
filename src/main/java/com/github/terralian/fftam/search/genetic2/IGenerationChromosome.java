package com.github.terralian.fftam.search.genetic2;

import com.github.terralian.aforge.genetic.chromosomes.IChromosome;

/**
 * 含有代概念的染色体，并且有寿命概念
 * <p>
 * 对于代越晚的染色体，年龄越大的染色体，其死亡概率越大，当寿命结束时，强制销毁
 */
interface IGenerationChromosome extends IChromosome {

    /**
     * 获取其年龄
     */
    int getAge();

    /**
     * 年龄增长，每次增长1
     * 
     * @return 返回增长后的年龄
     */
    int ageAdd();

    /**
     * 设置最大年龄，当年龄超过这个限度，将被最优先剔除
     * 
     * @param maxAge 最大年龄
     */
    void setMaxAge(int maxAge);

    /**
     * 获取染色体的最大年龄
     */
    int getMaxAge();

    /**
     * 获取当前年龄所属的代
     * <p>
     * 默认实现是按照比例
     * <li>年轻代占年龄开头部分的40%
     * <li>中年代占年龄中间部分的30%
     * <li>老年代占年龄末尾部分的30%
     * <p>
     * 基于年代可以在外部设置每个代别的死亡几率，来计算该染色体个体是否存活，或者意外死亡。
     */
    default Generation getGeneration() {
        int currentAge = getAge();
        int maxAge = getMaxAge();
        if (currentAge > maxAge * 0.7)
            return Generation.OLDER;
        else if (currentAge > maxAge * 0.4)
            return Generation.MIDDLE;
        return Generation.YOUNG;
    }

    /**
     * 代枚举
     * 
     * @author terra.lian
     * @since 2022年6月17日
     */
    enum Generation {
        /**
         * 年轻代
         */
        YOUNG,
        /**
         * 中年代
         */
        MIDDLE,
        /**
         * 老年代
         */
        OLDER
    }
}
