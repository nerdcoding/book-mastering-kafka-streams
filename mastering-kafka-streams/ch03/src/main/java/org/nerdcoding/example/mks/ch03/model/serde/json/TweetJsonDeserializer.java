/*
 * TweetDeserializer.java
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

package org.nerdcoding.example.mks.ch03.model.serde.json;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.nerdcoding.example.mks.ch03.model.Tweet;

import java.nio.charset.StandardCharsets;

public class TweetJsonDeserializer implements Deserializer<Tweet> {

    private final ObjectMapper objectMapper;

    public TweetJsonDeserializer(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Tweet deserialize(final String topic, final byte[] data)  {
        try {
            return objectMapper.readValue(
                    new String(data, StandardCharsets.UTF_8),
                    Tweet.class
            );
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
