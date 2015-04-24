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
		private JTextField textFieldHost;
		private JTextField textFieldDBName;
		private JTextField textFieldDB;
	public PrepareDialog(JFrame frame) {
		super(frame, "Enter information:", true);
		setLayout(new GridLayout(6, 2));
		setLocation(300, 350);
		
		JLabel labelUser = new JLabel("User:");
		add(labelUser);
		
		textFieldUser = new JTextField(20);
		add(textFieldUser);
		
		JLabel labelPassword = new JLabel("Password:");
		add(labelPassword);
		
		textFieldPassword = new JTextField(20);
		add(textFieldPassword);

		JLabel labelHost = new JLabel("Host:");
		add(labelHost);
		
		textFieldHost = new JTextField(20);
		textFieldHost.setText("localhost");
		add(textFieldHost);

		JLabel labelDBName = new JLabel("DB name:");
		add(labelDBName);
		
		textFieldDBName = new JTextField(20);
		add(textFieldDBName);
		
		JLabel labelDB = new JLabel("DB (MySQL or HSQL):");
		add(labelDB);
		
		textFieldDB = new JTextField(20);
		textFieldDB.setText("MySQL");
		add(textFieldDB);
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (e.getActionCommand()){
					case "OK": 
						String user = textFieldUser.getText();
						
						String password = textFieldPassword.getText();
						
						String host = textFieldHost.getText();

						String dbName = textFieldDBName.getText();
						
						String db = textFieldDB.getText();
						
						Environment env = Environment.getInstance();
						env.prepare(user, password, host, dbName, db);
						
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