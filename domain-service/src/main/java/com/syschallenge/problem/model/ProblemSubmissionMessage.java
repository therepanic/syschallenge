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

package com.syschallenge.problem.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Data;

/**
 * Entity class representing a problem submission messages in the system
 *
 * @author therepanic
 * @since 1.0.0
 */
@Data
@Builder
@Table(name = "problem_submission_messages_table")
public class ProblemSubmissionMessage {

    @Id private UUID id;

    @Column("problem_submission_id")
    private UUID problemSubmissionId;

    private ProblemSubmissionMessageSide side;

    private String text;

    @Column("created_at")
    private LocalDateTime createdAt;
}
