package Lesson8;

import java.util.List;

public class AuthService {
    private List<String> nicksLst;

    public synchronized String checkNick(String nick) {
        return (nicksLst.contains(nick)?null:nick);
    }
    public synchronized boolean existNick(String nick) {
        return nicksLst.contains(nick);
    }
    public synchronized void addNick(String nick) {
        if(!existNick(nick)) nicksLst.add(nick);
    }
    public void start(List<String> nicksLst){
        this.nicksLst = nicksLst;
    }
    public void stop(){

    }
}
