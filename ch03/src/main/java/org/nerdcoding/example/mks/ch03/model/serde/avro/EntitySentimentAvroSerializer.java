/*
 * TweetAvroSerializer.java
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

package org.nerdcoding.example.mks.ch03.model.serde.avro;


import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Serializer;
import org.nerdcoding.example.mks.ch03.EntitySentiment;

import java.io.ByteArrayOutputStream;

public class EntitySentimentAvroSerializer implements Serializer<EntitySentiment> {

    @Override
    public byte[] serialize(final String topic, final EntitySentiment tweet) {
        final DatumWriter<EntitySentiment> datumWriter = new SpecificDatumWriter<>(EntitySentiment.class);

        final byte[] bytes;
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            final Encoder binaryEncoder = EncoderFactory.get().binaryEncoder(out, null);
            datumWriter.write(tweet, binaryEncoder);
            binaryEncoder.flush();
            bytes = out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return bytes;
    }
}
