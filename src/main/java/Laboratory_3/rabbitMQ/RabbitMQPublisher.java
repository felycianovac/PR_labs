package Laboratory_3.rabbitMQ;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component

public class RabbitMQPublisher {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendMessage(String message) {
        rabbitTemplate.execute(channel -> {
            // Declare the exchange dynamically (topic exchange)
            channel.exchangeDeclare(exchange, "topic", true, false, null);
            return null;
            });
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        LOGGER.info(String.format("Sending message: %s", message));

    }
}
