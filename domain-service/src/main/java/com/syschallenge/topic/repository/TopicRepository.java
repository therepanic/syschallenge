/*
 * Copyright by the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.syschallenge.topic.repository;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.syschallenge.public_.tables.TopicsTable;
import com.syschallenge.topic.model.Topic;

import lombok.RequiredArgsConstructor;

/**
 * Repository for handling topic data persistence operations
 *
 * @author therepanic
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class TopicRepository {

    private final DSLContext ctx;

    /**
     * Finds all topics with pagination
     *
     * @return list of topics
     */
    public List<Topic> findAll() {
        return this.ctx.selectFrom(TopicsTable.TOPICS_TABLE).fetchInto(Topic.class);
    }
}
