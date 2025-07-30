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

package com.syschallenge.problem.configuration;

import com.syschallenge.problem.model.ProblemSubmissionMessage;
import com.syschallenge.problem.model.ProblemSubmissionMessageSide;
import com.syschallenge.problem.repository.ProblemSubmissionMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class PerProblemSubmissionChatMemoryAdvisor implements BaseAdvisor {

	private final int contextWindow;

	private final ProblemSubmissionMessageRepository problemSubmissionMessageRepository;

	@Override
	public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
		UUID problemSubmissionMessageId = (UUID) chatClientRequest.context().get("problemSubmissionMessageId");
		List<ProblemSubmissionMessage> history = this.problemSubmissionMessageRepository
			.findByProblemSubmissionIdOrderByCreatedAtAsc(problemSubmissionMessageId, this.contextWindow);
		// since we will receive messages from the newest to the oldest, we need to
		// reverse them
		Collections.reverse(history);
		List<Message> historyMessages = new ArrayList<>(List.of(chatClientRequest.prompt().getSystemMessage()));
		historyMessages.addAll(history.stream().map(msg -> {
			if (msg.getSide().equals(ProblemSubmissionMessageSide.USER)) {
				return new UserMessage(msg.getText());
			}
			else if (msg.getSide().equals(ProblemSubmissionMessageSide.ASSISTANT)) {
				return new AssistantMessage(msg.getText());
			}
			else {
				throw new IllegalArgumentException("Unknown role: " + msg.getSide());
			}
		}).toList());
		historyMessages.addAll(chatClientRequest.prompt().getUserMessages());
		chatClientRequest.prompt().mutate().messages(historyMessages);
		return chatClientRequest;
	}

	@Override
	public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
		return chatClientResponse;
	}

	@Override
	public int getOrder() {
		return Advisor.DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER;
	}

}
