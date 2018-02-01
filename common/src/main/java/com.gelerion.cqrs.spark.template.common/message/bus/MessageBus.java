package com.gelerion.cqrs.spark.template.common.message.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gelerion.cqrs.spark.template.common.commands.Command;
import com.gelerion.cqrs.spark.template.common.events.CmdCompleted;
import com.gelerion.cqrs.spark.template.common.events.Event;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created by denis.shuvalov on 28/01/2018.
 */
@Component
public class MessageBus {

    @Value("${kafka.commands-topic}")
    protected String commandsTopic;
    @Value("${kafka.events-topic}")
    protected String eventsTopic;

    private final KafkaTemplate<String, String> producer;
    private final ObjectMapper jasonMapper;
    // Map to keep handlers, that should be called when command has been completed
    private final ConcurrentHashMap<String, Consumer<CmdCompleted>> responses;

    public MessageBus(KafkaTemplate<String, String> producer, ObjectMapper jasonMapper) {
        this.producer = producer;
        this.jasonMapper = jasonMapper;
        this.responses = new ConcurrentHashMap<>();
    }

    @SneakyThrows
    public <T extends Event<T>> void send(final T event) {
        producer.send(eventsTopic, jasonMapper.writeValueAsString(event));
    }

    @SneakyThrows
    public <T extends Command> void send(final T command, final Consumer<CmdCompleted> callback) {
        responses.put(command.id(), callback);
        producer.send(commandsTopic, jasonMapper.writeValueAsString(command));
    }

    @KafkaListener(topics = "${kafka.events-topic}")
    void listen(CmdCompleted event) {
        String eventId = event.getId();

        if (responses.containsKey(eventId)) {
            responses.get(eventId).accept(event);
            responses.remove(eventId);
        }

    }

}
