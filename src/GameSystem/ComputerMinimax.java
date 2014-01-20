package GameSystem;

import SharedSystem.SharedConstants;
import java.util.ArrayList;
import java.util.List;

public class ComputerMinimax extends Player implements SharedConstants {
    private static final int DEPTH_LIMIT = 5;
    
    private GameGrid gameGrid;
    private int nodes;
    
    public ComputerMinimax(int id, String name, GameGrid gameGrid) {
        super(id, name);
        this.gameGrid = gameGrid;
    }
     
    @Override
    public int computeMove() throws InterruptedException {        
        return minimaxDecision(gameGrid.clone());
    }
        
    private int minimaxDecision(GameGrid grid) {
        List<Integer> legalMoves = grid.getLegalMoves(getID());
        
        nodes = 0;
        
        int bestMove  = -1;
        int bestValue = Integer.MIN_VALUE;
        
        for(int i : legalMoves) {
            int originalIndex = grid.getID(i);
            
            nodes++;
            
            List<Integer> flippedMarkers = new ArrayList();
            grid.replaceID(i, getID(), flippedMarkers);
            int value = minValue(grid, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
            grid.replaceID(i, originalIndex, flippedMarkers);
            
            if(value > bestValue) {
                bestValue = value;
                bestMove = i;
            }
        }
        
        System.out.println("Nodes visited: " + nodes);
        
        return bestMove;
    }
    
    private int maxValue(GameGrid grid, int alpha, int beta, int depth) {
//        if(terminalTest(grid))
//            return utility(grid);
        if(cutoffTest(grid, depth))
            return eval(grid);
        
        List<Integer> legalMoves = grid.getLegalMoves(getID());
        
        int v = Integer.MIN_VALUE;
        
        if(legalMoves.isEmpty()) 
            v = Math.max(v, minValue(grid, alpha, beta, depth));
    
        for(int i : legalMoves) {
            int originalIndex = grid.getID(i);
            
            nodes++;
            depth++;
            
            List<Integer> flippedMarkers = new ArrayList();
            grid.replaceID(i, getID(), flippedMarkers);
            v = Math.max(v, minValue(grid, alpha, beta, depth));
            grid.replaceID(i, originalIndex, flippedMarkers);
            
            depth--;
            
            if(v >= beta)
                return v;
            alpha = Math.max(alpha, v);
        }
        
        return v;
    }
    
    private int minValue(GameGrid grid, int alpha, int beta, int depth) {
//        if(terminalTest(grid))
//            return utility(grid);
        if(cutoffTest(grid, depth))
            return eval(grid);
        
        List<Integer> legalMoves = grid.getLegalMoves(getID() == PLAYER_1 ? PLAYER_2 : PLAYER_1);
        
        int v = Integer.MAX_VALUE;
        
        if(legalMoves.isEmpty()) 
            v = Math.min(v, maxValue(grid, alpha, beta, depth));
    
        for(int i : legalMoves) {
            int originalIndex = grid.getID(i);
            
            nodes++;
            depth++;
            
            List<Integer> flippedMarkers = new ArrayList();
            grid.replaceID(i, getID() == PLAYER_1 ? PLAYER_2 : PLAYER_1, flippedMarkers);
            v = Math.min(v, maxValue(grid, alpha, beta, depth));
            grid.replaceID(i, originalIndex, flippedMarkers);
            
            depth--;
            
            if(v <= alpha)
                return v;
            beta = Math.min(beta, v);
        }
        
        return v;
    }
    
    private boolean cutoffTest(GameGrid grid, int depth) {
        if(terminalTest(grid) || depth > DEPTH_LIMIT)
            return true;
        
        return false;
    }
    
    private int eval(GameGrid grid) {
        return mobility(grid) + brickParity(grid) + capturedCorners(grid) + closeCorners(grid);
    }
    
    private int mobility(GameGrid grid) {        
        int moves1 = grid.getLegalMoves(getID()).size();
        int moves2 = grid.getLegalMoves(getID() == PLAYER_1 ? PLAYER_2 : PLAYER_1).size();
        
        if(moves1 + moves2 > 2)
            return 100 * (moves1 - moves2) / (moves1 + moves2);
        
        return 0;
    }
    
    private int brickParity(GameGrid grid) {        
        int score1 = grid.getBricks(getID());
        int score2 = grid.getBricks(getID() == PLAYER_1 ? PLAYER_2 : PLAYER_1);
        
        return 100 * (score1 - score2) / (score1 + score2);
    }
    
    private int closeCorners(GameGrid grid) {
        int size = GRID_SIZE;
        int last = size * size;
        
        int points = 0;
        
        points -= calculateNearCorners(grid, 0, 1, size, size + 1);
        points -= calculateNearCorners(grid, size - 1, size - 2, size + size - 1, size + size - 2);
        points -= calculateNearCorners(grid, last - size, last - size + 1, last - size - size, last - size - size + 1);
        points -= calculateNearCorners(grid, last - 1, last - 2, last - size - 1, last - size - 2);
        
        return points * 35;
    }
    
    private int capturedCorners(GameGrid grid) {        
        int corners1 = calculateCorners(grid, getID());
        int corners2 = calculateCorners(grid, getID() == PLAYER_1 ? PLAYER_2 : PLAYER_1);
        
        if((corners1 + corners2) != 0)
            return 100 * (corners1 - corners2) / (corners1 + corners2);
        
        return 0;
    }
    
    private int calculateNearCorners(GameGrid grid, int c1, int c2, int c3, int c4) {
        int points = 0;
        
        if(grid.getID(c1) == getID())
            points++;
        if(grid.getID(c2) == getID())
            points++;
        if(grid.getID(c3) == getID())
            points++;
        if(grid.getID(c4) == getID())
            points++;
        
        return points;
    }
    
    private int calculateCorners(GameGrid grid, int id) {
        int corners = 0;
        
        if(grid.getID(0) == id)
            corners++;
        if(grid.getID(GRID_SIZE - 1) == id)
            corners++;
        if(grid.getID(GRID_SIZE * GRID_SIZE - GRID_SIZE) == id)
            corners++;
        if(grid.getID(GRID_SIZE * GRID_SIZE - 1) == id)
            corners++;
        
        return corners;
    }
        
    private boolean terminalTest(GameGrid grid) {
        int result = grid.getResult();
        return result == RESULT_PLAYER_1_WON || result == RESULT_PLAYER_2_WON || result == RESULT_DRAW;
    }
    
    private int utility(GameGrid grid) {
        return grid.getBricks(getID());
    }
}
