package GameSystem;

import SharedSystem.SharedConstants;
import java.util.List;
import java.util.Random;

public class ComputerEasy extends Player implements SharedConstants {
    private GameGrid gameGrid;
    
    public ComputerEasy(int id, String name, GameGrid gameGrid) {
        super(id, name);
        this.gameGrid = gameGrid;
    }
     
    @Override
    public int computeMove() throws InterruptedException {        
        List<Integer> legalMoves = gameGrid.getLegalMoves(getID());
           
        Thread.sleep(500);
        
        if(legalMoves.isEmpty())
            return -1;        
        if(legalMoves.size() == 1)
            return legalMoves.get(0);
        
        return legalMoves.get(new Random().nextInt(legalMoves.size() - 1));
    }
}
