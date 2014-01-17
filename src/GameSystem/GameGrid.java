package GameSystem;

import SharedSystem.IGGListener;
import SharedSystem.SharedConstants;
import static SharedSystem.SharedConstants.GRID_SIZE;
import java.util.ArrayList;
import java.util.List;

public class GameGrid implements SharedConstants {
    private int grid[] = new int[GRID_SIZE * GRID_SIZE];
    private List<IGGListener> listeners = new ArrayList();
    
    public boolean setID(int index, int id) {
        if(index < 0 || index >= grid.length || grid[index] != PLAYER_EMPTY)
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
    
    public void replaceID(int index, int id) {
        grid[index] = id;
    }
    
    public int getID(int index) {
        return grid[index];
    }
    
    public int getGridSize() {
        return grid.length;
    }
        
    public void clear() {
        for(int i = 0; i < grid.length; i++)
            grid[i] = PLAYER_EMPTY;
        
        /*grid[0] = 1;
        grid[1] = 2;
        
        grid[7] = 1;
        grid[6] = 2;
        grid[15] = 2;
        grid[23] = 2;
        
        grid[19] = 2;
        grid[26] = 2;
        grid[27] = 1;
        grid[10] = 2;
        grid[11] = 2;
        
        grid[0] = 1;
        grid[9] = 2;
        grid[7] = 1;
        grid[14] = 2;
        grid[56] = 1;
        grid[63] = 1;
        grid[54] = 2;
        grid[49] = 2;
        
        grid[49] = 2;
        grid[42] = 1;*/
                 
        for(int i = 0; i < grid.length; i++) {
            for(IGGListener listener : listeners)
                listener.updateMove(i, grid[i], null);
        }
    }
           
   public int getResult() {
       return RESULT_NO_OUTCOME;
   }
    
    public void registerListener(IGGListener listener) {
        listeners.add(listener);
    }
    
    public void clearListeners() {
        listeners.clear();
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
