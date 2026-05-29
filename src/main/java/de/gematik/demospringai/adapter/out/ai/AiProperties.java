package de.gematik.demospringai.adapter.out.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.ai")
public record AiProperties(String provider) {
}

