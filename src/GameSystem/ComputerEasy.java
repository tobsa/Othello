package GameSystem;

import SharedSystem.SharedConstants;

public class ComputerEasy extends Player implements SharedConstants {
    private GameGrid gameGrid;
    
    public ComputerEasy(int id, String name, GameGrid gameGrid) {
        super(id, name);
        this.gameGrid = gameGrid;
    }
     
    @Override
    public int computeMove() throws InterruptedException {
        return 0;
    }
}
