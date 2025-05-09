package lk.chat.server;

import java.rmi.Naming;

public class ServerThread extends Thread {
    private String hostName;
    private String serviceName;

    public ServerThread(String hostName, String serviceName) {
        this.hostName = hostName;
        this.serviceName = serviceName;
    }

    public void run() {
        try {
            ChatServerITF hello = new ChatServer();
            String rmiUrl = "rmi://" + hostName + "/" + serviceName;
            System.out.println("Binding server to " + rmiUrl);
            Naming.rebind(rmiUrl, hello);
            System.out.println("Group Chat RMI Server("+serviceName+") is running...");
        } catch (Exception e) {
            System.err.println("Server had problems starting: " + e.getMessage());
            e.printStackTrace();
            System.err.println("Check if RMI registry is running and if the hostname '" + hostName + "' is correct.");
        }
    }
}
