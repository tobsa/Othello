package GameSystem;

import java.io.IOException;

public abstract class Player {
    private int id;
    private String name;
    
    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getID() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract int computeMove() throws InterruptedException, IOException;
}
