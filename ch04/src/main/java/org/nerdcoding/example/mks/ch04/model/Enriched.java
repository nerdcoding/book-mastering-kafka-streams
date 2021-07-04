/*
 * Enriched.java
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

public class Enriched {

    private final Long playerId;
    private final Long productId;
    private final String playerName;
    private final String productName;
    private final Double score;

    private Enriched(
            final Long playerId,
            final Long productId,
            final String playerName,
            final String productName,
            final Double score) {

        this.playerId = playerId;
        this.productId = productId;
        this.playerName = playerName;
        this.productName = productName;
        this.score = score;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getProductName() {
        return productName;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Enriched enriched = (Enriched) o;
        return Objects.equals(playerId, enriched.playerId) && Objects.equals(productId,
                enriched.productId) && Objects.equals(playerName,
                enriched.playerName) && Objects.equals(productName,
                enriched.productName) && Objects.equals(score, enriched.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, productId, playerName, productName, score);
    }


    public static final class EnrichedBuilder {

        private Long playerId;
        private Long productId;
        private String playerName;
        private String productName;
        private Double score;

        public EnrichedBuilder withPlayerId(Long playerId) {
            this.playerId = playerId;
            return this;
        }

        public EnrichedBuilder withProductId(Long productId) {
            this.productId = productId;
            return this;
        }

        public EnrichedBuilder withPlayerName(String playerName) {
            this.playerName = playerName;
            return this;
        }

        public EnrichedBuilder withProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public EnrichedBuilder withScore(Double score) {
            this.score = score;
            return this;
        }

        public Enriched build() {
            return new Enriched(playerId, productId, playerName, productName, score);
        }
    }
}
