package GuiSystem;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MessageDialog extends JDialog {
    
    public MessageDialog(JFrame parent, String message) {
        setLayout(new BorderLayout());
        
        JButton button = new JButton("OK");
        button.addActionListener(new ButtonOKListener());
        
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel1.add(new JLabel(message));
        panel1.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel2.add(button);
        panel2.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        
        pack();
        
        setTitle("Message");
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setVisible(true);
    }
    
    private class ButtonOKListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            dispose();
        }
    }
}
