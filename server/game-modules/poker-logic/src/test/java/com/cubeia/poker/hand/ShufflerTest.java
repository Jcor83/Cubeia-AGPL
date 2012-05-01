package com.cubeia.poker.hand;

import org.junit.Test;
import org.mockito.Mockito;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShufflerTest {

    @Test
    public void testShuffle() {
        List<String> list = Arrays.asList("a", "b", "c");
        Random rng = mock(Random.class);
        when(rng.nextInt(Mockito.anyInt())).thenReturn(0, 0, 0);
        List<String> shuffledList = new Shuffler<String>(rng).shuffle(list);
        assertThat(shuffledList, is(Arrays.asList("b", "c", "a")));
    }


    /**
     * Example from: http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#Modern_method
     */
    @Test
    public void testShuffleWikiReferenceData() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        Random rng = mock(Random.class);
        when(rng.nextInt(Mockito.anyInt())).thenReturn(6 - 1, 2 - 1, 6 - 1, 1 - 1, 3 - 1, 3 - 1, 1 - 1);
        List<Integer> shuffledList = new Shuffler<Integer>(rng).shuffle(list);
        assertThat(shuffledList, is(Arrays.asList(7, 5, 4, 3, 1, 8, 2, 6)));
    }

    @Test
    public void testShuffleReallyBigListJustForFun() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        SecureRandom rng = new SecureRandom(new byte[]{1, 2, 3, 4});
        List<Integer> shuffledList = new Shuffler<Integer>(rng).shuffle(list);
        assertThat(shuffledList.size(), is(list.size()));
        assertThat(shuffledList, not(list));
    }
}
