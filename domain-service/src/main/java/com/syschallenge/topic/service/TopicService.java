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

import org.springframework.stereotype.Service;

import com.syschallenge.topic.dto.TopicDto;
import com.syschallenge.topic.mapper.TopicToTopicDtoMapper;
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
}
