package Laboratory_3.rabbitMQ;
import Laboratory_2.product.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        if(cleanedMessage.startsWith("\"")){
            cleanedMessage = cleanedMessage.substring(1);
        }
        if(cleanedMessage.endsWith("\"")){
            cleanedMessage = cleanedMessage.substring(0, cleanedMessage.length() - 1);
        }

        sendToServer(cleanedMessage);
    }

    private void sendToServer(String jsonMessage) {
        try {
            if(jsonMessage.startsWith("\"")){
                jsonMessage = jsonMessage.substring(1);
            }else if(jsonMessage.endsWith("\"")){
                jsonMessage = jsonMessage.substring(0, jsonMessage.length() - 1);
            }else {
                System.out.println("Sending JSON Message: " + jsonMessage);

                URL url = new URL(SERVER_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "*/*");
                connection.setRequestProperty("Connection", "keep-alive");
                connection.setRequestProperty("User-Agent", "PostmanRuntime/7.43.0");
                connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(jsonMessage.getBytes("UTF-8"));
                }

                int responseCode = connection.getResponseCode();
//                System.out.println("Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Data sent successfully.");
                } else {
                    System.out.println("Failed to send data. Response Code: " + responseCode);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


}