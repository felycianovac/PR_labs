package FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class FTPFetcher {

    private static final String FTP_SERVER = "ftp_server";
    private static final String FTP_USER = "testuser";
    private static final String FTP_PASS = "testpass";
    private static final String FTP_DIR_PATH = "/home/testuser";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private int fileIndex = 0;



    public void startFetching() {
        executorService.submit(() -> {
            while (true) {
                System.out.println("Fetching file from FTP server...");
                fetchAndProcessFileFromFTP();
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
    }

    private void fetchAndProcessFileFromFTP() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FTP_SERVER, 21);
            ftpClient.login(FTP_USER, FTP_PASS);
            ftpClient.enterLocalPassiveMode();

            String[] fileNames = ftpClient.listNames(FTP_DIR_PATH);
            Arrays.sort(fileNames);

            if (fileNames != null && fileNames.length > 0 && fileIndex < fileNames.length) {
                String fileName = fileNames[fileIndex % fileNames.length];
                if (fileName.endsWith(".json")) {
                    System.out.println("Fetching file: " + fileName);
                    fetchAndUploadFile(ftpClient, fileName);
                }
                fileIndex++;
            } else {
                System.out.println("No files found in FTP directory.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchAndUploadFile(FTPClient ftpClient, String fileName) {
        try (InputStream inputStream = ftpClient.retrieveFileStream(FTP_DIR_PATH + "/" + fileName)) {
            if (inputStream != null) {
                System.out.println("File fetched successfully: " + fileName);
                sendFileToWebServer(inputStream, fileName);
            } else {
                System.out.println("Failed to fetch file: " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    private void sendFileToWebServer(InputStream inputStream, String fileName) {
        try {
            String leaderAddress = getCurrentLeaderAddress();
            if (leaderAddress == null) {
                System.err.println("No leader available. Aborting upload.");
                return;
            }
            URL url = new URL(leaderAddress + "/api/products/upload");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

            try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
                dos.writeBytes("------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n");
                dos.writeBytes("Content-Type: application/json\r\n");
                dos.writeBytes("\r\n");

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }

                dos.writeBytes("\r\n");
                dos.writeBytes("------WebKitFormBoundary7MA4YWxkTrZu0gW--\r\n");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("File " + fileName + " uploaded successfully to the web server.");
                } else {
                    System.out.println("Failed to upload file " + fileName + ". Response Code: " + responseCode);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

