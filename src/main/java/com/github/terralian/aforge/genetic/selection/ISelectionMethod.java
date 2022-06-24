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

/**
 * Genetic selection method interface.
 * <p>
 * The interface should be implemented by all classes, which
 * implement genetic selection algorithm. These algorithms select members of
 * current generation, which should be kept in the new generation. Basically,
 * these algorithms filter provided population keeping only specified amount of
 * members.
 */
public interface ISelectionMethod {

    /**
     * Apply selection to the specified population.
     * 
     * <p>
     * Filters specified population according to the implemented selection algorithm.
     * 
     * @param chromosomes Population, which should be filtered.
     * @param size The amount of chromosomes to keep.
     */
    void applySelection(List<IChromosome> chromosomes, int size);
}
