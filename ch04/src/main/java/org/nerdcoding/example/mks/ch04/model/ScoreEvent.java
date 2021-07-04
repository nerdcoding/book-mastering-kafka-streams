/*
 * ScoreEvent.java
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


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Objects;

@JsonDeserialize(builder = ScoreEvent.ScoreEventBuilder.class)
public class ScoreEvent {

    private final Long playerId;
    private final Long productId;
    private final Double score;

    private ScoreEvent(final Long playerId, final Long productId, final Double score) {
        this.playerId = playerId;
        this.productId = productId;
        this.score = score;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public Long getProductId() {
        return productId;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ScoreEvent that = (ScoreEvent) o;
        return Objects.equals(playerId, that.playerId) && Objects.equals(productId,
                that.productId) && Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, productId, score);
    }

    public static final class ScoreEventBuilder {

        private Long playerId;
        private Long productId;
        private Double score;

        @JsonProperty("playerId")
        public ScoreEventBuilder withPlayerId(Long playerId) {
            this.playerId = playerId;
            return this;
        }

        @JsonProperty("productId")
        public ScoreEventBuilder withProductId(Long productId) {
            this.productId = productId;
            return this;
        }

        @JsonProperty("score")
        public ScoreEventBuilder withScore(Double score) {
            this.score = score;
            return this;
        }

        public ScoreEvent build() {
            return new ScoreEvent(playerId, productId, score);
        }
    }
}
