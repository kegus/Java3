package Lesson4;

import java.sql.*;
import java.util.List;

public class AuthService {
    private Connection connection;
    private List<String> nicksLst;
    private boolean use_db;
    private PreparedStatement prpdStmnSel;
    private PreparedStatement prpdStmnIns;

    public synchronized String checkNick(String nick) {
        if (use_db) {
            String res = null;
            try {
                prpdStmnSel.setString(1, nick);
                ResultSet result = prpdStmnSel.executeQuery();
                res = (result.next() ? null : nick);
            } catch (Exception e) { }
            return res;
        } else return (nicksLst.contains(nick)?null:nick);
    }
    public synchronized boolean existNick(String nick) {
        if (use_db) {
            boolean res = false;
            try {
                prpdStmnSel.setString(1, nick);
                ResultSet result = prpdStmnSel.executeQuery();
                res = result.next() ;
            } catch (Exception e) { }
            return res;
        } else return nicksLst.contains(nick);
    }
    public synchronized void addNick(String nick) {
        if(!existNick(nick))
            if (use_db) {
                try {
                    prpdStmnIns.setString(1, nick);
                    prpdStmnIns.setString(2, nick);
                    prpdStmnIns.setString(3, nick);
                    prpdStmnIns.executeQuery();
                } catch (Exception e) { }
            } else nicksLst.add(nick);
    }
    public void start(List<String> nicksLst, Connection connection, boolean use_db) throws Exception {
        this.nicksLst = nicksLst;
        this.connection = connection;
        this.use_db = use_db;
        if (use_db) {
            prpdStmnSel = connection.prepareStatement("SELECT * FROM test where nick = ?");
            prpdStmnIns = connection.prepareStatement("INSERT INTO test (login, pswrd, nick) VALUES (?, ?, ?)");
        }
    }
    public void stop(){
        try { if (prpdStmnSel != null) prpdStmnSel.close(); } catch (Exception e) {}
        try { if (prpdStmnIns != null) prpdStmnIns.close(); } catch (Exception e) {}
    }
}
