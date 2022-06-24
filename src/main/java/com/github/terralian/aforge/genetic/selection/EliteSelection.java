// AForge Genetic Library
// AForge.NET framework
// http://www.aforgenet.com/framework/
//
// Copyright � Andrew Kirillov, 2006-2009
// andrew.kirillov@aforgenet.com
//
package com.github.terralian.aforge.genetic.selection;

import java.util.List;

import com.github.terralian.aforge.genetic.chromosomes.IChromosome;
import com.github.terralian.csharp.LangUtil;

/**
 * Elite selection method.
 * <p>
 * Elite selection method selects specified amount of
 * best chromosomes to the next generation.
 */
public class EliteSelection implements ISelectionMethod {

    /**
     * Initializes a new instance of the {@link EliteSelection} class.
     */
    public EliteSelection() {}

    @Override
    public void applySelection(List<IChromosome> chromosomes, int size) {
        // sort chromosomes
        chromosomes.sort(LangUtil.defaultSort());
        // remove bad chromosomes
        LangUtil.listRemoveRange(chromosomes, size, chromosomes.size() - size);
    }

}
