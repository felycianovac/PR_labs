package Laboratory_3.rabbitMQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



@Service
public class RabbitMQConsumer{

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private static final String SERVER_URL = "http://localhost:8080/api/products/create";


    @RabbitListener(queues = "${rabbitmq.queue.name}")
    private void consume(String message) {
        String cleanedMessage = message.replace("\\", "");
        sendToServer(cleanedMessage);
        if (cleanedMessage.startsWith("\"") && cleanedMessage.endsWith("\"")) {
            cleanedMessage = cleanedMessage.substring(1, cleanedMessage.length() - 1);  // Remove first and last quotes
        }

        System.out.println("Received message: " + cleanedMessage);
        sendToServer(cleanedMessage);
    }

    private void sendToServer(String jsonMessage) {
        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = jsonMessage.toString();

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Data sent to server successfully.");
            } else {
                System.out.println("Error sending data to server. Response code: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Error sending POST request: " + e.getMessage());
            e.printStackTrace();
        }
    }
}