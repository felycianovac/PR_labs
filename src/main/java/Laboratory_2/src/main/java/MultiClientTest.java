import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MultiClientTest {

    private static final int CLIENT_COUNT = 5;

    public static void main(String[] args) {
        for (int i = 0; i < CLIENT_COUNT; i++) {
            final int clientId = i + 1;
            new Thread(() -> {
                try {
                    if (clientId % 2 == 0) {
                        performWrite(clientId);
                    } else {
                        performRead(clientId);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void performWrite(int clientId) throws IOException {
        try (Socket socket = new Socket("localhost", 2121);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String message = "Message from client " + clientId;
            log("Client " + clientId + " sent WRITE " + message + " command");

            out.write("WRITE " + message + System.lineSeparator());
            out.flush();

            String response = in.readLine();
            log("Client " + clientId + " received response: " + response);
        }
    }

    private static void performRead(int clientId) throws IOException {
        try (Socket socket = new Socket("localhost", 2121);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {


            out.write("READ" + System.lineSeparator());
            out.flush();
            log("Client " + clientId + " sent READ command");

            String response;
            while ((response = in.readLine()) != null) {
                log("Client " + clientId + " received: " + response);
            }
        }
    }

    private static void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[" + timestamp + "] " + message);
    }
}
