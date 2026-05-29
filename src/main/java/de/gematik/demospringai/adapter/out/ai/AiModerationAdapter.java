package de.gematik.demospringai.adapter.out.ai;

import de.gematik.demospringai.application.port.out.ModerationPort;
import de.gematik.demospringai.domain.model.ModerationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * AI-powered moderation adapter.
 * Uses whichever ChatModel is selected by ChatModelConfiguration (OpenAI or Ollama).
 * The business logic is completely provider-agnostic.
 */
@Slf4j
@Service
public class AiModerationAdapter implements ModerationPort {

    private final ChatClient chatClient;

    public AiModerationAdapter(@Qualifier("activeChatModel") ChatModel activeChatModel) {
        this.chatClient = ChatClient.builder(activeChatModel)
                .defaultSystem(ModerationPrompt.SYSTEM_PROMPT)
                .build();
        log.info("AiModerationAdapter initialized with model: {}", activeChatModel.getClass().getSimpleName());
    }

    @Override
    public ModerationResult moderate(String text) {
        log.debug("Sending moderation request to AI ({} chars)", text.length());
        ModerationResult result = chatClient.prompt()
                .user(ModerationPrompt.userPrompt(text))
                .call()
                .entity(ModerationResult.class);
        log.debug("Moderation response: allowed={}", result.allowed());
        return result;
    }
}

