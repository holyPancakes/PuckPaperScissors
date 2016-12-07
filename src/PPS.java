import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
/**
 * The game client itself!
 */

public class PPS extends JPanel implements Runnable, Constants{
    /**
     * Main window
     */
    JFrame frame= new JFrame();

	/* Game Sprites
	*/

    ImageIcon rockIcon = new ImageIcon("res/rock.png");
    ImageIcon paperIcon = new ImageIcon("res/paper.png");
    ImageIcon scissorsIcon = new ImageIcon("res/scissors.png");
    ImageIcon grayIcon = new ImageIcon("res/gray.png");
    ImageIcon bg = new ImageIcon("res/bg.jpg");
    Image rockImage = rockIcon.getImage();
    Image paperImage = paperIcon.getImage();
    Image scissorsImage = scissorsIcon.getImage();
    Image grayImage = grayIcon.getImage();
    Image bgImage = bg.getImage();

	/**/



    /**
     * Player position, speed etc.
     */
    int x,y,xspeed=0,yspeed=0,puckVal=ROCK,prevX,prevY,prevPuckVal=ROCK;

    /**
     * Game timer, handler receives data from server to update game state
     */
    Thread t=new Thread(this);

    /**
     * Nice name!
     */
    String myName="Bob";

    /**
     * Player name of others
     */
    String pname;

    /**
     * Server to connect to
     */
    String server="localhost";

    /**
     * Flag to indicate whether this player has connected or not
     */
    boolean connected=false;

    /**
     * get a datagram socket
     */
    DatagramSocket socket = new DatagramSocket();


    /**
     * Placeholder for data received from server
     */
    String serverData;

    /**
     * Offscreen image for double buffering, for some
     * real smooth animation :)
     */
    BufferedImage offscreen;


    /**
     * Basic constructor
     * @param server
     * @param name
     * @throws Exception
     */
    public PPS(String server,String myName) throws Exception{
        this.server=server;
        this.myName=myName;

        frame.setTitle(APP_NAME+":"+myName);
        //set some timeout for the socket
        socket.setSoTimeout(100);

        frame.getContentPane().add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_LENGTH);
        frame.setVisible(true);
        frame.setResizable(false);

        //create the buffer
        offscreen=(BufferedImage)this.createImage(FRAME_WIDTH, FRAME_LENGTH);

        //Some gui stuff again...
        frame.addKeyListener(new KeyHandler());

        //tiime to play
        t.start();
    }

    /**
     * Helper method for sending data to server
     * @param msg
     */
    public void send(String msg){
        try{
            byte[] buf = msg.getBytes();
            InetAddress address = InetAddress.getByName(server);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, UDP_PORT);
            socket.send(packet);
        }catch(Exception e){}

    }

    /**
     * The juicy part!
     */
    public synchronized void run(){

        while(true){
            try{
                Thread.sleep(10);
            }catch(Exception ioe){}

            //Get the data from players
            byte[] buf = new byte[512];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try{
                socket.receive(packet);
            }catch(Exception ioe){}

            serverData = new String(buf);
            serverData = serverData.trim();

            if (!connected && serverData.startsWith("CONNECTED")){
                connected=true;
                System.out.println("Connected.");
            }else if (!connected){
                System.out.println("Connecting..");
                send("CONNECT "+myName);
            }else if (connected){
                offscreen.getGraphics().drawImage(bgImage,0,0,null);

                //reflect the received data from server
                if (serverData.startsWith("PLAYER")){
                    String[] playersInfo = serverData.split(":");
                    for (int i=0;i<playersInfo.length;i++){
                        String[] playerInfo = playersInfo[i].split(" ");
                        String pname =playerInfo[1];
                        int px = Integer.parseInt(playerInfo[2]);
                        int py = Integer.parseInt(playerInfo[3]);
                        int ppuckVal = Integer.parseInt(playerInfo[4]);
                        if(pname.equals(myName)){
                            x=px;
                            y=py;
                        }
                        //draw on the offscreen image
                        if(!pname.equals(myName)){
                            offscreen.getGraphics().drawImage(grayImage,px, py, null);
                        }
                        else{
                            switch(ppuckVal){
                                case ROCK:
                                    offscreen.getGraphics().drawImage(rockImage,px, py, null);
                                    break;
                                case PAPER:
                                    offscreen.getGraphics().drawImage(paperImage,px, py, null);
                                    break;
                                case SCISSORS:
                                    offscreen.getGraphics().drawImage(scissorsImage,px, py, null);
                                    break;
                            }
                        }
                        offscreen.getGraphics().drawString(pname,px,py-8);
                    }
                    //show the changes
                    frame.invalidate();
                    frame.validate();
                    frame.repaint();
                }
                prevX = x;
                prevY = y;

                x+=xspeed;
                if(x<-20){
                    x=FRAME_WIDTH+x-20;
                }
                else if(x>FRAME_WIDTH-20){
                    x%=FRAME_WIDTH-20;
                    x-=20;
                }

                y+=yspeed;
                if(y<-20){
                    y=FRAME_LENGTH+y-20;
                }
                else if(y>FRAME_LENGTH-20){
                    y%=FRAME_LENGTH-20;
                    y-=20;
                }
                System.out.println("PLAYER "+myName+" "+x+" "+y+" PREV:"+prevPuckVal+" PVAL:"+puckVal);
                if(prevX != x || prevY != y || prevPuckVal!=puckVal ){
                    send("PLAYER "+myName+" "+x+" "+y+" "+puckVal);
                    prevPuckVal=puckVal;
                }
            }
        }
    }

    /**
     * Repainting method
     */
    public void paintComponent(Graphics g){
        g.drawImage(offscreen, 0, 0, null);
    }


    class KeyHandler extends KeyAdapter{
        public void keyPressed(KeyEvent ke){
            switch (ke.getKeyCode()){
                case KeyEvent.VK_DOWN:
                    yspeed=SPEED_UNIT;
                    break;
                case KeyEvent.VK_UP:
                    yspeed=-SPEED_UNIT;
                    break;
                case KeyEvent.VK_LEFT:
                    xspeed=-SPEED_UNIT;
                    break;
                case KeyEvent.VK_RIGHT:
                    xspeed=SPEED_UNIT;
                    break;
                case KeyEvent.VK_Z:
                    if(puckVal!=ROCK){
                        prevPuckVal=puckVal;
                        puckVal=ROCK;
                    }
                    break;
                case KeyEvent.VK_X:
                    if(puckVal!=PAPER) {
                        prevPuckVal=puckVal;
                        puckVal = PAPER;
                    }
                    break;
                case KeyEvent.VK_C:
                    if(puckVal!=SCISSORS) {
                        prevPuckVal=puckVal;
                        puckVal = SCISSORS;
                    }
                    break;
            }
        }

        public void keyReleased(KeyEvent ke){
            switch (ke.getKeyCode()){
                case KeyEvent.VK_DOWN:
                    if(yspeed==SPEED_UNIT)yspeed=0;
                    break;
                case KeyEvent.VK_UP:
                    if(yspeed==-SPEED_UNIT)yspeed=0;
                    break;
                case KeyEvent.VK_LEFT:
                    if(xspeed==-SPEED_UNIT)xspeed=0;
                    break;
                case KeyEvent.VK_RIGHT:
                    if(xspeed==SPEED_UNIT)xspeed=0;
                    break;
            }
        }
    }


    public static void main(String args[]) throws Exception{
        if (args.length != 2){
            System.out.println("Usage: java PPS <servername> <player name>");
            System.exit(1);
        }

        new PPS(args[0],args[1]);
        new ChatClient(args[1],new Socket(args[0],TCP_PORT));
    }
}
