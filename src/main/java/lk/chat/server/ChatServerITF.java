package lk.chat.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;

public interface ChatServerITF extends Remote {
    void updateChat(String var1, String var2) throws RemoteException;

    void passIDentity(RemoteRef var1) throws RemoteException;

    void registerListener(String[] var1) throws RemoteException;

    void leaveChat(String var1) throws RemoteException;

}