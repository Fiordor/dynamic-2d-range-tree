package DynamicTreeStructures;

import javax.swing.JDialog;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TreeDialog extends JDialog {
	
	public String textTree = "";
	
	public TreeDialog(Frame owner) {
		super(owner, true);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTextArea txtrWriteYourTree = new JTextArea();
		txtrWriteYourTree.setText("Write your tree here...");
		getContentPane().add(txtrWriteYourTree, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Accept");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textTree = txtrWriteYourTree.getText();
				txtrWriteYourTree.setText("Write your tree here...");
				setVisible(false);
			}
		});
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textTree = "";
				txtrWriteYourTree.setText("Write your tree here...");
				setVisible(false);
			}
		});
		panel.add(btnNewButton_1);
	}

}
