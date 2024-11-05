package Laboratory_2;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class TCPServer {
    private static final int PORT = 12345;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();
    private static boolean writePending = false;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port: " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                String command;
                while ((command = in.readLine()) != null) {
                    processCommand(command, out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void processCommand(String command, PrintWriter out) {
            lock.lock();
            try {
                sleepRandomly();

                if (command.startsWith("WRITE")) {
                    String data = command.substring(6);
                    writeToFile(data);
                    out.println("Data written: " + data);
                } else if (command.equals("READ")) {
                    while (writePending) {
                        condition.await();
                    }
                    String data = readFromFile();
                    out.println("Data read: " + data);
                } else if (command.equals("CLEAR")) {
                    clearFile();
                    out.println("File cleared.");
                } else if (command.equals("LIST")) {
                    String data = readFromFile();
                    out.println("File contents:\n" + data);
                } else {
                    out.println("Invalid command");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        private void writeToFile(String data) {
            String timestamp = getCurrentTimestamp();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("shared_data.txt", true))) {
                writer.write(timestamp + " - " + data);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String readFromFile() {
            StringBuilder data = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader("shared_data.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data.toString();
        }

        private void clearFile() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("shared_data.txt"))) {
                writer.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String getCurrentTimestamp() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.now().format(formatter);
        }

        private void sleepRandomly() {
            try {
                int sleepTime = 1000 + (int) (Math.random() * 6000);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
