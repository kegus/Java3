package Lesson8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final int TIME_OUT = 120000;
    private Socket socket;
    private Server server;
    private AuthService authService;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean autorized = false;

    private String nick = null;

    public String getNick() {
        return nick;
    }

    public ClientHandler(Server server, Socket socket, AuthService authService) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.authService = authService;
            new Thread(() -> {
                try {
                    autorization();
                    read();
                } catch (IOException e) {
                    System.out.println("client read error");
                    //e.printStackTrace();
                } finally {
                    close();
                }
            }).start();
            new Thread(() -> {
                try {
                    Thread.sleep(TIME_OUT);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    System.out.println("sleep error");
                } finally {
                    if (!isAutorized()) close();
                }
            }).start();
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            server.unsubscribe(this, nick);
        }
    }

    private synchronized boolean isAutorized() {
        return autorized;
    }

    private synchronized void setAutorized(boolean val) {
        autorized = val;
    }

    public void sendMsg(String msg){
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void read() {
        while (true) {
            try {
                String str = in.readUTF();
                if (str.equalsIgnoreCase("/end")) {
                    //sendMsg("/serverclosed");

                    break;
                } else
                if (str.startsWith("/w")) {
                    String[] tokens = str.split(" ");
                    StringBuffer s = new StringBuffer();
                    for (int i = 2; i < tokens.length; i++) {
                        s.append(tokens[i] + " ");
                    }
                    String msg = s.toString();
                    server.sendMsgTo(tokens[1], "from " + nick +": " + msg);
                } else
                if (str.startsWith("/list")) {
                    server.sendListTo(nick);
                } else
                    server.broadcast(nick, str);
            } catch (IOException e) {
                //e.printStackTrace();
                break;
            }
        }
    }

        private void autorization() throws IOException {
        while (true) {
            String str = in.readUTF();
            if (str.startsWith("/reg")) {
                String[] tokens = str.split(" ");
                nick = authService.checkNick(tokens[1]);
                if (nick != null){
                    authService.addNick(nick);
                    sendMsg("/regOK");
                } else
                    sendMsg("Такой ник уже есть");
            } else
            if (str.startsWith("/auth")) {
                String[] tokens = str.split(" ");
                nick = tokens[1];
                if (authService.existNick(nick)) {
                    setAutorized(true);
                    sendMsg("/authOK");
                    server.subscribe(this, nick);
                    break;
                } else {
                    sendMsg("Ник не найден");
                }
            } else {
                System.out.println("получено: "+str);
            }
        }
    }

    private void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
//            e.printStackTrace();
        }
        server.unsubscribe(this, nick);
    }
}
