package Lesson2;

import java.sql.*;
import java.util.List;

public class AuthService {
    private Connection connection;
    private List<String> nicksLst;

    public synchronized String checkNick(String nick) {
        String res = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM test where nick = ?");
            preparedStatement.setString(1, nick);
            ResultSet result = preparedStatement.executeQuery();
            res = (result.next()?null:nick);
        } catch (Exception e) {
            res = "error";
        }
        return res;
//        return (nicksLst.contains(nick)?null:nick);
    }
    public synchronized boolean existNick(String nick) {
        return nicksLst.contains(nick);
    }
    public synchronized void addNick(String nick) {
        if(!existNick(nick)) nicksLst.add(nick);
    }
    public void start(List<String> nicksLst, Connection connection) throws Exception {
        this.nicksLst = nicksLst;
        this.connection = connection;
//        statement = connection.createStatement();
    }
    public void stop(){
//        try { if (statement != null) statement.close(); } catch (Exception e) {}
    }
}
