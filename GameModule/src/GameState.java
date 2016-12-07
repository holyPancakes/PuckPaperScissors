import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The class that contains the state of the game.
 * The game state refers the current position of the players etc.
 * @author Joseph Anthony C. Hermocilla
 *
 */


public class GameState implements Constants{
    /**
     * This is a map(key-value pair) of <player name,NetPlayer>
     */
    private Map<String, NetPlayer> players=new HashMap<String, NetPlayer>();

    /**
     * Map of highscores
     */
    private Map<String, Integer> scores= new HashMap<String, Integer>();

    /**
     * Simple constructor
     *
     */
    public GameState(){}

    /**
     * Update the game state. Called when player moves
     * @param name
     * @param player
     */
    public void update(String name, NetPlayer player){
        players.put(name,player);
        checkCollision(name, player);
    }

    private void checkCollision(String name, NetPlayer player){
        int x1 = player.getX();
        int y1 = player.getY();
        for (String key : players.keySet()){
            if(key.equals(name)){continue;}
            int x2 = ((NetPlayer)players.get(key)).getX();
            int y2 = ((NetPlayer)players.get(key)).getY();
            double dist = Math.sqrt((Math.pow(x2-x1,2))+(Math.pow((y2-y1),2)));
            if(dist < 50){
                duel(name,key);
            }
        }
    }

    private void duel(String a, String b){
        NetPlayer playerA = (NetPlayer)players.get(a);
        NetPlayer playerB = (NetPlayer)players.get(b);
        int pA = playerA.getPuckValue();
        int pB = playerB.getPuckValue();
        if(pA==pB){
            //draw
            System.out.println("DRAW");
            playerA.randomizeCoordinate();
            playerB.randomizeCoordinate();
        }
        else if( pA==ROCK&&pB==SCISSORS || pA==PAPER&&pB==ROCK || pA==SCISSORS&&pB==PAPER){
            //pA wins
            System.out.println(playerA.getName()+" wins!");
            playerB.randomizeCoordinate();
            scores.put(a,scores.get(a)+1);
        }
        else if( pB==ROCK&&pA==SCISSORS || pB==PAPER&&pA==ROCK || pB==SCISSORS&&pA==PAPER ){
            //pB wins
            System.out.println(playerB.getName()+" wins");
            playerA.randomizeCoordinate();
            scores.put(b,scores.get(b)+1);
        }
        System.out.println(getScores());
    }

    /**
     * String representation of this object. Used for data transfer
     * over the network
     */
    public String toString(){
        String retval="";
        for(Iterator ite=players.keySet().iterator();ite.hasNext();){
            String name=(String)ite.next();
            NetPlayer player=(NetPlayer)players.get(name);
            retval+=player.toString()+":";
        }
        return retval;
    }

    /**
     * Returns the map
     * @return
     */
    public Map getPlayers(){
        return players;
    }

    /**
     * Initialize Scores to zero
     */
    public void initScores(){
        for (String key : players.keySet()){
            scores.put(key, 0);
        }
    }

    /**
     * Return scores in a string
     */
    public String getScores(){
        String ret = "SCORES ";
        for (String key : scores.keySet()){
            ret+=key+" "+(int)scores.get(key)+":";
        }

        return ret;
    }
}
