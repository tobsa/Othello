package SharedSystem;


public interface SharedConstants {
    public static final int GRID_SIZE = 8;
    public static final int IMAGE_SIZE = 100;
    
    public static final int PLAYER_EMPTY = 0;
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;
    
    public static final int TYPE_HUMAN = 0;
    public static final int TYPE_COMPUTER_EASY = 1;
    public static final int TYPE_COMPUTER_NORMAL = 2;
    public static final int TYPE_COMPUTER_HARD = 3;
    public static final int TYPE_NETWORK_READ = 4;
    public static final int TYPE_NETWORK_SEND = 5;
    
    public static final int NETWORK_HOST = 0;
    public static final int NETWORK_CLIENT = 1;
    
    public static final int RESULT_PLAYER_1_WON = 0;
    public static final int RESULT_PLAYER_2_WON = 1;
    public static final int RESULT_DRAW = 2;
    public static final int RESULT_NO_OUTCOME = 3;
}
