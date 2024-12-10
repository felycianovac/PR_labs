package Laboratory_3.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FTPFetcher {

    private static final String FTP_SERVER = "localhost";
    private static final String FTP_USER = "testuser";
    private static final String FTP_PASS = "testpass";
    private static final String FTP_DIR_PATH = "/home/testuser";
    private static final String WEB_SERVER_URL = "http://localhost:8080/api/products/upload";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private int fileIndex = 1;

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

            if (fileNames != null && fileNames.length > 0) {
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

    private void sendFileToWebServer(InputStream inputStream, String fileName) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(WEB_SERVER_URL).openConnection();
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


    public static void main(String[] args) {
        FTPFetcher fetcher = new FTPFetcher();
        fetcher.startFetching();
    }
}

