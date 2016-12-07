import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

public class ChatReceiveThread extends Thread{
	private Socket sock = null;
	
	public ChatReceiveThread(Socket s){
		super("ProjectClientThread");
		this.sock = s;
	}
	public void run(){
		while(true){
			/* Receive data from the ServerSocket */
			try{
				InputStream inFromServer = sock.getInputStream();
				DataInputStream in = new DataInputStream(inFromServer);
				String messageReceived = in.readUTF();
			}
			catch(Exception e){
				
			}
		}
	}
}
