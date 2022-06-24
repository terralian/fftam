package com.github.terralian.fftam.search.genetic2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.github.terralian.aforge.genetic.Population;
import com.github.terralian.aforge.genetic.chromosomes.IChromosome;
import com.github.terralian.aforge.genetic.fitness.IFitnessFunction;
import com.github.terralian.aforge.genetic.selection.ISelectionMethod;
import com.github.terralian.common.lang.CollectionUtil;
import com.github.terralian.csharp.LangUtil;
import com.github.terralian.fftam.search.genetic2.IGenerationChromosome.Generation;

/**
 * 游戏地图种群
 * 
 * @author terra.lian
 */
public class GameMapPopulation {

    private IFitnessFunction fitnessFunction;
    private ISelectionMethod selectionMethod;
    private List<IGenerationChromosome> population = new ArrayList<>();
    private int size;
    private double randomSelectionPortion = 0.0;
    private boolean autoShuffling = false;

    // 种群参数
    private double crossoverRate = 0.75;
    private double mutationRate = 0.10;

    // 随机数生成器
    private static ThreadLocalRandom rand = ThreadLocalRandom.current();

    //
    private double fitnessMax = 0;
    private double fitnessSum = 0;
    private double fitnessAvg = 0;
    private IChromosome bestChromosome = null;

    /**
     * 初始化一个种群{@link Population}实例.
     * <p>
     * 创建一个指定大小的种群. 祖先染色体将作为种群的第一个成员，并使用统一的参数创建剩下的其他成员.
     * 
     * @param size 种群大小初始值.
     * @param ancestor 用于创建种群的先祖染色体.
     * @param fitnessFunction 用于计算染色体适应值的适应度函数.
     * @param selectionMethod 用于选择新一代染色体的选择算法.
     * @throws IllegalArgumentException 指定的种群规模太小.若size小于2，则抛出该异常.
     */
    public GameMapPopulation(int size, IGenerationChromosome ancestor, IFitnessFunction fitnessFunction, ISelectionMethod selectionMethod) {
        if (size < 2)
            throw new IllegalArgumentException("Too small population's size was specified.");

        this.fitnessFunction = fitnessFunction;
        this.selectionMethod = selectionMethod;
        this.size = size;

        // 将祖先添加到种群
        ancestor.evaluate(fitnessFunction);
        population.add((IGenerationChromosome) ancestor.clone());
        // 添加更多的染色体到种群
        for (int i = 1; i < size; i++) {
            // 创建新的染色体
            IChromosome c = ancestor.createNew();
            // 计算其适应度
            c.evaluate(fitnessFunction);
            // 添加到种群
            population.add((IGenerationChromosome) c);
        }
    }

    /**
     * 重新生成种群.
     * <p>
     * 该方法使用随机染色体重新生成种群.
     */
    public void regenerate() {
        IChromosome ancestor = population.get(0);

        // 清空种群
        population.clear();
        // 添加染色体到种群
        for (int i = 0; i < size; i++) {
            // 创建新染色体
            IChromosome c = ancestor.createNew();
            // 计算其适应度
            c.evaluate(fitnessFunction);
            // 添加到种群
            population.add((IGenerationChromosome) c);
        }
    }

    /**
     * 执行种群交叉（crossover）.
     * <p>
     * 该方法遍历种群并按顺序对每两条染色体执行交叉算子. 配对的染色体总数由交叉概率（{@link #crossoverRate}）决定
     */
    public void crossover() {
        // 交叉
        List<IGenerationChromosome> middles = CollectionUtil.filterToList(population,
                k -> k.getGeneration() == Generation.MIDDLE);

        for (int i = 1; i < middles.size(); i += 2) {
            // 生成下一个随机数，并判断是否需要进行交叉
            if (rand.nextDouble() <= crossoverRate) {
                // 克隆自身及祖先
                IGenerationChromosome c1 = (IGenerationChromosome) middles.get(i - 1).clone();
                IGenerationChromosome c2 = (IGenerationChromosome) middles.get(i).clone();

                // 执行交叉
                c1.crossover(c2);

                // 对后代计算适应度
                c1.evaluate(fitnessFunction);
                c2.evaluate(fitnessFunction);

                // 添加到种群
                population.add(c1);
                population.add(c2);
            }
        }
    }

    /**
     * 执行种群突变.
     * <p>
     * 该方法遍历种群，对每一个染色体执行突变操作. 突变的染色体总数由突变概率（{@link #mutationRate}）决定.
     */
    public void mutate() {
        // 突变
        for (int i = 0; i < size; i++) {
            // 生成下一个随机数并判断是否进行突变
            if (rand.nextDouble() <= mutationRate) {
                // 克隆染色体
                IChromosome c = population.get(i);
                // 突变
                c.mutate();
                // 对突变型计算适应度
                c.evaluate(fitnessFunction);
            }
        }
    }

    /**
     * 执行选择.
     * <p>
     * 该方法对当前种群进行选择操作. 使用指定的选择算法对当前种群进行选择作为新种群，并在需要时添加定义数量的随机成员. 参考 {@link #randomSelectionPortion}
     */
    public void selection() {
        // 执行选择
        IGenerationChromosome ancestor = population.get(0);

        selectionMethod.applySelection((List) population, 0);

        // 交叉
        if (population.size() < size) {
            int addSize = size - population.size();
            List<IGenerationChromosome> middles = CollectionUtil.filterToList(population,
                    k -> k.getGeneration() == Generation.MIDDLE || k.getGeneration() == Generation.OLDER);
            if (middles.size() != 0) {
                int i = 0;
                while (i < addSize) {
                    int randNum = rand.nextInt(middles.size());
                    IGenerationChromosome target = middles.get(randNum);
                    // 执行交叉
                    target.crossover(null);

                    // 对后代计算适应度
                    target.evaluate(fitnessFunction);

                    // 添加到种群
                    population.add(target);
                    i++;
                }
            }

            else {
                for (int i = 0; i < addSize; i++) {
                    // 创建新染色体
                    IGenerationChromosome c = (IGenerationChromosome) ancestor.createNew();
                    // 计算适应度
                    c.evaluate(fitnessFunction);
                    // 添加到种群
                    population.add(c);
                }

            }
        }

        findBestChromosome();
    }

    /**
     * 执行一个种群时期（epoch）.
     * <p>
     * 该方法执行一个种群时期（epoch），进行交叉，突变及选择.通过调用 {@link #crossover()}, {@link #mutate()} 和 {@link #selection()}.
     */
    public void runEpoch() {
        mutate();
        selection();
        
        for (IGenerationChromosome g : population) {
            g.ageAdd();
        }

        if (autoShuffling)
            shuffle();
    }

    /**
     * 对当前种群进行随机洗牌.
     * <p>
     * 该方法常用于 选择操作的结果导致的染色体顺序不是随机的情况（例如在选择精英染色体后，种群可能按升序/降序排列）
     */
    public void shuffle() {
        // 当前种群大小
        int newSize = population.size();
        // 创建种群的临时副本
        List<IChromosome> tempPopulation = new ArrayList<>(population.subList(0, newSize));
        // 清空当前种群，并重新随机填充
        population.clear();

        while (newSize > 0) {
            int i = rand.nextInt(newSize);

            population.add((IGenerationChromosome) tempPopulation.get(i));
            tempPopulation.remove(i);

            newSize--;
        }
    }

    /**
     * 将染色体添加到种群.
     * <p>
     * 该方法将指定的染色体添加到当前种群. 常用于添加某些已初始化的而非随机的染色体.
     * <p>
     * 手动添加染色体应当非常小心，因为这可能破坏种群. 手动添加的染色体必须具有与初始化构造体时的祖先染色体一致的类型与初始化参数
     * 
     * @param chromosome 要添加到种群的染色体.
     */
    public void addChromosome(IChromosome chromosome) {
        chromosome.evaluate(fitnessFunction);
        population.add((IGenerationChromosome) chromosome);
    }

    /**
     * 在两个种群间执行迁移（migration）.
     * <p>
     * 该方法在两个种群之间执行迁移操作- 当前种群 和 另一个种群. 在迁移过程中，通过migrantsSelector从每个种群间选择numberOfMigrants个染色体，并将其放入另一个种群中，替换其中最差的个体.
     * 
     * @param anotherPopulation 需要迁移的种群.
     * @param numberOfMigrants 每个种群中需要迁移的数量.
     * @param migrantsSelector 用于选择要迁移的染色体的选择算法.
     */
    public void migrate(GameMapPopulation anotherPopulation, int numberOfMigrants, ISelectionMethod migrantsSelector) {
        int currentSize = this.size;
        int anotherSize = anotherPopulation.size;

        // 创建当前染色体的克隆
        List<IGenerationChromosome> currentCopy = new ArrayList<>();

        for (int i = 0; i < currentSize; i++) {
            currentCopy.add((IGenerationChromosome) population.get(i).clone());
        }

        // 创建另一个染色体的克隆
        List<IGenerationChromosome> anotherCopy = new ArrayList<>();

        for (int i = 0; i < anotherSize; i++) {
            anotherCopy.add((IGenerationChromosome) anotherPopulation.population.get(i).clone());
        }

        // 从各个种群执行选择算法 - 选择需要迁移的染色体
        migrantsSelector.applySelection((List) currentCopy, numberOfMigrants);
        migrantsSelector.applySelection((List) anotherCopy, numberOfMigrants);

        // 对原始的种群进行排序，这样最好的染色体就在集合的开始部分
        population.sort((a, b) -> a.compareTo(b));
        anotherPopulation.population.sort((a, b) -> a.compareTo(b));

        // 从两个种群中剔除最差的染色体，为新成员腾出空间（从后向前删除N个元素）
        LangUtil.listRemoveRange(population, currentSize - numberOfMigrants, numberOfMigrants);
        LangUtil.listRemoveRange(anotherPopulation.population, anotherSize - numberOfMigrants, numberOfMigrants);

        // 添加移民染色体
        population.addAll(anotherCopy);
        anotherPopulation.population.addAll(currentCopy);

        // 在每个种群中找到最好的染色体
        findBestChromosome();
        anotherPopulation.findBestChromosome();
    }

    /**
     * 调整种群的大小为目标值.
     * <p>
     * 该方法用于调整种群大小. 当种群需要增大，这里仅会增加缺少部分数量的随机染色体，当种群需要减小，这里会使用{@link #selectionMethod}选择算法来缩小种群.
     * 
     * @param newPopulationSize 新的种群数量.
     * @throws IllegalArgumentException 指定的种群数量太小，若newPopulationSize小于2则会抛出该异常.
     */
    public void resize(int newPopulationSize) {
        resize(newPopulationSize, selectionMethod);
    }

    /**
     * 调整种群的大小为目标值.
     * <p>
     * 该方法用于调整种群大小. 当种群需要增大，这里仅会增加缺少部分数量的随机染色体，当种群需要减小，这里会使用membersSelector选择算法来缩小种群.
     * 
     * @param newPopulationSize 新的种群数量.
     * @param membersSelector 在缩小种群时所使用的选择算法
     * @throws IllegalArgumentException 指定的种群数量太小，若newPopulationSize小于2则会抛出该异常.
     */
    public void resize(int newPopulationSize, ISelectionMethod membersSelector) {
        if (newPopulationSize < 2)
            throw new IllegalArgumentException("Too small new population's size was specified.");

        if (newPopulationSize > size) {
            // 种群需要增长，所以这里需要增加新随机成员

            // 注意：在这里使用种群，应使用差值而不是使用新大小，因为种群有可能已经过交叉/变异操作增大过了.
            // 所以这里需要保持这些成员，仅添加缺失的数量，而非添加所有.
            int toAdd = newPopulationSize - population.size();

            for (int i = 0; i < toAdd; i++) {
                // 创建新染色体
                IGenerationChromosome c = (IGenerationChromosome) population.get(0).createNew();
                // 计算其适应度
                c.evaluate(fitnessFunction);
                // 添加到种群
                population.add(c);
            }
        } else {
            // 进行选择
            membersSelector.applySelection((List) population, newPopulationSize);
        }

        size = newPopulationSize;
    }

    // 找到种群中迄今为止最好的染色体
    private void findBestChromosome() {
        bestChromosome = population.get(0);
        fitnessMax = bestChromosome.getFitness();
        fitnessSum = fitnessMax;

        for (int i = 1; i < size; i++) {
            double fitness = population.get(i).getFitness();

            // 累加汇总值
            fitnessSum += fitness;

            // 检查最大值
            if (fitness > fitnessMax) {
                fitnessMax = fitness;
                bestChromosome = population.get(i);
            }
        }
        fitnessAvg = fitnessSum / size;
    }

    /**
     * 获得交叉概率, [0.1, 1].
     * <p>
     * 该值决定参与交叉的染色体数量
     * <p>
     * 默认值为 <b>0.75</b>.
     */
    public double getCrossoverRate() {
        return crossoverRate;
    }

    /**
     * 设置交叉概率, [0.1, 1].
     * <p>
     * 该值决定参与交叉的染色体数量
     * 
     * @param crossoverRate 值
     */
    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = Math.max(0.1, Math.min(1.0, crossoverRate));
    }

    /**
     * 获得突变概率, [0.1, 1].
     * <p>
     * 该值决定参与突变的染色体数量
     * <p>
     * 默认值为 <b>0.1</b>.
     */
    public double getMutationRate() {
        return mutationRate;
    }

    /**
     * 设置突变概率, [0.1, 1].
     * <p>
     * 该值决定参与突变的染色体数量
     * 
     * @param mutationRate 值
     */
    public void setMutationRate(double mutationRate) {
        this.mutationRate = Math.max(0.1, Math.min(1.0, mutationRate));
    }

    /**
     * 获取随机选择部分, [0, 0.9].
     * <p>
     * 该值确定为新种群随机生成的染色体数量. 该属性控制使用{@link #selectionMethod}选择到新种群的染色体数量
     * <p>
     * 默认值为 <b>0</b>.
     */
    public double getRandomSelectionPortion() {
        return randomSelectionPortion;
    }

    /**
     * 设置随机选择部分, [0, 0.9].
     * <p>
     * 该值确定为新种群随机生成的染色体数量. 该属性控制使用{@link #selectionMethod}选择到新种群的染色体数量
     * 
     * @param randomSelectionPortion 值
     */
    public void setRandomSelectionPortion(double randomSelectionPortion) {
        this.randomSelectionPortion = Math.max(0, Math.min(0.9, randomSelectionPortion));
    }

    /**
     * 确定是否自动洗牌.
     * <p>
     * 该属性确定是否需要在每个{@link #runEpoch()}上自动调用{@link #shuffle()}方法执行洗牌操作
     * <p>
     * 默认值为 <b>false</b>.
     */
    public boolean isAutoShuffling() {
        return autoShuffling;
    }

    /**
     * 设置是否自动洗牌.
     * <p>
     * 该属性确定是否需要在每个{@link #runEpoch()}上自动调用{@link #shuffle()}方法执行洗牌操作
     * 
     * @param autoShuffling 值
     */
    public void setAutoShuffling(boolean autoShuffling) {
        this.autoShuffling = autoShuffling;
    }

    /**
     * 种群的选择方法.
     * <p>
     * 该属性设置用于为新种群选择种群成员的选择方法 - 该方法过滤种群在种群使用如交叉突变等操作之后进行.
     */
    public ISelectionMethod getSelectionMethod() {
        return selectionMethod;
    }

    /**
     * 种群的选择方法.
     * <p>
     * 该属性设置用于为新种群选择种群成员的选择方法 - 该方法过滤种群在种群使用如交叉突变等操作之后进行.
     * 
     * @param selectionMethod value 值
     */
    public void setSelectionMethod(ISelectionMethod selectionMethod) {
        this.selectionMethod = selectionMethod;
    }

    /**
     * 应用于种群的适应度函数.
     * <P>
     * 该属性设置适应度函数，用于评估种群染色特的有效性. 设置新的适应度函数会导致重新计算所有种群成员的适应值，并找到新的最佳成员.
     */
    public IFitnessFunction getFitnessFunction() {
        return fitnessFunction;
    }

    /**
     * 应用于种群的适应度函数.
     * <P>
     * 该属性设置适应度函数，用于评估种群染色特的有效性. 设置新的适应度函数会导致重新计算所有种群成员的适应值，并找到新的最佳成员.
     * 
     * @param fitnessFunction IFitnessFunction
     */
    public void setFitnessFunction(IFitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
        for (IChromosome member : population) {
            member.evaluate(fitnessFunction);
        }
        findBestChromosome();
    }

    /**
     * 种群的最大适应度.
     * <p>
     * 该属性为种群中当前存在的染色体最大适应度.
     * <p>
     * 该属性只有在{@link #selection()}或{@link #migrate}后进行重算.
     */
    public double getFitnessMax() {
        return fitnessMax;
    }

    /**
     * 种群适应度的汇总值.
     * <p>
     * 该属性为种群中当前存在的染色体适应度的汇总值.
     * <p>
     * 该属性只有在{@link #selection()}或{@link #migrate}后进行重算.
     */
    public double getFitnessSum() {
        return fitnessSum;
    }

    /**
     * 种群适应度的平均值.
     * <p>
     * 该属性为种群中当前存在的染色体适应度的平均值.
     * <p>
     * 该属性只有在{@link #selection()}或{@link #migrate}后进行重算.
     */
    public double getFitnessAvg() {
        return fitnessAvg;
    }

    /**
     * 种群中的最佳染色体.
     * <p>
     * 该属性持为最佳种群中存在的最佳染色体，或者当所有染色体的适应度为0时为null
     */
    public IChromosome getBestChromosome() {
        return bestChromosome;
    }

    /**
     * 种群的大小.
     * <p>
     * 该属性为种群的初始（最小）规模. 种群的规模始终会在{@link #selection}或{@link #runEpoch}方法调用，执行选择操作之后返回到该值大小
     */
    public int getSize() {
        return size;
    }

    /**
     * 获取指定下标的染色体.
     * <p>
     * 允许访问染色体中的个体
     * 
     * @param index 染色体的下标.
     */
    public IChromosome getPopulation(int index) {
        return this.population.get(index);
    }
}
