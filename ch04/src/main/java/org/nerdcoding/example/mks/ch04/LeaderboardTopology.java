/*
 * StreamConfiguration.java
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

package org.nerdcoding.example.mks.ch04;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.nerdcoding.example.mks.ch04.model.Enriched;
import org.nerdcoding.example.mks.ch04.model.Player;
import org.nerdcoding.example.mks.ch04.model.Product;
import org.nerdcoding.example.mks.ch04.model.ScoreEvent;
import org.nerdcoding.example.mks.ch04.model.ScoreWithPlayer;
import org.nerdcoding.example.mks.ch04.model.serde.JsonSerde;

import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;
import java.util.Properties;

@Factory
public class LeaderboardTopology {

    private static final String CONSUMER_GROUP_ID = "client2";

    public static final String SCORE_EVENT_TOPIC_NAME = "score-events";
    public static final String PLAYER_TOPIC_NAME = "players";
    public static final String PRODUCT_TOPIC_NAME = "products";

    @Singleton
    KStream<String, ScoreEvent> createLeaderboardTopology(final ConfiguredStreamBuilder streamBuilder) {
        addConfiguration(streamBuilder);

        final KStream<String, ScoreEvent> scoreEvents = streamBuilder.stream(
                SCORE_EVENT_TOPIC_NAME,
                Consumed.with(Serdes.ByteArray(), new JsonSerde<>(ScoreEvent.class))
        )
        // To ensure related events are routed to the same partition they must have the same key (co-partitioning).
        // -> re-key the score event
        .selectKey((key, value) -> value.getPlayerId().toString());

        final KTable<String, Player> players = streamBuilder.table(
                PLAYER_TOPIC_NAME,
                Consumed.with(Serdes.String(), new JsonSerde<>(Player.class))
        );

        final GlobalKTable<String, Product> products = streamBuilder.globalTable(
                PRODUCT_TOPIC_NAME,
                Consumed.with(Serdes.String(), new JsonSerde<>(Product.class))
        );

        // join ScoreEvents - Players
        final KStream<String, ScoreWithPlayer> scoreWithPlayers = scoreEvents.join(
                players,
                // ValueJoiner: defines how different records (here ScoreEvent & Player) should be combined.
                ScoreWithPlayer::new,
                // Joined: defines the Serdes to be used to serialize/deserialize inputs of the joined streams.
                Joined.with(
                        Serdes.String(),
                        new JsonSerde<>(ScoreEvent.class),
                        new JsonSerde<>(Player.class)
                )
        );

        // join ScoreWithPlayer - Products
        final KStream<String, Enriched> enriched = scoreWithPlayers.join(
                products,
                // KeyValueMapper: maps key/value pair (from ScoreWithPlayer stream) to key of the Product stream
                (scoreWithPlayerKey, scoreWithPlayerValue) ->
                        String.valueOf(scoreWithPlayerValue.getScoreEvent().getProductId()),
                // ValueJoiner: defines how different records (here ScoreWithPlayer & Product) should be combined.
                (scoreWithPlayer, product) -> new Enriched.EnrichedBuilder()
                        .withPlayerId(scoreWithPlayer.getPlayer().getId())
                        .withProductId(product.getId())
                        .withPlayerName(scoreWithPlayer.getPlayer().getName())
                        .withProductName(product.getName())
                        .withScore(scoreWithPlayer.getScoreEvent().getScore())
                        .build()
        );

        // Grouping is a requirement for the following aggregation. Enforces the processing of related events by the
        // same observer (same as re-keying).
        final KGroupedStream<String, Enriched> groupedEnriched = enriched.groupByKey(
                // Key- and Value Serdes for the grouped records
                Grouped.with(Serdes.String(), new JsonSerde<>(Enriched.class))
        );



        return scoreEvents;
    }

    private void addConfiguration(final ConfiguredStreamBuilder streamBuilder) {
        final Properties props = streamBuilder.getConfiguration();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, CONSUMER_GROUP_ID);
        props.put(
                StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                LogAndContinueExceptionHandler.class
        );
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

}
