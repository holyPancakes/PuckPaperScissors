import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.net.Socket;

public class Player {
	
	private Socket sock;
	private String username;
	private Point2D.Double position;
	private double velocity;
	private BufferedImage image;
	private int powerup;
	
	public Player(String username, Socket sock){
		this.username = username;
		this.velocity = 0;
	}
	
	public Socket getSocket(){
		return sock;
	}
	
	public String getUsername(){
		return username;
	}
	
	public Point2D.Double getPosition(){
		return position;
	}
	
	public double getVelocity(){
		return velocity;
	}
	
}
