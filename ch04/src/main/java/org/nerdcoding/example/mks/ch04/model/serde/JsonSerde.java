/*
 * TweetSerdes.java
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

package org.nerdcoding.example.mks.ch04.model.serde;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerde<T> implements Serde<T> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Class<T> genericClassType;

    public JsonSerde(final Class<T> genericClassType) {
        this.genericClassType = genericClassType;
    }

    @Override
    public Serializer<T> serializer() {
        return new JsonSerializer<>(OBJECT_MAPPER);
    }

    @Override
    public Deserializer<T> deserializer() {
        return new JsonDeserializer<>(OBJECT_MAPPER, genericClassType);
    }
}
