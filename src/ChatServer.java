import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer implements Constants{

    boolean listening;
    public static ArrayList<Socket> socketArray = new ArrayList<Socket>();
    public static ArrayList<String> userNames = new ArrayList<String>();
    public static ConcurrentHashMap<Socket,String> sockArray = new ConcurrentHashMap<Socket,String>();

    public ChatServer(){
        final int port = TCP_PORT;
        boolean listening = true;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(listening) {
                Socket SOCK = serverSocket.accept();

                DataInputStream in = new DataInputStream(SOCK.getInputStream());
                String username = in.readUTF();
                sockArray.put(SOCK, username);

                socketArray.add(SOCK);
                ChatServerThread chatThread = new ChatServerThread(SOCK,username);
                chatThread.start();
                System.out.println("Client Added:" + username +"...");
                System.out.println("Connected Users:"+socketArray.size());
            }
            serverSocket.close();
        }
        catch(IOException e) {
            e.printStackTrace();
            System.out.println("Usage: java ProjectServer <port no.>");
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("ARRAY OUT OF BOUNDS ");
        }
    }
}
