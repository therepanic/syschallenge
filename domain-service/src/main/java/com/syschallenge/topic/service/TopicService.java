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

package com.syschallenge.topic.service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.syschallenge.topic.dto.TopicDto;
import com.syschallenge.topic.mapper.TopicToTopicDtoMapper;
import com.syschallenge.topic.model.Topic;
import com.syschallenge.topic.payload.CreateTopicRequest;
import com.syschallenge.topic.payload.UpdateTopicRequest;
import com.syschallenge.topic.repository.TopicRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service for handling topic-related operations
 *
 * @author therepanic
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicToTopicDtoMapper topicToTopicDtoMapper;

    /**
     * Get list of topics
     *
     * @return list of topic DTOs
     */
    public Collection<TopicDto> getAll() {
        return this.topicToTopicDtoMapper.topicListToTopicDtoList(this.topicRepository.findAll());
    }

    /**
     * Get a topic details
     *
     * @param id the topic id whose topic is being got
     * @return a DTO containing the got topic information
     */
    public TopicDto get(UUID id) {
        return this.topicToTopicDtoMapper.topicToTopicDto(this.topicRepository.findById(id));
    }

    /**
     * Creates a topic
     *
     * @param request the topic create details
     * @return a DTO containing the created topic information
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TopicDto create(CreateTopicRequest request) {
        Topic newTopic = Topic.builder().title(request.title()).build();
        newTopic = this.topicRepository.save(newTopic);
        return this.topicToTopicDtoMapper.topicToTopicDto(newTopic);
    }

    /**
     * Updates a topic
     *
     * @param id the topic ID to update
     * @param request the request containing updated topic details
     * @return a DTO containing the updated topic information
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TopicDto update(UUID id, UpdateTopicRequest request) {
        Topic topicToUpdate = Topic.builder().id(id).title(request.title()).build();
        return this.topicToTopicDtoMapper.topicToTopicDto(
                this.topicRepository.update(topicToUpdate));
    }

    /**
     * Deletes a topic details
     *
     * @param id the topic ID
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(UUID id) {
        this.topicRepository.deleteById(id);
    }
}
