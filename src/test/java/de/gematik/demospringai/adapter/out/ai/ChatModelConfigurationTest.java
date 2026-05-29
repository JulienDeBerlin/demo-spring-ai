package de.gematik.demospringai.adapter.out.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

/**
 * Unit test for ChatModelConfiguration provider selection logic.
 * Does not start a Spring context — fast and deterministic.
 */
class ChatModelConfigurationTest {

    private final ChatModelConfiguration config = new ChatModelConfiguration();
    private final ChatModel ollamaModel = mock(ChatModel.class);
    private final ChatModel openAiModel = mock(ChatModel.class);

    private final Map<String, ChatModel> chatModels = Map.of(
            "ollamaChatModel", ollamaModel,
            "openAiChatModel", openAiModel
    );

    @Test
    void shouldSelectOllamaModel_whenProviderIsOllama() {
        ChatModel result = config.activeChatModel(new AiProperties("ollama"), chatModels);
        assertThat(result).isSameAs(ollamaModel);
    }

    @Test
    void shouldSelectOpenAiModel_whenProviderIsOpenai() {
        ChatModel result = config.activeChatModel(new AiProperties("openai"), chatModels);
        assertThat(result).isSameAs(openAiModel);
    }

    @Test
    void shouldThrow_whenProviderIsUnknown() {
        assertThatThrownBy(() -> config.activeChatModel(new AiProperties("unknown"), chatModels))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown AI provider: 'unknown'");
    }

    @Test
    void shouldThrow_whenProviderConfiguredButStarterMissing() {
        Map<String, ChatModel> onlyOllama = Map.of("ollamaChatModel", ollamaModel);
        assertThatThrownBy(() -> config.activeChatModel(new AiProperties("openai"), onlyOllama))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("no 'openAiChatModel' bean found");
    }
}

