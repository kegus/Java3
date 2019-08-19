package Lesson6;

import java.io.*;
import java.sql.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;
import java.util.regex.Pattern;

public class MyServer6 {
    public static void main(String[] args) throws IOException {
        new Server();
        System.out.println("Ok");
    }
}

class Server {
    //Logger
    private static final Logger logger = Logger.getLogger(MyServer6.class.getName());
    private static final String logFileName = "server.log";

    //Replacements
    public List<String> checkWords = new ArrayList<>();
    public List<String> replaceWords = new ArrayList<>();

    //Database
    private final boolean USE_DB = false;
    private final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/test";
    private final String USER = "postgres";
    private final String PASS = "admin";
    private Connection connection;

    //Server
    private final int PORT = 8189;
    private AuthService authService;
    private List<ClientHandler> peers;
    private List<String> nicksLst;
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ExecutorService execSrv;

    Server() throws IOException {
        Handler h = new FileHandler(logFileName);
        h.setFormatter(new SimpleFormatter());
        logger.addHandler(h);

        logger.log(Level.SEVERE, "logger started...");

        checkWords.add("Путин лох");
        checkWords.add("Путин чмо");
        checkWords.add("Порошенко бог");
        checkWords.add("Порошенко молодец");

        replaceWords.add("Путин бог");
        replaceWords.add("Путин молодец");
        replaceWords.add("парашенка и зиленский лохи");
        replaceWords.add("парашенка и зиленский чмо");

        authService = new AuthService();
        peers = new CopyOnWriteArrayList<>();
        nicksLst = new CopyOnWriteArrayList<>();
        execSrv = Executors.newCachedThreadPool();
        try {
            if (USE_DB)
                try {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(DB_URL, USER, PASS);
                } catch (Exception ex) {
                    logger.log(Level.WARNING, "Error connect to DB", ex);
                }
            if (connection != null || !USE_DB) {
                authService.start(nicksLst, connection, USE_DB);
                serverSocket = new ServerSocket(PORT);
                //System.out.println("Сервер запущен!");
                logger.log(Level.SEVERE, "Server started...");
                while (true) {
                    socket = serverSocket.accept();
                    logger.log(Level.SEVERE, "Client connected");
                    //System.out.println("Клиент подключился!");

                    execSrv.execute(new ClientHandler(this, socket, authService, logger));
//                    new Thread(new ClientHandler(this, socket, authService)).start();
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error starting server", e);
            //e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "Error close connection to DB", ex);
                }
            }
            try {
                if (connection != null) socket.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error close serverSocket", e);
            }
            authService.stop();
            execSrv.shutdown();
        }

    }

    private String checkMsg(String msg) {
        String str;
        String newStr;
        String resMsg = msg;
        for (int i = 0; i < checkWords.size(); i++) {
            /*str = "(?i)"+checkWords.get(i);
            newStr = replaceWords.get(i);
            resMsg = Pattern.compile(str, Pattern.UNICODE_CASE).matcher(str).replaceAll(newStr);
            //resMsg = msg.replaceAll(str, newStr);*/
            str = checkWords.get(i);
            newStr = replaceWords.get(i);
            if (resMsg.contains(str)) {
                resMsg = resMsg.replaceAll(str, newStr);
            }
        }
        return resMsg;
    }
    public void sendListTo(String nick) {
        StringBuffer lst = new StringBuffer("/list ");
        for (ClientHandler clientHandler : peers) {
            lst.append(clientHandler.getNick()+" ");
        }
        if (lst.length()>0) lst.setLength(lst.length()-1);
        String msg = lst.toString();
        sendMsgTo(nick, msg);
    }

    void sendMsgTo(String nick, String msg) {
        msg = checkMsg(msg);
        for (ClientHandler clientHandler : peers) {
            if (clientHandler.getNick().equals(nick)) {
                clientHandler.sendMsg(msg);
                break;
            }
        }
    }

    void broadcastList() {
        for (ClientHandler clientHandler : peers) {
            String nick = clientHandler.getNick();
            if (nick != null) sendListTo(nick);
        }
    }

    void broadcast(String nick, String msg) {
        msg = checkMsg(msg);
        if (nick != null)
        for (ClientHandler clientHandler : peers) {
            clientHandler.sendMsg(msg);
        }
        broadcastList();
    }

    void subscribe(ClientHandler clientHandler, String nick) {
        peers.add(clientHandler);
        //nicksLst.add(nick);
        broadcast(nick,nick+" connected");
    }

    void unsubscribe(ClientHandler clientHandler, String nick) {
        peers.remove(clientHandler);
        //nicksLst.remove(nick);
        if (nick != null) broadcast(nick,nick+" leave chat");
    }

}
