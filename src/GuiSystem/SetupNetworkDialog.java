package GuiSystem;

import SharedSystem.SharedConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class SetupNetworkDialog extends JDialog implements SharedConstants {
    public static final int SELECTION_OK = 0;
    public static final int SELECTION_CANCEL = 1;
    private int selection;
    private JComboBox<String> typeComboBox = new JComboBox();
    private JTextField nameTextfield = new JTextField("Server", 10);
    private JTextField ipTextfield     = new JTextField("localhost", 10);
    private JTextField portTextfield   = new JTextField("8080", 10);
    
    public SetupNetworkDialog(JFrame parent) {
        setLayout(new BorderLayout());
        
        typeComboBox.addItem("Host");
        typeComboBox.addItem("Client");
        typeComboBox.addItemListener(new ComboBoxItemListener());
        
        ipTextfield.setEnabled(false);
                
        JButton button1 = new JButton("OK");
        JButton button2 = new JButton("Cancel");
        button1.addActionListener(new ButtonOKListener());
        button2.addActionListener(new ButtonCancelListener());
        
        JPanel panel1 = new JPanel(new FlowLayout());
        panel1.add(typeComboBox);
        panel1.setBorder(new TitledBorder("Type"));
        
        JPanel panel2 = new JPanel();
        panel2.add(ipTextfield);
        panel2.setBorder(new TitledBorder("IP"));
        
        JPanel panel3 = new JPanel();
        panel3.add(nameTextfield);
        panel3.setBorder(new TitledBorder("Name"));
        
        JPanel panel4 = new JPanel();
        panel4.add(portTextfield);
        panel4.setBorder(new TitledBorder("Port"));
        
        JPanel panel5 = new JPanel(new GridLayout(0, 2));
        panel5.add(panel1);
        panel5.add(panel2);
        panel5.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel panel6 = new JPanel(new GridLayout(0, 2));
        panel6.add(panel3);
        panel6.add(panel4);
        panel6.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        JPanel panel7 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel7.add(button1);
        panel7.add(button2);
        panel7.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        add(panel5, BorderLayout.NORTH);
        add(panel6, BorderLayout.CENTER);
        add(panel7, BorderLayout.SOUTH);
        
        pack();
        
        setTitle("Setup Network Game");
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setModal(true);
        setVisible(true);
    }
    
    public int getPlayerType() {
        return typeComboBox.getSelectedIndex();
    }
    
    public String getPlayerName() {
        return nameTextfield.getText();
    }
    
    public String getIP() {
        return ipTextfield.getText();
    }
    
    public int getPort() {
        return Integer.parseInt(portTextfield.getText());
    }
    
    public int getSelection() {
        return selection;
    }
    
    private class ButtonOKListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            selection = SELECTION_OK;
            dispose();
        }
    }
    
    private class ButtonCancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            selection = SELECTION_CANCEL;
            dispose();
        }
    }
    
    private class ComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if(((String)event.getItem()).equals("Host")) {
                ipTextfield.setEnabled(false);
                nameTextfield.setText("Server");
            }
            else {
                ipTextfield.setEnabled(true);
                nameTextfield.setText("Client");
            }
        }
    }
}
