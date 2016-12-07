public interface Constants {
    public static final String APP_NAME="PuckPaperScissors";

    /**
     * Game states.
     */
    public static final int GAME_START=0;
    public static final int IN_PROGRESS=1;
    public final int GAME_END=2;
    public final int WAITING_FOR_PLAYERS=3;

    /**
     * Game port
     */
    public static final int PORT=4444;


    /**
     *	Puck Values
     */
    public static final int ROCK=7;
    public static final int PAPER=8;
    public static final int SCISSORS=9;



    public static int SPEED_UNIT=5;
    public static int FRAME_WIDTH=800;
    public static int FRAME_LENGTH=600;

}
