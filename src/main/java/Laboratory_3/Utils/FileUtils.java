package Laboratory_3.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;


public class FileUtils {

    private static final String FTP_SERVER = "localhost";
    private static final String FTP_USER = "testuser";
    private static final String FTP_PASS = "testpass";
    private static final String FTP_UPLOAD_PATH = "/home/testuser/";


    public static void saveProcessedProductToFile(String filteredProduct, int index) {
        try {
            String fileName = "product_" + index + ".json";
            File processedFile = new File(fileName);


            FileWriter fileWriter = new FileWriter(processedFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);


            String productJson = cleanProductJson(filteredProduct);
            bufferedWriter.write(productJson);

            bufferedWriter.close();
            uploadFileToFTP(processedFile, fileName);


            System.out.println("Processed product saved to " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String cleanProductJson(String productJson) {
        String cleaned = productJson.replaceAll("\\\\", "");
        if(cleaned.startsWith("\"")) {
            cleaned = cleaned.substring(1);
        }
        if (cleaned.endsWith("\"")) {
            cleaned = cleaned.substring(0, cleaned.length() - 1);
        }

        return cleaned;
    }

    private static void uploadFileToFTP(File file, String fileName) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FTP_SERVER, 21);
            ftpClient.login(FTP_USER, FTP_PASS);

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();

            String remoteFilePath = FTP_UPLOAD_PATH + fileName;

            try (InputStream inputStream = new FileInputStream(file)) {
                boolean done = ftpClient.storeFile(remoteFilePath, inputStream);
                if (done) {
                    System.out.println("File uploaded successfully to FTP server at: " + remoteFilePath);
                } else {
                    System.out.println("Failed to upload file.");
                }
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
}

