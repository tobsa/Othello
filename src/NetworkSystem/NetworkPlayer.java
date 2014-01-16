package NetworkSystem;

import GameSystem.Player;

public abstract class NetworkPlayer extends Player {
    protected NetworkManager manager;
    
    public NetworkPlayer(int id, String name, NetworkManager manager) {
        super(id, name);
        this.manager = manager;
    }
}
