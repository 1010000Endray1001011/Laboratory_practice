package main.java;
import java.io.*;
import java.net.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server {
    private static final int PORT = 8080;
    private static Map<String, PrintWriter> clients = new HashMap<>();
    private static final String CLIENTS_FILE = "clients.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        loadClientsFromFile();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новое подключение: " + clientSocket);

                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                // Запрашиваем имя пользователя
                out.println("Введите ваше имя:");
                username = in.readLine();

                // Проверяем, не занято ли имя
                while (clients.containsKey(username)) {
                    out.println("Это имя уже занято. Введите другое имя:");
                    username = in.readLine();
                }

                // Добавляем клиента
                clients.put(username, out);
                saveClientToFile(username, socket.getInetAddress().getHostAddress());

                // Оповещаем всех о новом пользователе
                broadcast(formatMessage("SERVER", username + " присоединился к чату"));

                String message;
                while ((message = in.readLine()) != null) {
                    if ("exit".equalsIgnoreCase(message)) {
                        break;
                    }
                    broadcast(formatMessage(username, message));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (username != null) {
                    clients.remove(username);
                    broadcast(formatMessage("SERVER", username + " покинул чат"));
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String formatMessage(String username, String message) {
        String time = LocalDateTime.now().format(formatter);
        return String.format("[%s] %s: %s", time, username, message);
    }

    private static void broadcast(String message) {
        System.out.println(message); // Логирование на сервере
        for (PrintWriter client : clients.values()) {
            client.println(message);
            client.flush();
        }
    }

    private static void loadClientsFromFile() {
        try {
            File file = new File(CLIENTS_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveClientToFile(String username, String clientAddress) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CLIENTS_FILE, true))) {
            writer.println(username + "," + clientAddress + "," + LocalDateTime.now().format(formatter));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}