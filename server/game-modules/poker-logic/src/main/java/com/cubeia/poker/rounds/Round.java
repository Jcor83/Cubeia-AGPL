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

import com.cubeia.poker.action.PokerAction;

import java.io.Serializable;

/**
 * A round is a specific part of a poker hand, e.g. deal initial
 * cards or betting. It does not concern the entire hand flow.
 *
 * @author Fredrik Johansson, Cubeia Ltd
 */
public interface Round extends Serializable {

    /**
     * A player request to act. The action should
     * be checked before executed so it is valid
     * given the current state.
     *
     * @param action
     */
    public void act(PokerAction action);

    public void timeout();

    public String getStateDescription();

    public boolean isFinished();

    public void visit(RoundVisitor visitor);

    public boolean isWaitingForPlayer(int playerId);

}
