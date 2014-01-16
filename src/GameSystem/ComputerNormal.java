package GameSystem;

import SharedSystem.SharedConstants;
import java.util.List;
import java.util.Random;

public class ComputerNormal extends Player implements SharedConstants {
    private GameGrid gameGrid;
    
    public ComputerNormal(int id, String name, GameGrid gameGrid) {
        super(id, name);
        this.gameGrid = gameGrid;
    }
     
    @Override
    public int computeMove() throws InterruptedException {
        return 0;
    }
}
