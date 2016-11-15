import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

public class ProjectServer {
	
	boolean listening = true;
	public static ArrayList<Socket> socketArray = new ArrayList<Socket>();
	public static ArrayList<String> userNames = new ArrayList<String>();
	public static ConcurrentHashMap<Socket,String> sockArray = new ConcurrentHashMap<Socket,String>();
	
	public static void main(String [] args) {
		final int port = Integer.parseInt(args[0]);
		boolean listening = true;
				
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(listening) {
				Socket SOCK = serverSocket.accept();
				
				DataInputStream in = new DataInputStream(SOCK.getInputStream());
				String[] msgparts = in.readUTF().split(": ", 2);
				String username = msgparts[0], message = msgparts[1];
				sockArray.put(SOCK, username);
				
				socketArray.add(SOCK);
				ProjectServerThread chatThread = new ProjectServerThread(SOCK, username, (username+": "+message).toString());
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
