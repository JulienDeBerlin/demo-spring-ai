package de.gematik.demospringai.adapter.in.web;

import de.gematik.demospringai.application.port.out.ModerationPort;
import de.gematik.demospringai.domain.model.ModerationCategory;
import de.gematik.demospringai.domain.model.ModerationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ModerationPort moderationPort;

    private static final String VALID_REQUEST = """
            {
              "name": "Julien",
              "age": 35,
              "bio": "I love dancing forró and gardening."
            }
            """;

    @Test
    @DisplayName("POST /profiles → 201 when moderation allows the bio")
    void shouldCreateProfile_whenModerationAllows() throws Exception {
        when(moderationPort.moderate(anyString()))
                .thenReturn(new ModerationResult(true, List.of(), "Content is appropriate."));

        mockMvc.perform(post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Julien"))
                .andExpect(jsonPath("$.age").value(35))
                .andExpect(jsonPath("$.bio").value("I love dancing forró and gardening."));
    }

    @Test
    @DisplayName("POST /profiles → 400 when moderation rejects the bio")
    void shouldRejectProfile_whenModerationDenies() throws Exception {
        when(moderationPort.moderate(anyString()))
                .thenReturn(new ModerationResult(
                        false,
                        List.of(ModerationCategory.OFF_PLATFORM_CONTACT, ModerationCategory.SPAM_OR_SCAM),
                        "Bio contains off-platform contact information."
                ));

        String request = """
                {
                  "name": "Julien",
                  "age": 35,
                  "bio": "Add me on telegram @julien123 for a good time"
                }
                """;

        mockMvc.perform(post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bio contains off-platform contact information."))
                .andExpect(jsonPath("$.categories.length()").value(2))
                .andExpect(jsonPath("$.categories[0]").value("OFF_PLATFORM_CONTACT"))
                .andExpect(jsonPath("$.categories[1]").value("SPAM_OR_SCAM"));
    }

    @Test
    @DisplayName("POST /profiles → 503 when moderation service fails technically")
    void shouldReturn503_whenModerationServiceThrowsException() throws Exception {
        when(moderationPort.moderate(anyString()))
                .thenThrow(new RuntimeException("Connection to OpenAI timed out"));

        mockMvc.perform(post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("Service temporarily unavailable. Please try again later."))
                .andExpect(jsonPath("$.categories").isEmpty());
    }
}

