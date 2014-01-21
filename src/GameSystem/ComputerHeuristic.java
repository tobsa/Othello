package GameSystem;

import SharedSystem.SharedConstants;
import java.util.ArrayList;
import java.util.List;

public class ComputerHeuristic extends Player implements SharedConstants {
    private final int DEPTH_LIMIT = 7;
    
    private GameGrid gameGrid;
    private int nodesVisited;
    
    public ComputerHeuristic(int id, String name, GameGrid gameGrid) {
        super(id, name);
        this.gameGrid = gameGrid;
    }
     
    @Override
    public int computeMove() throws InterruptedException {          
        return minimaxDecision(gameGrid.clone());
    }
        
    private int minimaxDecision(GameGrid grid) {
        List<Integer> legalMoves = grid.getLegalMoves(getID());
        
        nodesVisited = 0;
        
        int availableMoves = legalMoves.size();
                        
        int bestMove  = -1;
        int bestValue = Integer.MIN_VALUE;
        int depth = 0;
        
        for(int i : legalMoves) {
            int originalIndex = grid.getID(i);
            
            nodesVisited++;
            depth++;
            
            List<Integer> flippedMarkers = new ArrayList();
            grid.replaceID(i, getID(), flippedMarkers);
            int value = minValue(grid, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
            grid.replaceID(i, originalIndex, flippedMarkers);
            
            depth--;
            
            if(value > bestValue) {
                bestValue = value;
                bestMove = i;
            }
        }
        
        System.out.print("Nodes: " + nodesVisited);
        System.out.println("\tMoves: " + availableMoves);
        
        return bestMove;
    }
    
    private int maxValue(GameGrid grid, int alpha, int beta, int depth) {
        if(cutoffTest(grid, depth))
            return eval(grid);
        
        List<Integer> legalMoves = grid.getLegalMoves(getID());
        
        int v = Integer.MIN_VALUE;
        depth++;            
        
        if(legalMoves.isEmpty()) 
            v = Math.max(v, minValue(grid, alpha, beta, depth));
    
        for(int i : legalMoves) {
            int originalIndex = grid.getID(i);
            
            nodesVisited++;
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
        
        depth--;
        
        return v;
    }
    
    private int minValue(GameGrid grid, int alpha, int beta, int depth) {
        if(cutoffTest(grid, depth))
            return eval(grid);
        
        List<Integer> legalMoves = grid.getLegalMoves(getID() == PLAYER_1 ? PLAYER_2 : PLAYER_1);
        
        int v = Integer.MAX_VALUE;
        
        if(legalMoves.isEmpty()) 
            v = Math.min(v, maxValue(grid, alpha, beta, depth));
    
        for(int i : legalMoves) {
            int originalIndex = grid.getID(i);
            
            nodesVisited++;
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
        int mobility        = 500 * getMobility(grid);
        int parity          = 10 * getBrickParity(grid);
        int capturedCorners = 500 * getCapturerdCorners(grid);
        int stability       = 50 * getStability(grid);
        int closeCorners    = 100 * getCloseCorners(grid);
                        
        return mobility + parity + capturedCorners + stability + closeCorners;
    }
    
    private int getMobility(GameGrid grid) {        
        int moves1 = grid.getLegalMoves(getID()).size();
        int moves2 = grid.getLegalMoves(getID() == PLAYER_1 ? PLAYER_2 : PLAYER_1).size();
        
        if(moves1 + moves2 != 0)
            return 100 * (moves1 - moves2) / (moves1 + moves2);
        
        return 0;
    }
    
    private int getBrickParity(GameGrid grid) {        
        int score1 = grid.getBricks(getID());
        int score2 = grid.getBricks(getID() == PLAYER_1 ? PLAYER_2 : PLAYER_1);
        
        return 100 * (score1 - score2) / (score1 + score2);
    }
    
    private int getCloseCorners(GameGrid grid) {
        int size = GRID_SIZE;
        int last = size * size;
        
        int points1 = 0;
        points1 += calculateNearCorners(grid, getID(), 0, 1, size, size + 1);
        points1 += calculateNearCorners(grid, getID(), size - 1, size - 2, size + size - 1, size + size - 2);
        points1 += calculateNearCorners(grid, getID(), last - size, last - size + 1, last - size - size, last - size - size + 1);
        points1 += calculateNearCorners(grid, getID(), last - 1, last - 2, last - size - 1, last - size - 2);
        
        int id = getID() == PLAYER_1 ? PLAYER_2 : PLAYER_1;
        
        int points2 = 0;
        points2 += calculateNearCorners(grid, getID(), 0, 1, size, size + 1);
        points2 += calculateNearCorners(grid, getID(), size - 1, size - 2, size + size - 1, size + size - 2);
        points2 += calculateNearCorners(grid, getID(), last - size, last - size + 1, last - size - size, last - size - size + 1);
        points2 += calculateNearCorners(grid, getID(), last - 1, last - 2, last - size - 1, last - size - 2);
        
        if(points1 + points2 != 0)
            return 100 * (points1 - points2) / (points1 + points2);
        return 0;
    }
    
    private int getCapturerdCorners(GameGrid grid) {        
        int corners1 = calculateCorners(grid, getID());
        int corners2 = calculateCorners(grid, getID() == PLAYER_1 ? PLAYER_2 : PLAYER_1);
        
        if((corners1 + corners2) != 0)
            return 100 * (corners1 - corners2) / (corners1 + corners2);
        
        return 0;
    }
    
    private int getStability(GameGrid grid) {
        int stability1 = 0;
        int stability2 = 0;
        
        for(int i = 0; i < grid.getSize(); i++) {
            if(grid.getID(i) == getID())
                stability1 += getWeight(i);
            if(grid.getID(i) == (getID() == PLAYER_1 ? PLAYER_2 : PLAYER_1))
                stability2 += getWeight(i);
        }
        
        if(stability1 + stability2 != 0)
            return 100 * (stability1 - stability2) / (stability1 + stability2);
        
        return 0;
    }
    
    private int calculateNearCorners(GameGrid grid, int id, int c1, int c2, int c3, int c4) {
        int points = 0;
        
        if(grid.getID(c1) == id)
            points++;
        if(grid.getID(c2) == id)
            points++;
        if(grid.getID(c3) == id)
            points++;
        if(grid.getID(c4) == id)
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
    
    private int getWeight(int i) {
        final int NW = 0;
        final int NE = GRID_SIZE - 1;
        final int SW = GRID_SIZE * GRID_SIZE - GRID_SIZE;
        final int SE = GRID_SIZE * GRID_SIZE - 1;
        
        int x = i % GRID_SIZE;
        int y = i / GRID_SIZE;
        
        if(i == 0 || i == NE || i == SW || i == SE)
            return 4;
        if(i == NW + GRID_SIZE + 1 || i == NE + GRID_SIZE - 1 || i == SW - GRID_SIZE + 1 || i == SE - GRID_SIZE - 1)
            return -4;
        if(i == NW + 1 || i == NW + GRID_SIZE || i == NE - 1 || i == NE + GRID_SIZE)
            return -3;
        if(i == SW - GRID_SIZE || i == SW + 1 || i == SE - 1 || i == SE - GRID_SIZE)
            return -3;
        if(x > NW + 1 && x < NE - 1 && (y == 0 || y == GRID_SIZE - 1))
            return 2;
        if(y > 1 && y < GRID_SIZE - 2 && (x == 0 || x == GRID_SIZE - 1))
            return 2;
        if(x > NW + 1 && x < NE - 1 && (y == 1 || y == GRID_SIZE - 2))
            return -1;
        if(y > 1 && y < GRID_SIZE - 2 && (x == 1 || x == GRID_SIZE - 2))
            return -1;
        if(x == 2 && (y == 2 || y == GRID_SIZE - 3))
            return 1;
        if(x == GRID_SIZE - 3 && (y == 2 || y == GRID_SIZE - 3))
            return 1;
        if(x == GRID_SIZE / 2 - 1 && (y == GRID_SIZE / 2 - 1 || y == GRID_SIZE / 2))
            return 1;
        if(x == GRID_SIZE / 2 && (y == GRID_SIZE / 2 - 1 || y == GRID_SIZE / 2))
            return 1;
           
        return 0;
    }
}
