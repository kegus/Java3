package Lesson4;

import java.io.*;
import java.sql.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServer5 {
    public static void main(String[] args) {
        new Server();
        System.out.println("Ok");
    }
}

class Server {
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

    Server(){
        authService = new AuthService();
        peers = new CopyOnWriteArrayList<>();
        nicksLst = new CopyOnWriteArrayList<>();
        try {
            if (USE_DB)
                try {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(DB_URL, USER, PASS);
                } catch (Exception ex) {
//                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, ex.getMessage());
                }
            if (connection != null || !USE_DB) {
                authService.start(nicksLst, connection, USE_DB);
                serverSocket = new ServerSocket(PORT);
                System.out.println("Сервер запущен!");
                while (true) {
                    socket = serverSocket.accept();
                    System.out.println("Клиент подключился!");
                    new ClientHandler(this, socket, authService);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, ex.getMessage());
                }
            }
            try {
                if (connection != null) socket.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            authService.stop();
        }

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
