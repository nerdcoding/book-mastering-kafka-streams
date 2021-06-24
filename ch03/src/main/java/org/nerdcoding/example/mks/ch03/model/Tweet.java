/*
 * Tweet.java
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

package org.nerdcoding.example.mks.ch03.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tweet {

    private final Long createdAt;
    private final Long id;
    private final String text;
    private final String lang;
    private final boolean retweet;
    private final String source;
    private final User user;

    @JsonCreator
    public Tweet(
            @JsonProperty("createdAt") final Long createdAt,
            @JsonProperty("id") final Long id,
            @JsonProperty("text") final String text,
            @JsonProperty("lang") final String lang,
            @JsonProperty("retweet") final boolean retweet,
            @JsonProperty("source") final String source,
            @JsonProperty("user") final User user) {

        this.createdAt = createdAt;
        this.id = id;
        this.text = text;
        this.lang = lang;
        this.retweet = retweet;
        this.source = source;
        this.user = user;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getLang() {
        return lang;
    }

    public boolean isRetweet() {
        return retweet;
    }

    public String getSource() {
        return source;
    }

    public User getUser() {
        return user;
    }
}
