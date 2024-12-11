package rabbitMQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



@Service
public class RabbitMQConsumer{

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);


    @RabbitListener(queues = "${rabbitmq.queue.name}")
    private void consume(String message) {
        String cleanedMessage = message.replace("\\", "");
//        sendToServer(cleanedMessage);
        if(cleanedMessage.startsWith("\"")){
            cleanedMessage = cleanedMessage.substring(1);
        }
        if(cleanedMessage.endsWith("\"")){
            cleanedMessage = cleanedMessage.substring(0, cleanedMessage.length() - 1);
        }

        sendToServer(cleanedMessage);
    }

    private String getCurrentLeaderAddress() {
        try {
            URL url = new URL("http://localhost:8080/api/current-leader");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String leaderAddress = in.readLine();
            in.close();

            return leaderAddress;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void sendToServer(String jsonMessage) {
        try {
            String leaderAddress = getCurrentLeaderAddress();
            if (leaderAddress == null) {
                System.err.println("No leader available. Aborting.");
                return;
            }
            if(jsonMessage.startsWith("\"")){
                jsonMessage = jsonMessage.substring(1);
            }else if(jsonMessage.endsWith("\"")){
                jsonMessage = jsonMessage.substring(0, jsonMessage.length() - 1);
            }else {
                System.out.println("sending message to" + leaderAddress + "/api/products/create");
                System.out.println("Sending JSON Message: " + jsonMessage);

                URL url = new URL(leaderAddress + "/api/products/create");
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