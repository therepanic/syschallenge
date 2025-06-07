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
import java.util.UUID;

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

    /**
     * Finds a topic by id
     *
     * @param id topic id to search for
     * @return topic entity or null if not found
     */
    public Topic findById(UUID id) {
        return this.ctx
                .selectFrom(TopicsTable.TOPICS_TABLE)
                .where(TopicsTable.TOPICS_TABLE.ID.eq(id))
                .fetchOneInto(Topic.class);
    }

    /**
     * Saves a new topic to the database
     *
     * @param topic entity to save
     * @return topic entity with updated information
     */
    public Topic save(Topic topic) {
        return this.ctx
                .insertInto(TopicsTable.TOPICS_TABLE)
                .set(TopicsTable.TOPICS_TABLE.TITLE, topic.getTitle())
                .returningResult(TopicsTable.TOPICS_TABLE)
                .fetchOneInto(Topic.class);
    }

    /**
     * Updates a topic to the database
     *
     * @param topic entity to update
     * @return topic entity with updated information
     */
    public Topic update(Topic topic) {
        return this.ctx
                .update(TopicsTable.TOPICS_TABLE)
                .set(TopicsTable.TOPICS_TABLE.TITLE, topic.getTitle())
                .where(TopicsTable.TOPICS_TABLE.ID.eq(topic.getId()))
                .returningResult(TopicsTable.TOPICS_TABLE)
                .fetchOneInto(Topic.class);
    }

    /**
     * Deletes a topic based on the id to the database
     *
     * @param id topic id to search for
     */
    public void deleteById(UUID id) {
        this.ctx
                .deleteFrom(TopicsTable.TOPICS_TABLE)
                .where(TopicsTable.TOPICS_TABLE.ID.eq(id))
                .execute();
    }
}
