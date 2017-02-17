package net.sf.tweety.commons.tests;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.sf.tweety.commons.util.DefaultSubsetIterator;
import net.sf.tweety.commons.util.IncreasingSubsetIterator;
import net.sf.tweety.commons.util.MathTools;
import net.sf.tweety.commons.util.RandomSubsetIterator;
import net.sf.tweety.commons.util.SubsetIterator;

public class SubsetIteratorTest {

    Set<Integer> integerSet;

    @Before
    public void setUp() {
        integerSet = new HashSet<>();
        for (int i = 0; i < 3; i++)
        {
            integerSet.add(i);
        }
    }

    @Test
    public void increasingSubsetIteratorTest() {
        SubsetIterator<Integer> iterator = new IncreasingSubsetIterator<>(integerSet);
        checkSubsetsSize(iterator, getExpectedSize(integerSet));
    }

    @Test
    public void randomSubsetIteratorTest() {
        SubsetIterator<Integer> iterator = new RandomSubsetIterator<>(integerSet, true);
        checkSubsetsSize(iterator, getExpectedSize(integerSet));
    }

    @Test
    public void defaultSubsetIteratorTest() {
        SubsetIterator<Integer> iterator = new DefaultSubsetIterator<>(integerSet);
        checkSubsetsSize(iterator, getExpectedSize(integerSet));
    }

    private int getExpectedSize(Set<?> set) {
        int expectedSize = 0;
        for (int subsetSize = 0; subsetSize <= set.size(); subsetSize++)
        {
            expectedSize += MathTools.binomial(set.size(), subsetSize);
        }

        return expectedSize;
    }

    private void checkSubsetsSize(SubsetIterator<?> iterator, int expectedSize) {
        int subsetsCount = 0;
        while (iterator.hasNext())
        {
            iterator.next();
            subsetsCount++;
        }

        Assert.assertEquals(expectedSize, subsetsCount);
    }
}
