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
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.nerdcoding.example.mks.ch03.model.Tweet;
import org.nerdcoding.example.mks.ch03.model.serde.avro.EntitySentimentAvroSerdes;
import org.nerdcoding.example.mks.ch03.model.serde.json.TweetJsonSerdes;

import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Factory
public class TweetTopology {

    private static final String MICRONAUT_STREAM_NAME = "default";

    private static final String CONSUMER_GROUP_ID = "client2";
    private static final String INPUT_TOPIC_NAME = "users";
    private static final String OUTPUT_TOPIC_NAME = "crypto-sentiment";

    @Singleton
    @Named(MICRONAUT_STREAM_NAME)
    KStream<byte[], Tweet> usersStream(@Named(MICRONAUT_STREAM_NAME) final ConfiguredStreamBuilder streamBuilder) {
        addConfiguration(streamBuilder);

        // -- Source Processor
        // Deserialize event
        final KStream<byte[], Tweet> sourceStream = streamBuilder.stream(
                INPUT_TOPIC_NAME,
                Consumed.with(Serdes.ByteArray(), new TweetJsonSerdes())
        );
        // -- Stream Processor
        // Filter out all retweets
        final KStream<byte[], Tweet> filteredStream = sourceStream.filterNot(
                (key, tweet) -> tweet.isRetweet()
        );
        // Branch streams between english and non-english tweets
        final Map<String, KStream<byte[], Tweet>> stringKStreamMap = filteredStream.split(org.apache.kafka.streams.kstream.Named.as("branch-"))
                .branch((key, tweet) -> "en".equals(tweet.getLang()), Branched.as("english"))
                .branch((key, tweet) -> !"en".equals(tweet.getLang()), Branched.as("non-english"))
                .defaultBranch();
        final KStream<byte[], Tweet> englishTweets = stringKStreamMap.get("branch-english");
        final KStream<byte[], Tweet> nonEnglishTweets = stringKStreamMap.get("branch-non-english");
        // Pseudo translate non-english streams
        final KStream<byte[], Tweet> translatedTweets = nonEnglishTweets.mapValues(value -> value);
        // Merge english and translated streams
        final KStream<byte[], Tweet> merged = englishTweets.merge(translatedTweets);

        // Pseudo-Sentiment analysis
        final KStream<byte[], EntitySentiment> enriched = merged
                .flatMapValues(this::pseudoSentimentAnalysis);

        // -- Sink Processor
        enriched.to(
                OUTPUT_TOPIC_NAME,
                Produced.with(
                        Serdes.ByteArray(),
                        new EntitySentimentAvroSerdes()
                )
        );


        // print event
        enriched.foreach((key, entitySentiment) -> System.out.println("*** " + entitySentiment));

        return merged;
    }

    private void addConfiguration(final ConfiguredStreamBuilder streamBuilder) {
        final Properties props = streamBuilder.getConfiguration();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, CONSUMER_GROUP_ID);
        props.put(
                StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                LogAndContinueExceptionHandler.class
        );
        //props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        //props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    public List<EntitySentiment> pseudoSentimentAnalysis(final Tweet tweet) {
        return Arrays.stream(tweet.getText().toLowerCase().split("\\s+"))
                .map(word -> EntitySentiment.newBuilder()
                                .setCreatedAt(tweet.getCreatedAt())
                                .setId(tweet.getId())
                                .setEntity(word)
                                .setText(tweet.getText())
                                .setSalience(randomDouble())
                                .setSentimentScore(randomDouble())
                                .setSentimentMagnitude(randomDouble())
                                .build())
                .collect(Collectors.toList());
    }

    private Double randomDouble() {
        return ThreadLocalRandom.current().nextDouble(0, 1);
    }

}
