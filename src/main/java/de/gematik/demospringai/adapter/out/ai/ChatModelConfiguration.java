package de.gematik.demospringai.adapter.out.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Selects the active ChatModel based on the configured AI provider.
 * This resolves the ambiguity when both OpenAI and Ollama starters are on the classpath.
 * Spring injects all ChatModel beans as a Map (bean name → instance).
 * Typical bean names: "openAiChatModel", "ollamaChatModel".
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class ChatModelConfiguration {

    private static final Map<String, String> PROVIDER_TO_BEAN = Map.of(
            "openai", "openAiChatModel",
            "ollama", "ollamaChatModel"
    );

    @Bean
    public ChatModel activeChatModel(AiProperties aiProperties, Map<String, ChatModel> chatModels) {
        log.info("AI provider configured: '{}'. Available models: {}", aiProperties.provider(), chatModels.keySet());

        String beanName = PROVIDER_TO_BEAN.get(aiProperties.provider());

        if (beanName == null) {
            throw new IllegalArgumentException(
                    "Unknown AI provider: '%s'. Supported: %s".formatted(
                            aiProperties.provider(), PROVIDER_TO_BEAN.keySet()));
        }

        ChatModel model = chatModels.get(beanName);
        if (model == null) {
            throw new IllegalStateException(
                    "Provider '%s' is configured but no '%s' bean found. Is the starter on the classpath?"
                            .formatted(aiProperties.provider(), beanName));
        }

        log.info("Using ChatModel: {} (bean: '{}')", model.getClass().getSimpleName(), beanName);
        return model;
    }
}

