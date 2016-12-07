import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread {
    private Socket serverSocket = null;
    private String username = "";
    String msg = "";

    public ChatServerThread(Socket socket, String uname) {
        super("ChatServerThread");
        this.serverSocket = socket;
        this.username = uname;
    }

    public ChatServerThread(Socket socket, String uname, String msg) {
        super("ChatServerThread");
        this.serverSocket = socket;
        this.username = uname;
        this.msg = msg;
    }
    public void run() {
        try {
            //accepting connection from client
            System.out.println("Just connected: " + serverSocket.getRemoteSocketAddress());

            while(!msg.equals("/quit")) {
				/* Read data from the ClientSocket */
                for(Socket s : ChatServer.socketArray) {
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    out.writeUTF(msg+"\n");
                }
                DataInputStream in = new DataInputStream(serverSocket.getInputStream());
                msg=in.readUTF();
                System.out.println(msg);
				/*
				for(Socket s : ProjectServer.socketArray) {
					DataOutputStream out = new DataOutputStream(s.getOutputStream());
					out.writeUTF(msg+"\n");
				}
				*/
            }
        }
        catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        }
        catch(IOException e) {
            //e.printStackTrace();
            //System.out.println("Usage: java ProjectServer <port no.>");
        }
        ChatServer.socketArray.remove(serverSocket);
        System.out.println("Connected Users:"+ChatServer.socketArray.size());
    }
}
