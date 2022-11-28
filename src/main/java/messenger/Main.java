package messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

@SuppressWarnings({"InfiniteLoopStatement", "resource"})
public class Main {
    private static String token = "";
    private static BufferedReader reader;
    private static PrintWriter writer;

    public static void main(String[] args) {
        System.out.println("Hello world");
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("try connect");
            try {
                Socket socket = new Socket("localhost", 8080);
                System.out.println("connected to server");
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
                if (true) {
                    return;
                }
                while (true) {
                    System.out.println("--------------------------------------");
                    System.out.println("Please enter command:\nEnter 'exit' for stop program\n/serverTime - current server time\n/register - register new user\n/login - authorization user\n/sendMessage - send message to user\n/readMessages - read messages\n/getUserById - get user by id\n/getUserByLogin - get user by login");
                    String command = consoleReader.readLine();

                    if (command.equals("exit")) {
                        break;
                    }

                    switch (command) {
                        case "/serverTime" -> {
                            getServerAnswer(writer, command, reader, "server time is ");
                        }

                        case "/register" -> {
                            registerUser(consoleReader.readLine(), consoleReader.readLine());
                        }

                        case "/login" -> {
                            loginUser(consoleReader.readLine(), consoleReader.readLine());
                            //answer

                        }

                        case "/sendMessage" -> {
                            writer.println(command);
                            writer.flush();
                            System.out.println("Enter userId to send message: ");
                            String userIdToSendMessage = consoleReader.readLine();
                            System.out.println("Enter text message:");
                            String textMessage = consoleReader.readLine();
                            writer.println(userIdToSendMessage);
                            writer.println(textMessage);
                            writer.println(token);
                            writer.flush();

                            //answer
                            String answer = reader.readLine();
                            System.out.println("Server answer = " + answer);
                        }

                        case "/readMessages" -> {
                            writer.println(command);
                            System.out.println("Enter since_date");
                            writer.println(consoleReader.readLine());
                            writer.println(token);
                            writer.flush();
                            boolean isSuccess = reader.readLine().equals("true");
                            if (isSuccess) {
                                int messagesCount = Integer.parseInt(reader.readLine());
                                ArrayList<Message> messages = new ArrayList<>();
                                for (int i = 0; i < messagesCount; i++) {
                                    Message message = new Message();
                                    message.messageId = Integer.parseInt(reader.readLine());
                                    message.fromUserId = Integer.parseInt(reader.readLine());
                                    message.toUserId = Integer.parseInt(reader.readLine());
                                    message.message = reader.readLine();
                                    message.date = Long.parseLong(reader.readLine());
                                    messages.add(message);
                                }
                                printMessages(messages);
                            } else {
                                String failureReason = reader.readLine();
                                System.out.println("failed to read messages=" + failureReason);
                            }
                        }

                        case "/getUserById" -> {
                            writer.println(command);
                            writer.flush();
                            System.out.println("Enter userId");
                            String userId = consoleReader.readLine();
                            writer.println(userId);
                            writer.flush();
                            writer.println(token);
                            writer.flush();

                            //answer
                            String answer = reader.readLine();
                            System.out.println("Username: " + answer);
                        }

                        case "/getUserByLogin" -> {
                            writer.println(command);
                            writer.flush();
                            System.out.println("Enter username");
                            String userName = consoleReader.readLine();
                            writer.println(userName);
                            writer.println(token);
                            writer.flush();

                            //answer
                            String answerLogin = reader.readLine();
                            System.out.println("Username: " + answerLogin);
                            String answerId = reader.readLine();
                            System.out.println("User Id: " + answerId);
                        }

                        default -> System.out.println("unknown command");
                    }

                    writer.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("disconnected from server");
        }
    }

    public static String loginUser(String login, String password) throws IOException {
        try {
            writer.println("/login");
            writer.flush();
            writer.println(login);
            writer.println(password);
            writer.flush();

            //answer
            String answer = reader.readLine();
            if (!answer.equals("true")) {
                System.out.println("failed to login, answer=" + answer);
                return answer;
            }

            int userId = Integer.parseInt(reader.readLine());
            System.out.println("Userid is: " + userId);
            token = reader.readLine();
            System.out.println("Your token is: " + token);
            return answer;
        } catch (Throwable e) {
            e.printStackTrace();
            return "false";
        }
    }

    private static void printMessages(ArrayList<Message> messages) {

        System.out.println("print messages, count = " + messages.size());
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    private static void getServerAnswer(PrintWriter writer, String command, BufferedReader reader, String x) throws IOException {
        writer.println(command);
        writer.flush();
        String answer = reader.readLine();
        System.out.println(x + answer);
    }

    public static String registerUser(String name, String password) {
        try {
            writer.println("/register");
            writer.flush();
            writer.println(name);
            writer.println(password);
            writer.flush();

            String answer = reader.readLine();
            return answer;
        } catch (Throwable e) {
            e.printStackTrace();
            return "false";
        }
    }


}
