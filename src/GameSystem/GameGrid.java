package GameSystem;

import SharedSystem.IGGListener;
import SharedSystem.SharedConstants;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GameGrid implements SharedConstants {
    private int grid[] = new int[GRID_SIZE * GRID_SIZE];
    private List<IGGListener> listeners = new ArrayList();
    private int whiteBricks = 2;
    private int blackBricks = 2;
    
    public boolean setID(int index, int id) {
        if(index < 0) // Pass move
            return true;        
        if(index >= grid.length || grid[index] != PLAYER_EMPTY)
            return false;
                
        List<Integer> flippedMarkers = getFlippedMarkers(index, id);
        if(flippedMarkers.isEmpty())
            return false;
        
        grid[index] = id;
                                       
        for(int i : flippedMarkers)
            grid[i] = id;
        
        for(IGGListener listener : listeners)
            listener.updateMove(index, id, flippedMarkers);
        
        return true;
    }
        
    public int getID(int index) {
        return grid[index];
    }
    
    public int getSize() {
        return grid.length;
    }
    
    public void replaceID(int index, int id, List<Integer> flippedMarkers) {
        if(flippedMarkers.isEmpty()) {
            flippedMarkers.addAll(getFlippedMarkers(index, id));
            
            for(int i : flippedMarkers)
                grid[i] = id;
        }
        else
            for(int i : flippedMarkers)
                grid[i] = grid[i] == PLAYER_1 ? PLAYER_2 : PLAYER_1;
        
                                               
        grid[index] = id;
    } 
        
    public int getBestLegalMove(int id) {
        List<Integer> legalMoves = getLegalMoves(id);
        
        int index = -1;
        int maxSize  = -1;
        for(int i : legalMoves) {
            int size = getFlippedMarkers(i, id).size();            
            if(size > maxSize) {
                maxSize = size;
                index = i;
            }
        }
               
        return index;
    }
    
    public int getBestSabotageMove(int id) {
        List<Integer> legalMoves = getLegalMoves(id);
        
        GameGrid gameGrid = clone();
        int minBricks = Integer.MAX_VALUE;
        int index = -1;
        for(int i : legalMoves) {
            List<Integer> flippedMarkers = new ArrayList();
            gameGrid.replaceID(i, id, flippedMarkers);
            int bricks = gameGrid.calculateBricks(id == PLAYER_1 ? PLAYER_2 : PLAYER_1);
            gameGrid.replaceID(i, 0, flippedMarkers);
            
            if(bricks < minBricks) {
                minBricks = bricks;
                index = i;
            }
        }
        
        return index;
    }
    
    private int calculateBricks(int id) {
        int bricks = 0;
        for(int i = 0; i < grid.length; i++) {
            if(grid[i] == id)
                bricks++;
        }
        
        return bricks;
    }
        
    public List<Integer> getLegalMoves(int id) {
        List<Integer> legalMoves = new ArrayList();
        
        for(int i = 0; i < GRID_SIZE * GRID_SIZE; i++) { 
            if(grid[i] != PLAYER_EMPTY || legalMoves.contains(new Integer(i)))
                continue;
            
            int x = i % GRID_SIZE;
            int y = i / GRID_SIZE;
            
            checkNorthWest(i, id, legalMoves);
            checkNorthEast(i, id, legalMoves);
            checkSouthWest(i, id, legalMoves);
            checkSouthEast(i, id, legalMoves);
            checkWest(i, id, x, y, legalMoves); 
            checkEast(i, id, x, y, legalMoves);
            checkNorth(i, id, x, y, legalMoves);
            checkSouth(i, id, x, y, legalMoves);
        }
        
        Set<String> s = new LinkedHashSet(legalMoves);
        legalMoves = new ArrayList(s);
        
        return legalMoves;
    }
    
    public void clear() {
        for(int i = 0; i < grid.length; i++)
            grid[i] = PLAYER_EMPTY;
                   
        int index = GRID_SIZE * GRID_SIZE / 2 + GRID_SIZE / 2;
        grid[index - 0] = 1;
        grid[index - 1] = 2;
        grid[index - GRID_SIZE] = 2;
        grid[index - GRID_SIZE - 1] = 1;
//        
//        grid[0] = 2;
//        grid[1] = 1;
//        grid[2] = 1;
//        grid[3] = 1;
//        grid[4] = 2;
//        grid[5] = 1;
        //grid[8] = 1;
        //grid[7] = 2;
                
        for(int i = 0; i < grid.length; i++) {
            for(IGGListener listener : listeners)
                listener.updateMove(i, grid[i], null);
        }
    }
    
    public int getBlackBricks() {
        return blackBricks;
    }
    
    public int getWhiteBricks() {
        return whiteBricks;
    }
    
    public int getBricks(int id) {
        return id == PLAYER_1 ? blackBricks : whiteBricks;
    }
           
    public int getResult() {        
        blackBricks = 0;
        whiteBricks = 0;        
                
        for(int i = 0; i < grid.length; i++) {
            if(grid[i] == PLAYER_1)
                blackBricks++;
            if(grid[i] == PLAYER_2)
                whiteBricks++;
        }
        
        if(!hasLegalMoves(PLAYER_1) && !hasLegalMoves(PLAYER_2)) {
            if(blackBricks > whiteBricks)
                return RESULT_PLAYER_1_WON;
            else if(whiteBricks > blackBricks)
                return RESULT_PLAYER_2_WON;
            else
                return RESULT_DRAW;
        }
        
        return RESULT_NO_OUTCOME;
    }
    
    public void registerListener(IGGListener listener) {
        listeners.add(listener);
    }
    
    public void clearListeners() {
        listeners.clear();
    }
    
    @Override
    public GameGrid clone() {
        GameGrid gameGrid = new GameGrid();
                
        gameGrid.grid = grid.clone();
        gameGrid.whiteBricks = whiteBricks;
        gameGrid.blackBricks = blackBricks;
        
        return gameGrid;
    }
    
    private boolean hasLegalMoves(int id) {
        return !getLegalMoves(id).isEmpty();
    }
        
    private void checkSouthEast(int index, int id, List<Integer> legalMoves) {
        int diagonals = getDiagonals(index, 0, 0);
        int originalIndex = index;  
                
        if(index < 0 || index >= GRID_SIZE * GRID_SIZE || diagonals == 1 || diagonals == 2 || grid[index + GRID_SIZE + 1] == id)
            return;
        
        index = index + GRID_SIZE + 1;
        for(int i = 0; i < diagonals - 1; i++) {            
            if(index < 0 || index >= GRID_SIZE * GRID_SIZE || check(originalIndex, id, index, legalMoves))
                return;
            
            index = index + GRID_SIZE + 1;
        }
    }
    
    private void checkSouthWest(int index, int id, List<Integer> legalMoves) {
        int diagonals = getDiagonals(index, GRID_SIZE - 1, 0);
        int originalIndex = index;  
                
        if(index < 0 || index >= GRID_SIZE * GRID_SIZE || diagonals == 1 || diagonals == 2 || grid[index + GRID_SIZE - 1] == id)
            return;
        
        index = index + GRID_SIZE - 1;
        for(int i = 0; i < diagonals - 1; i++) {            
            if(index < 0 || index >= GRID_SIZE * GRID_SIZE || check(originalIndex, id, index, legalMoves))
                return;
            
            index = index + GRID_SIZE - 1;
        }
    }
    
    private void checkNorthEast(int index, int id, List<Integer> legalMoves) {
        int diagonals = getDiagonals(index, 0, GRID_SIZE - 1);
        int originalIndex = index;  
                
        if(index < 0 || index >= GRID_SIZE * GRID_SIZE || diagonals == 1 || diagonals == 2 || grid[index - GRID_SIZE + 1] == id)
            return;
        
        index = index - GRID_SIZE + 1;
        for(int i = 0; i < diagonals - 1; i++) {            
            if(index < 0 || index >= GRID_SIZE * GRID_SIZE || check(originalIndex, id, index, legalMoves))
                return;
            
            index = index - GRID_SIZE + 1;
        }
    }
    
    private void checkNorthWest(int index, int id, List<Integer> legalMoves) {
        int diagonals = getDiagonals(index, GRID_SIZE - 1, GRID_SIZE - 1);
        int originalIndex = index;        
                
        if(index < 0 || index >= GRID_SIZE * GRID_SIZE || diagonals == 1 || diagonals == 2 || grid[index - GRID_SIZE - 1] == id)
            return;
                
        index = index - GRID_SIZE - 1;
        for(int i = 0; i < diagonals - 1; i++) {            
            if(index < 0 || index >= GRID_SIZE * GRID_SIZE || check(originalIndex, id, index, legalMoves))
                return;
            
            index = index - GRID_SIZE - 1;
        }
    }
    
    private void checkSouth(int index, int id, int x, int y, List<Integer> legalMoves) {
        if(y == GRID_SIZE - 1 || grid[index + GRID_SIZE] == id)
            return;
        
        for(int i = index + GRID_SIZE; i < GRID_SIZE * GRID_SIZE; i += GRID_SIZE) {
            if(check(index, id, i, legalMoves))
                return;
        }
    }
    
    private void checkNorth(int index, int id, int x, int y, List<Integer> legalMoves) {
        if(y == 0 || grid[index - GRID_SIZE] == id)
            return;
        
        for(int i = index - GRID_SIZE; i >= x; i -= GRID_SIZE) {
            if(check(index, id, i, legalMoves))
                return;
        }
    }    
    
    private void checkEast(int index, int id, int x, int y, List<Integer> legalMoves) {
        if(x == GRID_SIZE - 1 || grid[index + 1] == id)
            return;
        
        for(int i = index + 1; i < y * GRID_SIZE + GRID_SIZE; i++) {
            if(check(index, id, i, legalMoves))
                return;
        }
    }    
    
    private void checkWest(int index, int id, int x, int y, List<Integer> legalMoves) {
        if(x == 0 || grid[index - 1] == id)
            return;
        
        for(int i = index - 1; i >= y * GRID_SIZE; i--) {
            if(check(index, id, i, legalMoves))
                return;
        }
    }
    
    private boolean check(int index, int id, int i, List<Integer> legalMoves) {
        if(grid[i] == PLAYER_EMPTY)
            return true;
        if(grid[i] == id) {
            if(!legalMoves.contains(new Integer(i))) {
                legalMoves.add(index);
                return true;
            }
        }
        
        return false;
    }
    
    private int getDiagonals(int index, int originX, int originY) {
        int x = index % GRID_SIZE;
        int y = index / GRID_SIZE;
        
        int dx = Math.abs(originX - x);
        int dy = Math.abs(originY - y);
        
        int times = dx + 1;
        int value = GRID_SIZE - dx;
        return value - Math.max(0, dy - times + 1);
    }
        
    private List<Integer> getFlippedMarkers(int index, int id) {        
        List<Integer> flippedMarkers = new ArrayList();
        flippedMarkers.addAll(getFlippedDiagonalMarkers(index, id, GRID_SIZE - 1, GRID_SIZE - 1, -GRID_SIZE - 1));  // SE
        flippedMarkers.addAll(getFlippedDiagonalMarkers(index, id, GRID_SIZE - 1, 0, GRID_SIZE - 1));               // NE
        flippedMarkers.addAll(getFlippedDiagonalMarkers(index, id, 0, GRID_SIZE - 1, -GRID_SIZE + 1));              // SW
        flippedMarkers.addAll(getFlippedDiagonalMarkers(index, id, 0, 0, GRID_SIZE + 1));                           // NW
        
        flippedMarkers.addAll(getFlippedStraightMarkers(index, id, GRID_SIZE, GRID_SIZE - 1, true));
        flippedMarkers.addAll(getFlippedStraightMarkers(index, id, -GRID_SIZE, 0, true));
        flippedMarkers.addAll(getFlippedStraightMarkers(index, id, 1, GRID_SIZE - 1, false));
        flippedMarkers.addAll(getFlippedStraightMarkers(index, id, -1, 0, false));
        

                
        return flippedMarkers;
    }
    
    private List<Integer> getFlippedStraightMarkers(int index, int id, int step, int origin, boolean vertical) {
        List<Integer> flippedMarkers = new ArrayList();
        
        int dv = Math.abs(origin - (vertical ? (index / GRID_SIZE) : (index % GRID_SIZE)));
                
        index = index + step;
        for(int i = 0; i < dv; i++) {
            if(getFlippedMarkers(index, id, flippedMarkers))
                break;
            
            index = index + step;
        }
        
        if(!flippedMarkers.isEmpty() && grid[flippedMarkers.get(flippedMarkers.size() - 1)] != id || flippedMarkers.size() == 1)
            flippedMarkers.clear();
        
        if(!flippedMarkers.isEmpty())
            flippedMarkers.remove(flippedMarkers.size() - 1);
        
        return flippedMarkers;
    }
    
    private List<Integer> getFlippedDiagonalMarkers(int index, int id, int originX, int originY, int step) {
        List<Integer> flippedMarkers = new ArrayList();
        
        int diagonals = getDiagonals(index, originX, originY);
        index = index + step;
        for(int i = 0; i < diagonals - 1; i++) {
            if(getFlippedMarkers(index, id, flippedMarkers))
                break;
            index = index + step;
        }
        
        if(!flippedMarkers.isEmpty() && grid[flippedMarkers.get(flippedMarkers.size() - 1)] != id || flippedMarkers.size() == 1)
            flippedMarkers.clear();
        
        if(!flippedMarkers.isEmpty())
            flippedMarkers.remove(flippedMarkers.size() - 1);
        
        return flippedMarkers;
    }
            
    private boolean getFlippedMarkers(int i, int id, List<Integer> flippedMarkers) {
        if(grid[i] == id) {
            flippedMarkers.add(i);
            return true;
        }

        if(grid[i] == PLAYER_EMPTY)
            return true;

        flippedMarkers.add(i);
        
        return false;
    }
}
