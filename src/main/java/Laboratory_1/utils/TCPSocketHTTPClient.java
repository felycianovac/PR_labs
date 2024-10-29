package Laboratory_1.utils;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPSocketHTTPClient {

    public static String sendHttpsGetRequestWithHeaders(String host, String path) {
        StringBuilder response = new StringBuilder();

        try (SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(host, 443);
             //secure connection to the server using TLS
             PrintWriter writer = new PrintWriter(socket.getOutputStream());
             //send data (HTTP request) over the socket's output stream
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println("GET " + path + " HTTP/1.1");
            writer.println("Host: " + host);
            writer.println("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            writer.println("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            writer.println("Accept-Language: en-US,en;q=0.5");
            writer.println("Connection: close");
            writer.println();
            writer.flush();

            String line;
            boolean headerSectionEnded = false;

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    headerSectionEnded = true;
                    continue;
                }

                if (headerSectionEnded) {
                    response.append(line).append("\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

}
