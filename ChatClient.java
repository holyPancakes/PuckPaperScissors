import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class ChatClient implements KeyListener{
	JPanel workingpanel;
	JTextArea log;
	JTextField chat;

	String name; 
	static Socket socket;


	public ChatClient(String name, Socket socket){
		this.name = name;
		this.socket = socket;

		this.workingpanel = new JPanel(new BorderLayout());
		this.workingpanel.setPreferredSize(new Dimension(800, 300));

		this.log = new JTextArea(5, 100);
		this.log.setEditable(false);
		this.chat = new JTextField(100);
		this.chat.addKeyListener(this);

		JScrollPane scrollpane = new JScrollPane(log);

		this.workingpanel.add(scrollpane, BorderLayout.CENTER);
		this.workingpanel.add(chat, BorderLayout.SOUTH);

		/*
		Thread send = new Thread(){
			public void run(){
				while(true){
					try{
						InputStream outToServer = socket.getOutputStream();
						DataOutputStream out = new DataOutputStream(outToServer);
						out.writeUTF();
					}
					catch(Exception e){}
				}
			}
		}
		*/

		Thread receive = new Thread(){
			public void run(){
				while(true){
					try{
						InputStream inFromServer = socket.getInputStream();
						DataInputStream in = new DataInputStream(inFromServer);
						log.append(in.readUTF());
					}
					catch(Exception e){}
				}
			}
		};


		//send.start();
		receive.start();
	}

	public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
    	if(e.getKeyCode() == KeyEvent.VK_ENTER){
    		try{
    			OutputStream outToServer = socket.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToServer);
				out.writeUTF(chat.getText());
				chat.setText("");
    		}catch(Exception f){}
    	}
    }

    public static void main(String[] args){
    	try{
    		JFrame j = new JFrame();
	    	ChatClient client = new ChatClient(args[2], new Socket(args[0], Integer.parseInt(args[1])));
	    	j.setContentPane(client.workingpanel);
	    	j.pack();
	    	j.setVisible(true);
	    	OutputStream outToServer = socket.getOutputStream();
		DataOutputStream out = new DataOutputStream(outToServer);
		out.writeUTF(args[2]);
    	}catch(Exception e){}
    	

    }
}
