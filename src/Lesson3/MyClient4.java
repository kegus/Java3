package Lesson3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;

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

    private static class History implements Serializable {
        private String logStr;

        public History(String logStr) {
            this.logStr = logStr;
        }

        public String getLogStr() {
            return logStr;
        }
    }
    private List<History> logLst = null;

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
                        String str = "You are registered\n";
                        area.insert(str, 0);
                    } else
                    if (strFromSrv.equalsIgnoreCase("/authOK")) {
                        nickName = nick.getText();
                        topPan.setVisible(false);
                        String str = "Server connected\n";
                        area.insert(str, 0);
                        loadHistory();
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
                    } else {
                        String str = strFromSrv + "\n";
                        area.insert(str, 0);
                        writeHistory(str);
                    }
                }
            } catch (IOException e) {
                String str = "Error reading from server\n";
                area.insert(str, 0);
                writeHistory(str);
                try {
                    closeConnect();
                    Thread.sleep(1500);
                } catch (InterruptedException ie) { }
                //e.printStackTrace();
            }
        });
        tLstnr.start();
    }

    private void writeHistory(String msg){
        if (logLst == null) {
            logLst = new ArrayList<>();
        }
        logLst.add(new History(msg));
    }

    private void SaveHistory() {
        if (nickName != null && !nickName.isEmpty())
        try (ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream("log_"+nickName+".txt"))) {
            writeHistory("Close\n");
            objOut.writeObject(logLst);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }
    private void loadHistory() {
        if (nickName != null && !nickName.isEmpty() && new File("log_"+nickName+".txt").exists())
        try (ObjectInputStream objIn = new ObjectInputStream(new FileInputStream("log_"+nickName+".txt"))) {
            logLst = (ArrayList<History>)objIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (logLst != null) {
            StringBuilder strHis = new StringBuilder();
            int j = 0;
            for (int i = logLst.size()-1; i >= 0; i--) {
                strHis.append(logLst.get(i).getLogStr());
                if (j++ > 100) break;
            }
//            for (History hs: logLst) {
//                strHis.append(hs.getLogStr());
//            }
            area.insert(strHis.toString(), 0);
        }
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
                    SaveHistory();

                    if (socket != null && socket.isConnected()) out.writeUTF("/end");
                    //closeConnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    public MyClient4(){
        drawGUI();
        openConnect();
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
