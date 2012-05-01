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

package com.cubeia.poker.rounds;

import com.cubeia.poker.rounds.ante.AnteRound;
import com.cubeia.poker.rounds.betting.BettingRound;
import com.cubeia.poker.rounds.blinds.BlindsRound;

/**
 * These will be called at the end of a round
 *
 * @author per.hjelm The master of the universe
 */
public interface RoundVisitor {

    void visit(AnteRound anteRound);

    void visit(BettingRound bettingRound);

    void visit(BlindsRound blindsRound);

    void visit(DealCommunityCardsRound round);

    void visit(DealExposedPocketCardsRound round);

    void visit(DealInitialPocketCardsRound round);

    void visit(ExposePrivateCardsRound exposePrivateCardsRound);


}
