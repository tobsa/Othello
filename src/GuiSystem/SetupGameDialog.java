package GuiSystem;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class SetupGameDialog extends JDialog {
    public static final int SELECTION_OK = 0;
    public static final int SELECTION_CANCEL = 1;
    private int selection;
    private JTextField player1NameTextfield       = new JTextField("Human", 10);
    private JTextField player2NameTextfield       = new JTextField("Computer", 10);
    private JComboBox<String> player1TypeComboBox = new JComboBox();
    private JComboBox<String> player2TypeComboBox = new JComboBox();
    private JButton okButton     = new JButton("OK");
    private JButton cancelButton = new JButton("CANCEL");    
    
    public SetupGameDialog(JFrame parent) {
        setLayout(new BorderLayout());
        
        player1TypeComboBox.addItem("Human");
        player1TypeComboBox.addItem("Easy Computer");
        player1TypeComboBox.addItem("Normal Computer");
        player1TypeComboBox.addItem("Hard Computer");
        player2TypeComboBox.addItem("Human");
        player2TypeComboBox.addItem("Easy Computer");
        player2TypeComboBox.addItem("Normal Computer");
        player2TypeComboBox.addItem("Hard Computer");
        player2TypeComboBox.setSelectedIndex(2);
        
        okButton.addActionListener(new ButtonOKListener());
        cancelButton.addActionListener(new ButtonCancelListener());
        
        JPanel player1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        player1Panel.add(player1NameTextfield);
        player1Panel.add(player1TypeComboBox);
        player1Panel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Player 1")));
        
        JPanel player2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        player2Panel.add(player2NameTextfield);
        player2Panel.add(player2TypeComboBox);
        player2Panel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 10, 10, 10), new TitledBorder("Player 2")));
        
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectionPanel.add(okButton);
        selectionPanel.add(cancelButton);
        selectionPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        add(player1Panel,   BorderLayout.NORTH);
        add(player2Panel,   BorderLayout.CENTER);
        add(selectionPanel, BorderLayout.SOUTH);
        
        pack();
        
        setTitle("Setup Game");
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setModal(true);
        setVisible(true);
    }
    
    public int getSelection() {
        return selection;
    }
    
    public String getPlayer1Name() {
        return player1NameTextfield.getText();
    }
    
    public String getPlayer2Name() {
        return player2NameTextfield.getText();
    }
    
    public int getPlayer1Type() {
        return player1TypeComboBox.getSelectedIndex();
    }
    
    public int getPlayer2Type() {
        return player2TypeComboBox.getSelectedIndex();
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
}
