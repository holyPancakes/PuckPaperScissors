import java.net.*;
import java.io.*;

public class ProjectClientThread extends Thread {
	private Socket clientSocket = null;
	public boolean connected = true;
	public ProjectClientThread(Socket s){
		super("ProjectClientThread");
		this.clientSocket = s;
	}
	public void run(){
		while(connected){
			/* Receive data from the ServerSocket */
			try{
				InputStream inFromServer = clientSocket.getInputStream();
				DataInputStream in = new DataInputStream(inFromServer);
				System.out.print(in.readUTF());
			}
			catch(Exception e){}
		}
	}
}
