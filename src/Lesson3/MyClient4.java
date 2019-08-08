package Lesson3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MyClient4 extends JFrame {
    private final String ADDR = "localhost";
    private final int PORT = 8189;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nickName;

    private Thread tLstnr;

    private JTextArea area;
    private JTextArea nicksListArea;
    private JTextField msg;
    private JTextField nick;
    private JTextField regNick;
    private JPanel topPan;
    private JPanel regPan;
    private JPanel nicksListPan;

    public MyClient4(){
        drawGUI();
        openConnect();
    }

    private void openConnect() {
        tLstnr = new Thread(() -> {
            while (true) try {
                while (true) try {
                    socket = new Socket(ADDR, PORT);
                    area.insert("connect to server succesfull\n", 0);
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF("Hello");
                    break;
                } catch (IOException ioe) {
                    area.insert("trying to connect...\n", 0);
                    try {
                        Thread.sleep(3000);
                        if (!topPan.isVisible()) topPan.setVisible(true);
                    } catch (InterruptedException ie) { }
                }

                while (true) {
                    String strFromSrv = in.readUTF();
                    if (strFromSrv.equalsIgnoreCase("/regOK")) {
                        nickName = regNick.getText();
                        regPan.setVisible(false);
                        nick.setText(nickName);
                        area.insert("You are registered\n", 0);
                    } else
                    if (strFromSrv.equalsIgnoreCase("/authOK")) {
                        nickName = nick.getText();
                        topPan.setVisible(false);
                        area.insert("Server connected\n", 0);
                    } else
                    if (strFromSrv.startsWith("/list")) {
                        String[] tokens = strFromSrv.split(" ");
                        nicksListArea.setText("");
                        for (int i = 1; i < tokens.length; i++) {
                            nicksListArea.append(tokens[i]+"\n");
                        }
                    } else
                    if (strFromSrv.equalsIgnoreCase("/end")) {
                        closeConnect();
                        break;
                    } else
                        area.insert(strFromSrv + "\n", 0);
                }
            } catch (IOException e) {
                area.insert("Error reading from server\n", 0);
                try {
                    closeConnect();
                    Thread.sleep(1500);
                } catch (InterruptedException ie) { }
                //e.printStackTrace();
            }
        });
        tLstnr.start();
    }

    private void closeConnect(){
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            in = null; out = null ;socket = null;
        } catch (IOException e) {
            System.out.println("Error close connection");
            //e.printStackTrace();
        }
    }

    private void drawGUI(){
        setBounds(600,100,400,400);
        setTitle("Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        nick = new JTextField();
        regNick = new JTextField();
        new TextPrompt("Введите ник", nick, TextPrompt.Show.ALWAYS);
        new TextPrompt("Регистрация", regNick, TextPrompt.Show.ALWAYS);
        topPan = new JPanel(new BorderLayout());
        regPan = new JPanel(new BorderLayout());
        nicksListPan = new JPanel(new BorderLayout());
        JButton btSendNick = new JButton("Ok");
        JButton btSendRegNick = new JButton("Ok");
        btSendNick.addActionListener(e -> sndNick());
        btSendRegNick.addActionListener(e -> sndRegNick());
        topPan.add(nick, BorderLayout.CENTER);
        topPan.add(btSendNick, BorderLayout.EAST);
        regPan.add(regNick, BorderLayout.CENTER);
        regPan.add(btSendRegNick, BorderLayout.EAST);
        topPan.add(regPan, BorderLayout.NORTH);

        add(topPan, BorderLayout.NORTH);

        msg = new JTextField();
        area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        nicksListArea = new JTextArea();
        nicksListArea.setEditable(false);
        nicksListArea.setLineWrap(true);
        nicksListPan.add(nicksListArea, BorderLayout.CENTER);
        add(nicksListPan, BorderLayout.EAST);
        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel botomPan = new JPanel(new BorderLayout());
        JButton btSendMsg = new JButton("Send");
        btSendMsg.addActionListener(e -> sndMsg());
        msg.addActionListener(e -> sndMsg());
        botomPan.add(msg, BorderLayout.CENTER);
        botomPan.add(btSendMsg, BorderLayout.EAST);

        add(botomPan, BorderLayout.SOUTH);

        //при закрытии окна клиента отключится от сервера и закрыть ресурсы
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    if (socket != null && socket.isConnected()) out.writeUTF("/end");
                    //closeConnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    private void sndRegNick() {
        if (socket != null && socket.isConnected() && !regNick.getText().trim().isEmpty()){
            try {
                out.writeUTF("/reg " + regNick.getText());
            } catch (IOException e) {
                System.out.println("sndRegNick Error writing");
                //e.printStackTrace();
            }
        }
    }

    private void sndNick(){
        if (socket != null && socket.isConnected() && !nick.getText().trim().isEmpty()){
            try {
                out.writeUTF("/auth " + nick.getText());
            } catch (IOException e) {
                System.out.println("sndNick Error writing");
                //e.printStackTrace();
            }
        }
    }

    private void sndMsg(){
        if (socket != null && socket.isConnected() && !msg.getText().trim().isEmpty()){
            try {
                String str = msg.getText();
                if (str.startsWith("/w") || str.startsWith("/list")) {
                    out.writeUTF(str);
                } else {
                    //area.insert("Client: " + str + "\n", 0);
                    out.writeUTF(nickName + ": " + str);
                }
                msg.setText("");
                msg.grabFocus();
            } catch (IOException e) {
                System.out.println("sndMsg Error writing");
                //e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyClient4::new);
    }
}
