/*
 * UsersStream.java
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

package org.nerdcoding.example.mks.ch03;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Properties;

@Factory
public class UsersStream {

    private static final String MICRONAUT_STREAM_NAME = "default";
    private static final String INPUT_TOPIC_NAME = "users";
    private static final String CONSUMER_GROUP_ID = "client1";

    @Singleton
    @Named(MICRONAUT_STREAM_NAME)
    KStream<String, String> usersStream(@Named(MICRONAUT_STREAM_NAME) final ConfiguredStreamBuilder streamBuilder) {
        final Properties props = streamBuilder.getConfiguration();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, CONSUMER_GROUP_ID);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final KStream<String, String> sourceTopic = streamBuilder.stream(INPUT_TOPIC_NAME);
        sourceTopic.foreach((key, value) -> System.out.println("****** " + key + " -> " + value));

        return sourceTopic;
    }

}
