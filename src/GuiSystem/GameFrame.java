package GuiSystem;

import GameSystem.GameManager;
import NetworkSystem.NetworkManager;
import SharedSystem.IGMListener;
import SharedSystem.SharedConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class GameFrame extends JFrame implements SharedConstants, IGMListener {
    private static final String MENU_PANEL_VIEW = "MENU_PANEL_VIEW";
    private static final String GAME_PANEL_VIEW = "GAME_PANEL_VIEW";
    
    private JButton newGameButton     = new JButton("New Game");
    private JButton networkGameButton = new JButton("Network Game");
    private JButton exitGameButton    = new JButton("Exit Game");
    private JButton backButton        = new JButton("Back");
    private GameBoard gameBoard = new GameBoard();
    private NetworkManager networkManager = new NetworkManager();
    private GameManager gameManager = new GameManager(networkManager);
    private Thread gameThread = new Thread(gameManager);
    private JLabel playerTurnLabel = new JLabel("");
    private JPanel menuPanelView = new JPanel(new CardLayout());
    
    public GameFrame() {
        newGameButton.addActionListener(new ButtonNewGameListener());
        networkGameButton.addActionListener(new ButtonNetworkGameListener());
        exitGameButton.addActionListener(new ButtonExitGameListener());
        backButton.addActionListener(new ButtonBackListener());
        
        playerTurnLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        playerTurnLabel.setForeground(Color.DARK_GRAY);
        
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        menuPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 5, 10), new EtchedBorder()));
        menuPanel.add(newGameButton);
        menuPanel.add(networkGameButton);
        menuPanel.add(exitGameButton);
        
        JPanel gamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gamePanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 5, 10), new EtchedBorder()));
        gamePanel.add(backButton);
        gamePanel.add(playerTurnLabel);
        
        menuPanelView.add(menuPanel, MENU_PANEL_VIEW);
        menuPanelView.add(gamePanel, GAME_PANEL_VIEW);
        
        JPanel boardPanel = new JPanel();
        boardPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 10, 10, 10), new EtchedBorder()));
        boardPanel.add(gameBoard);
        
        add(menuPanelView, BorderLayout.NORTH);
        add(boardPanel,    BorderLayout.CENTER);
       
        pack();
    }
    
    private void initializeGame() {
        gameThread.interrupt();
        gameManager.clearGrid();
        gameManager.clearIGGListeners();
        gameManager.clearIGMListeners();
        gameManager.registerIGGListener(gameBoard);
        gameManager.registerIGMListener(this);
        networkManager.closeConnection();
    }
    
    private void executeGame() {
        gameThread = new Thread(gameManager);
        gameThread.start();
        CardLayout menuView = (CardLayout)menuPanelView.getLayout();
        menuView.show(menuPanelView, GAME_PANEL_VIEW);
    }
    
    private void executeNetworkGame(final String name, final int port) {       
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerAcceptFrame frame = new ServerAcceptFrame(GameFrame.this, networkManager);
                
                int result = networkManager.createServer(name, port);
                if(result == NetworkManager.NETWORK_CREATE_SERVER){
                    gameManager.createNetworkPlayer1(TYPE_NETWORK_SEND, networkManager.getServerName());
                    gameManager.createNetworkPlayer2(TYPE_NETWORK_READ, networkManager.getClientName());
                    executeGame();
                }
                
                frame.dispose();
            }
        }).start();
    }

    @Override
    public void updateWinner(int result, String name1, String name2) {
        new ResultDialog(this, name1, name2, result);
        playerTurnLabel.setText("");
        CardLayout menuView = (CardLayout)menuPanelView.getLayout();
        menuView.show(menuPanelView, MENU_PANEL_VIEW);
    }
    
    @Override
    public void updateTurn(String name) {
        playerTurnLabel.setText("Turn: " + name);
    }
    
    @Override
    public void updateLostConnection() {
        new MessageDialog(this, "Lost connection!");
    }

    private class ButtonNewGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {            
            SetupGameDialog dialog = new SetupGameDialog(GameFrame.this);
            
            if(dialog.getSelection() == SetupGameDialog.SELECTION_OK) {
                initializeGame();
                gameManager.createPlayer1(dialog.getPlayer1Type(), dialog.getPlayer1Name());
                gameManager.createPlayer2(dialog.getPlayer2Type(), dialog.getPlayer2Name());
                executeGame();
            }
        }   
    }
    
    private class ButtonNetworkGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {            
            SetupNetworkDialog dialog = new SetupNetworkDialog(GameFrame.this);
            if(dialog.getSelection() == SetupNetworkDialog.SELECTION_OK) {
                initializeGame();
               
                if(dialog.getPlayerType() == NETWORK_HOST) {                    
                    if(networkManager.isPortAvailable(dialog.getPort())) {
                        executeNetworkGame(dialog.getPlayerName(), dialog.getPort());
                    }
                    else 
                        new MessageDialog(GameFrame.this, "Port already in use!");
                }
                else {
                    if(networkManager.createClient(dialog.getPlayerName(), dialog.getIP(), dialog.getPort())) {
                        gameManager.createNetworkPlayer1(TYPE_NETWORK_READ, networkManager.getServerName());
                        gameManager.createNetworkPlayer2(TYPE_NETWORK_SEND, networkManager.getClientName());
                        executeGame();
                    }
                    else 
                        new MessageDialog(GameFrame.this, "Couldn't connect to server!");
                }
            }
        }
    }
    
    private class ButtonExitGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
             System.exit(0);
        }   
    }
    
    private class ButtonBackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            initializeGame();
            CardLayout menuView = (CardLayout)menuPanelView.getLayout();
            menuView.show(menuPanelView, MENU_PANEL_VIEW);
        }   
    }
}
