import java.net.InetAddress;
import java.util.Random;


public class NetPlayer implements Constants{
    /**
     * The network address of the player
     */
    private InetAddress address;

    /**
     * The port number of
     */
    private int port;

    /**
     * The name of the player
     */
    private String name;

    /**
     * The position of player
     */
    private int x,y;

    /**
     * Life State
     */
    private boolean lifeStatus;

    /**
     * Puck Value
     */
    private int puckValue;



    /**
     * Constructor
     * @param name
     * @param address
     * @param port
     */
    public NetPlayer(String name,InetAddress address, int port){
        this.address = address;
        this.port = port;
        this.name = name;
        this.lifeStatus = true;
        this.puckValue = ROCK;
        this.randomizeCoordinate();
    }

    /**
     * Returns the address
     * @return
     */
    public InetAddress getAddress(){
        return address;
    }

    /**
     * Returns the port number
     * @return
     */
    public int getPort(){
        return port;
    }

    /**
     * Returns the name of the player
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the X coordinate of the player
     * @param x
     */
    public void setX(int x){
        this.x=x;
    }


    /**
     * Returns the X coordinate of the player
     * @return
     */
    public int getX(){
        return x;
    }


    /**
     * Returns the y coordinate of the player
     * @return
     */
    public int getY(){
        return y;
    }

    /**
     * Sets the y coordinate of the player
     * @param y
     */
    public void setY(int y){
        this.y=y;
    }

    /**
     * Returns the life status of the Player
     * @return
     */
    public boolean isAlive(){
        return lifeStatus;
    }

    /**
     * Sets the life status of the player
     */
    public void setLife(boolean lifeStatus){
        this.lifeStatus=lifeStatus;
    }

    /**
     * Returns the puck Value of the Player
     * @return
     */
    public int getPuckValue(){
        return puckValue;
    }

    /**
     * Sets the puck Value of the player
     */
    public void setPuckValue(int puckValue){
        this.puckValue=puckValue;
    }

    /**
     * String representation. used for transfer over the network
     */
    public String toString(){
        String retval="";
        retval+="PLAYER ";
        retval+=name+" ";
        retval+=x+" ";
        retval+=y+" ";
        retval+=puckValue;
        return retval;
    }

    public void randomizeCoordinate(){
        Random r = new Random();
        this.x = r.nextInt(FRAME_WIDTH-20)+10;
        this.y = r.nextInt(FRAME_WIDTH-20)+10;
    }
}
