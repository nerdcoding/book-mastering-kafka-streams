/*
 * User.java
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

public class User {

    private final Long id;
    private final String name;
    private final String description;
    private final String screenName;
    private final String url;
    private final Integer followersCount;
    private final Integer friendsCount;

    @JsonCreator
    public User(
            @JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("description") final String description,
            @JsonProperty("screenName") final String screenName,
            @JsonProperty("url") final String url,
            @JsonProperty("followersCount") final Integer followersCount,
            @JsonProperty("friendsCount") final Integer friendsCount) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.screenName = screenName;
        this.url = url;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
    }

        public Long getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public String getDescription() {
                return description;
        }

        public String getScreenName() {
                return screenName;
        }

        public String getUrl() {
                return url;
        }

        public Integer getFollowersCount() {
                return followersCount;
        }

        public Integer getFriendsCount() {
                return friendsCount;
        }
}
