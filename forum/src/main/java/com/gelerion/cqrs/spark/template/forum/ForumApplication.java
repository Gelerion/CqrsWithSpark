package com.gelerion.cqrs.spark.template.forum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gelerion.cqrs.spark.template.common.message.bus.MessageBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Created by denis.shuvalov on 28/01/2018.
 */
@SpringBootApplication
public class ForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }

    @Bean
    MessageBus messageBus(KafkaTemplate<String, String> kafka, ObjectMapper mapper) {
        return new MessageBus(kafka, mapper);
    }


}
