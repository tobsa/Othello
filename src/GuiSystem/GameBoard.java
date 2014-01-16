package GuiSystem;

import SharedSystem.BlockQueue;
import SharedSystem.IGGListener;
import SharedSystem.SharedConstants;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameBoard extends JPanel implements SharedConstants, IGGListener {
    private JLabel grid[] = new JLabel[GRID_SIZE * GRID_SIZE];
    private ImageIcon emptyIcon  = new ImageIcon("images/empty.png");
    private ImageIcon blackIcon  = new ImageIcon("images/black.png");
    private ImageIcon whiteIcon = new ImageIcon("images/white.png");
    
    public GameBoard() {
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        addMouseListener(new MousePressedListener());
        
        for(int i = 0; i < grid.length; i++) {
            JLabel label = new JLabel(emptyIcon);
            grid[i] = label;
            add(label);
        }
    }

    @Override
    public void updateMove(int index, int id) {
        switch(id) {
            case PLAYER_EMPTY:
                grid[index].setIcon(emptyIcon);
                break;
            case PLAYER_1:
                grid[index].setIcon(blackIcon);
                break;
            case PLAYER_2:
                grid[index].setIcon(whiteIcon);
                break;
        }
    }
    
    private class MousePressedListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            int x = event.getX() / IMAGE_SIZE;
            int y = event.getY() / IMAGE_SIZE;
              
            BlockQueue.getInstance().add(x + y * GRID_SIZE);
        }
    }
}

