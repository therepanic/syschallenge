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

package com.syschallenge.problem.repository;

import com.syschallenge.problem.model.ProblemSubmissionMessage;
import com.syschallenge.public_.tables.ProblemSubmissionMessagesTable;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for handling problem data persistence operations
 *
 * @author therepanic
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class ProblemSubmissionMessageRepository {

	private final DSLContext ctx;

	public List<ProblemSubmissionMessage> findByProblemSubmissionIdOrderByCreatedAtAsc(UUID problemSubmissionId,
			int limit) {
		return this.ctx.selectFrom(ProblemSubmissionMessagesTable.PROBLEM_SUBMISSION_MESSAGES_TABLE)
			.where(ProblemSubmissionMessagesTable.PROBLEM_SUBMISSION_MESSAGES_TABLE.PROBLEM_SUBMISSION_ID
				.eq(problemSubmissionId))
			.orderBy(ProblemSubmissionMessagesTable.PROBLEM_SUBMISSION_MESSAGES_TABLE.CREATED_AT.asc())
			.limit(limit)
			.fetchInto(ProblemSubmissionMessage.class);
	}

}
