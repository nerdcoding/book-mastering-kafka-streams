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
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.nerdcoding.example.mks.ch04.model.Player;
import org.nerdcoding.example.mks.ch04.model.Product;
import org.nerdcoding.example.mks.ch04.model.ScoreEvent;
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
    KStream<byte[], ScoreEvent> createLeaderboardTopology(final ConfiguredStreamBuilder streamBuilder) {
        addConfiguration(streamBuilder);

        final KStream<byte[], ScoreEvent> scoreEvents = streamBuilder.stream(
                SCORE_EVENT_TOPIC_NAME,
                Consumed.with(Serdes.ByteArray(), new JsonSerde<>(ScoreEvent.class))
        );

        final KTable<String, Player> players = streamBuilder.table(
                PLAYER_TOPIC_NAME,
                Consumed.with(Serdes.String(), new JsonSerde<>(Player.class))
        );

        final GlobalKTable<String, Product> products = streamBuilder.globalTable(
                PRODUCT_TOPIC_NAME,
                Consumed.with(Serdes.String(), new JsonSerde<>(Product.class))
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
