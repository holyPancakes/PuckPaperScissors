import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;

public class PuckPaperScissorsClient {
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("PuckPaperScissors");
		init(args);
		
		JPanel mainPanel = (JPanel)mainFrame.getContentPane();
		mainPanel.setLayout(new BorderLayout());
		
		JPanel gameMap = new JPanel();
		gameMap.setPreferredSize(new Dimension(800,800));
		gameMap.setBackground(Color.blue);
		
		JPanel chatPanel = new JPanel(new BorderLayout());
		JTextField chatField = new JTextField(50);
		JTextArea chatLog = new JTextArea(5,50);
		chatLog.setEditable(false);
		JScrollPane chatScroll = new JScrollPane(chatLog);
		chatPanel.add(chatScroll,BorderLayout.CENTER);
		chatPanel.add(chatField,BorderLayout.SOUTH);
		chatPanel.setBackground(Color.LIGHT_GRAY);
		chatPanel.setPreferredSize(new Dimension(100,800));
		
		mainPanel.add(chatPanel,BorderLayout.SOUTH);
		mainPanel.add(gameMap,BorderLayout.CENTER);
		
		JPanel leaderBoard = new JPanel();
		
		mainFrame.setVisible(true);
		mainFrame.pack();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private static void init(String[] args){
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
