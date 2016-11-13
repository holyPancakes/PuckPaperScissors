import java.net.*;
import java.io.*;
import java.util.Scanner;

//java GreetingClient 127.0.0.1 8080 username

public class ProjectClient {
	
	public static void main(String [] args) {
		
		try {
			String serverName = args[0];
			int port = Integer.parseInt(args[1]);   
			String username = args[2];
			boolean connected = true;              		
			
			String message =null;
			Scanner scanIn = new Scanner(System.in);
			/* Open a ClientSocket and connect to ServerSocket */
			System.out.println("Connecting to " + serverName + " on port " + port);
			//insert missing line here for creating a new socket for client and binding it to a port
			Socket clientSock = new Socket(serverName, port);
			
			DataOutputStream out = new DataOutputStream(clientSock.getOutputStream());
			out.writeUTF(username);
			
			System.out.println("Just connected to " + clientSock.getRemoteSocketAddress());
			/* Send data to the ServerSocket */
			ProjectClientThread clientThread = new ProjectClientThread(clientSock);
			clientThread.start();
			
			while(connected) {
				try {
					System.out.print(">>> ");
					scanIn = new Scanner(System.in);
					message = scanIn.nextLine();
				}
				catch(Exception e) { }
				
				out.writeUTF(username+" > " +message);
				if(message.equals("/quit")){
					connected = false;
				}
			}
			out.close();
			scanIn.close();
			clientSock.close();
			clientThread.connected = false;
		}
		catch(IOException e) {
			e.printStackTrace();
			System.out.println("Cannot find Server");
		}
		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage: java ProjectClient <server ip> <port no.>");
		}
	}
}

