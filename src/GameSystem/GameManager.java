package GameSystem;

import NetworkSystem.NetworkManager;
import NetworkSystem.NetworkReadPlayer;
import NetworkSystem.NetworkSendPlayer;
import SharedSystem.BlockQueue;
import SharedSystem.IGGListener;
import SharedSystem.IGMListener;
import SharedSystem.SharedConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameManager implements SharedConstants, Runnable {
    private Player player1;
    private Player player2;
    private GameGrid gameGrid = new GameGrid();
    private List<IGMListener> listeners = new ArrayList();
    private NetworkManager networkManager;
        
    public GameManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }
    
    public void createPlayer1(int type, String name) {
        switch(type) {
            case TYPE_HUMAN:
                player1 = new Human(PLAYER_1, name);
                break;
            case TYPE_COMPUTER_RANDOMIZER:
                player1 = new ComputerRandomizer(PLAYER_1, name, gameGrid);
                break;
            case TYPE_COMPUTER_GREEDY:
                player1 = new ComputerGreedy(PLAYER_1, name, gameGrid);
                break;
            case TYPE_COMPUTER_HEURISTIC:
                player1 = new ComputerHeuristic(PLAYER_1, name, gameGrid);
                break;
        }
    }
    
    public void createPlayer2(int type, String name) {
        switch(type) {
            case TYPE_HUMAN:
                player2 = new Human(PLAYER_2, name);
                break;
            case TYPE_COMPUTER_RANDOMIZER:
                player2 = new ComputerRandomizer(PLAYER_2, name, gameGrid);
                break;
            case TYPE_COMPUTER_GREEDY:
                player2 = new ComputerGreedy(PLAYER_2, name, gameGrid);
                break;
            case TYPE_COMPUTER_HEURISTIC:
                player2 = new ComputerHeuristic(PLAYER_2, name, gameGrid);
                break;
        }
    }
    
    public void createNetworkPlayer1(int type, String name) {
        switch(type) {
            case TYPE_NETWORK_SEND:
                player1 = new NetworkSendPlayer(PLAYER_1, name, networkManager);
                break;
            case TYPE_NETWORK_READ:
                player1 = new NetworkReadPlayer(PLAYER_1, name, networkManager);
                break;
        }
    }
    
    public void createNetworkPlayer2(int type, String name) {
        switch(type) {
            case TYPE_NETWORK_SEND:
                player2 = new NetworkSendPlayer(PLAYER_2, name, networkManager);
                break;
            case TYPE_NETWORK_READ:
                player2 = new NetworkReadPlayer(PLAYER_2, name, networkManager);
                break;
        }
    }
        
    public void clearGrid() {
        gameGrid.clear();
    }
    
    public void registerIGGListener(IGGListener listener) {
        gameGrid.registerListener(listener);
    }
    
    public void registerIGMListener(IGMListener listener) {
        listeners.add(listener);
    }
    
    public void clearIGGListeners() {
        gameGrid.clearListeners();
    }
    
    public void clearIGMListeners() {
        listeners.clear();
    }
    
    private boolean makeMove(Player player) throws InterruptedException, IOException {        
        while(true) {
            BlockQueue.getInstance().clear();
            
            boolean enablePass = gameGrid.getLegalMoves(player.getID()).isEmpty();
            for(IGMListener listener : listeners) {
                listener.updateTurn(player.getName());
                listener.updatePassButton(enablePass);
            }
          
            if(!gameGrid.setID(player.computeMove(), player.getID()))
                continue;
            
            int result = gameGrid.getResult();
            for(IGMListener listener : listeners)
                listener.updateScore(player1.getName(), gameGrid.getBlackBricks(), player2.getName(), gameGrid.getWhiteBricks()); 
            
            if(result == RESULT_PLAYER_1_WON || result == RESULT_PLAYER_2_WON || result == RESULT_DRAW) {
                for(IGMListener listener : listeners) {
                    listener.updateScore(player1.getName(), gameGrid.getBlackBricks(), player2.getName(), gameGrid.getWhiteBricks()); 
                    listener.updateWinner(result, player1.getName(), player2.getName());
                }

                return true;
            }
            
            return false;
        }
    }

    @Override
    public void run() {
        for(IGMListener listener : listeners)
                listener.updateScore(player1.getName(), gameGrid.getBlackBricks(), player2.getName(), gameGrid.getWhiteBricks());
        
        while(true) {
            try {
                if(makeMove(player1))
                    break;
                if(makeMove(player2))
                    break;
            } catch (InterruptedException ex) {
                break;
            } catch (IOException ex) {
                for(IGMListener listener : listeners)
                    listener.updateLostConnection();
                
                break;
            }
        }
    }
}
