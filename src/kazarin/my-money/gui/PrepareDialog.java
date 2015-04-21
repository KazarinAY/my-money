/*
 * PrepareDialog
 */
package kazarin.my_money.gui;

import kazarin.my_money.model.Environment;

import javax.swing.JFrame;
import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;

public class PrepareDialog extends JDialog {
		private JTextField textFieldUser;
		private JTextField textFieldPassword;
		private JTextField textFieldUrl;
		private JTextField textFieldDriver;
	public PrepareDialog(JFrame frame) {
		super(frame, "Enter information:", true);
		setLayout(new GridLayout(5, 2));
		setLocation(300, 350);
		
		JLabel labelUser = new JLabel("User:");
		add(labelUser);
		
		textFieldUser = new JTextField(20);
		add(textFieldUser);
		
		JLabel labelPassword = new JLabel("Password:");
		add(labelPassword);
		
		textFieldPassword = new JTextField(20);
		add(textFieldPassword);

		JLabel labelUrl = new JLabel("Url:");
		add(labelUrl);
		
		textFieldUrl = new JTextField(20);
		add(textFieldUrl);
		
		JLabel labelDriver = new JLabel("Driver:");
		add(labelDriver);
		
		textFieldDriver = new JTextField(20);
		add(textFieldDriver);
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (e.getActionCommand()){
					case "OK": 
						String user = textFieldUser.getText();
						
						String password = textFieldPassword.getText();
						
						String url = textFieldUrl.getText();
						
						String driver = textFieldDriver.getText();
						
						Environment env = Environment.getInstance();
						env.prepare(user, password, url, driver);
						
						dispose();
						
						break;
					case "Cancel":
						dispose();
						break;
				}
			}
		};
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(actionListener);
		add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(actionListener);
		add(cancelButton);
		
		pack();
		setVisible(true);
	}
}