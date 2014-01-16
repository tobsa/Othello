package GuiSystem;

import NetworkSystem.NetworkManager;
import SharedSystem.SharedConstants;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ServerAcceptFrame extends JFrame implements SharedConstants {
    private NetworkManager networkManager;
        
    public ServerAcceptFrame(JFrame parent, NetworkManager networkManager) {
        this.networkManager = networkManager;
        
        setLayout(new BorderLayout());
        
        JButton button = new JButton("Abort");
        button.addActionListener(new ButtonAbortListener());
        
        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Waiting for a client to connect..."));
        panel1.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel panel2 = new JPanel();
        panel2.add(button);
        panel2.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        
        pack();
        
        setTitle("Waiting for clients...");
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    
    private class ButtonAbortListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            networkManager.closeConnection();
            dispose();
        }
    }
}
