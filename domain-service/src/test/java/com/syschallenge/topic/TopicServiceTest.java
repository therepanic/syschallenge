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

package com.syschallenge.topic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.syschallenge.topic.dto.TopicDto;
import com.syschallenge.topic.mapper.TopicToTopicDtoMapper;
import com.syschallenge.topic.model.Topic;
import com.syschallenge.topic.repository.TopicRepository;
import com.syschallenge.topic.service.TopicService;

/**
 * @author panic08
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class TopicServiceTest {
    @Mock private TopicRepository topicRepository;

    @Mock private TopicToTopicDtoMapper topicToTopicDtoMapper;

    @InjectMocks private TopicService topicService;

    @Test
    void getAll_returnsListOfTopicDtos() {
        // given
        List<Topic> topics =
                List.of(Topic.builder().id(UUID.randomUUID()).title("Load Balancing").build());
        List<TopicDto> dtos = List.of(new TopicDto(topics.get(0).getId(), "System Design"));
        when(topicRepository.findAll()).thenReturn(topics);
        when(topicToTopicDtoMapper.topicListToTopicDtoList(topics)).thenReturn(dtos);

        // when
        List<TopicDto> result = topicService.getAll();

        // then
        assertNotNull(result);
        assertEquals(dtos.size(), result.size());
        assertEquals(dtos, result);
        verify(topicRepository).findAll();
        verify(topicToTopicDtoMapper).topicListToTopicDtoList(topics);
    }
}
