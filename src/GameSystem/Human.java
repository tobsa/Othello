package GameSystem;

import SharedSystem.BlockQueue;

public class Human extends Player {
    
    public Human(int id, String name) {
        super(id, name);
    }
     
    @Override
    public int computeMove() throws InterruptedException {
        return BlockQueue.getInstance().take();
    }
}
