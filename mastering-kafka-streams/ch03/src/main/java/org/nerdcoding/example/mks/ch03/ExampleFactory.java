package org.nerdcoding.example.mks.ch03;

import org.apache.kafka.streams.kstream.KStream;

import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;

import javax.inject.Named;
import javax.inject.Singleton;

@Factory
public class ExampleFactory {

    @Singleton
    @Named("example")
    KStream<String, String> exampleStream(ConfiguredStreamBuilder builder) {
        return builder.stream("streams-plaintext-input");
    }
}