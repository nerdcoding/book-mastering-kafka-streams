/*
 * ScoreWIthPlayer.java
 *
 * Copyright (c) 2021, Tobias Koltsch. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 and
 * only version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-2.0.html>.
 */

package org.nerdcoding.example.mks.ch04.model;


import java.util.Objects;

public class ScoreWithPlayer {

    private final ScoreEvent scoreEvent;
    private final Player player;

    public ScoreWithPlayer(final ScoreEvent scoreEvent, final Player player) {
        this.scoreEvent = scoreEvent;
        this.player = player;
    }

    public ScoreEvent getScoreEvent() {
        return scoreEvent;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ScoreWithPlayer that = (ScoreWithPlayer) o;
        return Objects.equals(scoreEvent, that.scoreEvent) && Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scoreEvent, player);
    }
}
