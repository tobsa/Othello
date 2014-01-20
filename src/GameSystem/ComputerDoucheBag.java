package GameSystem;

import SharedSystem.SharedConstants;

public class ComputerDoucheBag extends Player implements SharedConstants {
    private GameGrid gameGrid;
    
    public ComputerDoucheBag(int id, String name, GameGrid gameGrid) {
        super(id, name);
        this.gameGrid = gameGrid;
    }
     
    @Override
    public int computeMove() throws InterruptedException {        
        Thread.sleep(500);
        return gameGrid.getBestSabotageMove(getID());
    }
}
