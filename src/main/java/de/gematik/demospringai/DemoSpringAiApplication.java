package de.gematik.demospringai;

import org.springframework.ai.model.chat.client.autoconfigure.ChatClientAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = ChatClientAutoConfiguration.class)
public class DemoSpringAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringAiApplication.class, args);
    }

}
