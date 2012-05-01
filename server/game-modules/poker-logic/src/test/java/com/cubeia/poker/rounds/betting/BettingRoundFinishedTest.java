/**
 * Copyright (C) 2010 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cubeia.poker.rounds.betting;

import com.cubeia.poker.GameType;
import com.cubeia.poker.PokerState;
import com.cubeia.poker.action.ActionRequestFactory;
import com.cubeia.poker.player.PokerPlayer;
import com.cubeia.poker.variant.texasholdem.TexasHoldemFutureActionsCalculator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class BettingRoundFinishedTest {

    @Mock
    private GameType telesina;
    @Mock
    private PokerState state;
    @Mock
    private PlayerToActCalculator playerToActCalculator;
    @Mock
    private PokerPlayer player1;
    @Mock
    private PokerPlayer player2;
    @Mock
    private PokerPlayer player3;
    private BettingRound round;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(telesina.getState()).thenReturn(state);
        SortedMap<Integer, PokerPlayer> seatingMap = new TreeMap<Integer, PokerPlayer>();
        seatingMap.put(0, player1);
        seatingMap.put(1, player2);
        seatingMap.put(2, player3);
        when(state.getCurrentHandSeatingMap()).thenReturn(seatingMap);

        round = new BettingRound(telesina, 0, playerToActCalculator, new ActionRequestFactory(new NoLimitBetStrategy()), new TexasHoldemFutureActionsCalculator());
    }

    @Test
    public void testNotFinishedWhenNoOneHasActed() {
        when(state.getPlayersReadyToStartHand()).thenReturn(asList(player1, player2, player3));
        when(player1.hasActed()).thenReturn(false);
        when(player2.hasActed()).thenReturn(false);
        when(player3.hasActed()).thenReturn(false);
        assertThat(round.calculateIfRoundFinished(), is(false));
    }

    @Test
    public void testFinishedWhenEverybodyHasActed() {
        when(state.getPlayersReadyToStartHand()).thenReturn(asList(player1, player2, player3));
        when(player1.hasActed()).thenReturn(true);
        when(player2.hasActed()).thenReturn(true);
        when(player3.hasActed()).thenReturn(true);
        assertThat(round.calculateIfRoundFinished(), is(true));
    }

    @Test
    public void testFinishedWhenAllButOneFolded() {
        when(player1.hasFolded()).thenReturn(true);
        when(player2.hasFolded()).thenReturn(true);

        when(state.getPlayersReadyToStartHand()).thenReturn(asList(player1, player2, player3));
        assertThat(round.calculateIfRoundFinished(), is(true));
    }

    @Test
    public void testFinishedWhenAllButOneSittingOut() {
        when(state.getPlayersReadyToStartHand()).thenReturn(asList(player3));
        when(player1.isSittingOut()).thenReturn(true);
        when(player2.isSittingOut()).thenReturn(true);
        when(player3.isSittingOut()).thenReturn(false);
        assertThat(round.calculateIfRoundFinished(), is(false));
    }

    @Test
    public void testNotFinishedWhenAllSittingOut() {
        when(state.isEveryoneSittingOut()).thenReturn(true);
        when(player1.isSittingOut()).thenReturn(true);
        when(player2.isSittingOut()).thenReturn(true);
        when(player3.isSittingOut()).thenReturn(true);
        assertThat(round.calculateIfRoundFinished(), is(false));
    }

    @Test
    public void testFinished3PlayersAllInAndFoldedCombo() {
        when(state.getPlayersReadyToStartHand()).thenReturn(asList(player1, player2, player3));

        when(player1.isAllIn()).thenReturn(true);
        when(player2.hasFolded()).thenReturn(true);

        assertThat(round.calculateIfRoundFinished(), is(false));
    }

}
