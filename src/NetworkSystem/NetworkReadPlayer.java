package NetworkSystem;

import java.io.IOException;

public class NetworkReadPlayer extends NetworkPlayer {
    
    public NetworkReadPlayer(int id, String name, NetworkManager manager) {
        super(id, name, manager);
    }

    @Override
    public int computeMove() throws InterruptedException, IOException {
        return manager.readMove();
    }
}
