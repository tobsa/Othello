package GameSystem;

import SharedSystem.IGGListener;
import SharedSystem.SharedConstants;
import java.util.ArrayList;
import java.util.List;

public class GameGrid implements SharedConstants {
    private int grid[] = new int[GRID_SIZE * GRID_SIZE];
    private List<IGGListener> listeners = new ArrayList();
    
    public boolean setID(int index, int id) {
        
        
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
        for(int i = 0; i < grid.length; i++) {
            grid[i] = PLAYER_EMPTY;
            
            for(IGGListener listener : listeners)
                listener.updateMove(i, PLAYER_EMPTY);
        }
    }
           
   public int getResult() {
       return 0;
   }
    
    public void registerListener(IGGListener listener) {
        listeners.add(listener);
    }
    
    public void clearListeners() {
        listeners.clear();
    }
}
