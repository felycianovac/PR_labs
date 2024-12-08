package Laboratory_2;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class TCPServer {
    private static final int PORT = 2121;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition writesComplete = lock.newCondition();
    private static final BlockingQueue<Command> readQueue = new LinkedBlockingQueue<>();
    private static boolean processingWrites = true;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port: " + PORT);

            new Thread(TCPServer::processReadCommands).start();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processReadCommands() {
        while (true) {
            try {
                lock.lock();
                try {
                    while (processingWrites) {
                        writesComplete.await();
                    }
                } finally {
                    lock.unlock();
                }

                Command readCommand;
                while ((readCommand = readQueue.poll()) != null) {
                    readCommand.execute();
                }

                lock.lock();
                try {
                    processingWrites = true;
                } finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
                    if (command.startsWith("WRITE")) {
                        processWriteCommand(command, out);
                    } else if (command.equals("READ")) {
                        enqueueReadCommand(command, out);
                    } else if (command.equals("CLEAR")) {
                        processWriteCommand(command, out);
                    } else {
                        out.println("Invalid command");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void processWriteCommand(String command, PrintWriter out) {
            Command writeCommand = new Command(CommandType.WRITE, command, out);
            writeCommand.execute();

            lock.lock();
            try {
                processingWrites = false;
                writesComplete.signalAll();
            } finally {
                lock.unlock();
            }
        }

        private void enqueueReadCommand(String command, PrintWriter out) {
            Command readCommand = new Command(CommandType.READ, command, out);
            readQueue.add(readCommand);
        }
    }

    private static class Command {
        private final CommandType type;
        private final String command;
        private final PrintWriter out;

        public Command(CommandType type, String command, PrintWriter out) {
            this.type = type;
            this.command = command;
            this.out = out;
        }

        public void execute() {
            try {
                sleepRandomly();

                if (type == CommandType.WRITE) {
                    if (command.startsWith("WRITE")) {
                        writeToFile(command.substring(6));
                        out.println("Data written: " + command.substring(6));
                    } else if (command.equals("CLEAR")) {
                        clearFile();
                        out.println("File cleared.");
                    }
                } else if (type == CommandType.READ) {
                    String data = readFromFile();
                    out.println("Data read: " + data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void writeToFile(String data) throws IOException {
            String timestamp = getCurrentTimestamp();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("shared_data.txt", true))) {
                writer.write(timestamp + " - " + data);
                writer.newLine();
            }
        }

        private String readFromFile() throws IOException {
            StringBuilder data = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader("shared_data.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line).append("\n");
                }
            }
            return data.toString();
        }

        private void clearFile() throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("shared_data.txt"))) {
                writer.write("");
            }
        }

        private String getCurrentTimestamp() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.now().format(formatter);
        }

        private void sleepRandomly() {
            try {
                int sleepTime = 1000 + (int) (Math.random() * 6000);
                System.out.println("Sleeping for " + sleepTime + " milliseconds before executing command: " + command);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private enum CommandType {
        WRITE, READ
    }
}
