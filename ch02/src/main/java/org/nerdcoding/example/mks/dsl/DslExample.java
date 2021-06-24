package org.nerdcoding.example.mks.dsl;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class DslExample {


    public static void main(final String... args) throws Exception {
        final StreamsBuilder builder = new StreamsBuilder();

        final KStream<String, byte[]> testStream = builder.stream("users");
        testStream.foreach((key, value) -> System.out.println(key + " DSL Hello, " + new String(value, StandardCharsets.UTF_8)));

        final KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), createConfig());
        kafkaStreams.start();

        Thread.sleep(100_000);
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

    private static Properties createConfig() {
        final Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "dev3");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.5.22:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass());

        return config;
    }


}
