import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class ChatClient implements KeyListener, Constants{
    //temporary container for the chatbox
    JPanel workingpanel;

    //chatbox components
    JTextArea log;
    JTextField chat;

    //player information
    String name;
    Socket socket;

    JFrame mainFrame = new JFrame("PuckPaperScissors Chat");


    public ChatClient(String name, Socket socket){

        //fetch player information
        this.name = name;
        this.socket = socket;

        //customize workingpanel
        this.workingpanel = new JPanel(new BorderLayout());
        this.workingpanel.setMaximumSize(new Dimension(300, 250));
        this.workingpanel.setMinimumSize(new Dimension(200,200));
        this.workingpanel.setPreferredSize(new Dimension(300, 250));

        //customize log an chat
        this.log = new JTextArea(5, 50);
        this.log.setEditable(false);
        this.chat = new JTextField(50);
        this.chat.addKeyListener(this);

        //enable scrolling for log
        JScrollPane scrollpane = new JScrollPane(log);

        //add components to workingpanel
        this.workingpanel.add(scrollpane, BorderLayout.CENTER);
        this.workingpanel.add(chat, BorderLayout.SOUTH);

        //create thread for receiving information
        Thread receive = new Thread(){
            public void run(){
                while(true){
                    try{
                        InputStream inFromServer = socket.getInputStream();
                        DataInputStream in = new DataInputStream(inFromServer);
                        String message = in.readUTF();
                        if(message.startsWith(name)){
                            log.append(message);
                        }
                        else{
                            log.append(message);
                        }
                    }
                    catch(Exception e){}
                }
            }
        };
        receive.start();

        mainFrame.setLocation(mainFrame.getLocation().x+FRAME_WIDTH,mainFrame.getLocation().y);
        mainFrame.setContentPane(workingpanel);
        mainFrame.pack();
        mainFrame.setVisible(true);

        try{
            OutputStream outToServer = socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(name);
        }catch(Exception f){}
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            try{
                OutputStream outToServer = socket.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF(name+":    "+chat.getText());
                chat.setText("");
            }catch(Exception f){}
        }
    }

}