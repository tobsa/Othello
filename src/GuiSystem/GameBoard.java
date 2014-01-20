package GuiSystem;

import SharedSystem.BlockQueue;
import SharedSystem.IGGListener;
import SharedSystem.SharedConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameBoard extends JPanel implements SharedConstants, IGGListener {
    private JLabel grid[] = new JLabel[GRID_SIZE * GRID_SIZE];
    private ImageIcon emptyIcon = new ImageIcon("images/empty.png");
    private ImageIcon blackIcon = new ImageIcon("images/black.png");
    private ImageIcon whiteIcon = new ImageIcon("images/white.png");
    
    public GameBoard() {        
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        addMouseListener(new MousePressedListener());
        
        for(int i = 0; i < grid.length; i++) {
            JLabel label = new JLabel(emptyIcon);
            label.setText("" + i);
            label.setFont(new Font("Arial", Font.PLAIN, 20));
            label.setForeground(Color.RED);
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.CENTER);
            grid[i] = label;
            add(label);
        }
    }

    @Override
    public void updateMove(int index, int id, List<Integer> flippedMarkers) {
        switch(id) {
            case PLAYER_EMPTY:
                updateMove(index, flippedMarkers, emptyIcon);
                break;
            case PLAYER_1:
                updateMove(index, flippedMarkers, blackIcon);
                break;
            case PLAYER_2:
                updateMove(index, flippedMarkers, whiteIcon);
                break;
        }
    }
    
    private void updateMove(int index, List<Integer> flippedMarkers, ImageIcon icon) {
        grid[index].setIcon(icon);
        
        if(flippedMarkers != null) {
            for(int i : flippedMarkers)
                grid[i].setIcon(icon);
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

