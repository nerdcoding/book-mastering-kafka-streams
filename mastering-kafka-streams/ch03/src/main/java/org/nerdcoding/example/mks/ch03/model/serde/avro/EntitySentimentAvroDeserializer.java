/*
 * TweetAvroDeserializer.java
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

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.serialization.Deserializer;
import org.nerdcoding.example.mks.ch03.EntitySentiment;

public class EntitySentimentAvroDeserializer implements Deserializer<EntitySentiment> {

    @Override
    public EntitySentiment deserialize(final String topic, final byte[] data) {
        try {
            final DatumReader<EntitySentiment> reader = new SpecificDatumReader<>(
                    EntitySentiment.getClassSchema()
            );

            final Decoder binaryDecoder = DecoderFactory.get().binaryDecoder(data, null);
            return reader.read(null, binaryDecoder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
