package kazarin.my_money.gui;

import kazarin.my_money.model.Env;

import javax.swing.JFrame;
import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JComboBox;

public class AccountingDialog extends JDialog {

		private String[] result;
		private boolean isNew;
		private String[] dbStrings;
		private JTextField textFieldUser;
		private JTextField textFieldPassword;
		private JTextField textFieldHost;
		private JTextField textFieldDBName;
		private JTextField textFieldDB;
		private JComboBox<String> dbList;
	
	public AccountingDialog(JFrame frame, boolean isNew) {
		super(frame, "Enter information:", true);

		this.isNew =isNew;

		setLayout(new GridLayout(6, 2));
		setLocation(300, 350);
		
		JLabel labelUser = new JLabel("User:");			
		textFieldUser = new JTextField(20);
		textFieldUser.setText(System.getProperty("user.name"));		
		JLabel labelPassword = new JLabel("Password:");		
		textFieldPassword = new JTextField(20);		
		JLabel labelHost = new JLabel("Host:");		
		textFieldHost = new JTextField(20);
		JLabel labelDBName = new JLabel("DB name:");
		textFieldDBName = new JTextField(20);
		JLabel labelDB = new JLabel("DB:");		

		textFieldHost.setText(Env.LOCALHOST);
		dbStrings = new String[]{"MySQL", "HSQL"};
		dbList = new JComboBox<String>(dbStrings);
		dbList.setSelectedIndex(0);
		dbList.setEditable(false);
		dbList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
        		String textInField = (String) cb.getSelectedItem();
        		if (textInField.equals("MySQL")) {
        			textFieldHost.setText(Env.LOCALHOST);
        		} else if (textInField.equals("HSQL")) {
        			textFieldHost.setText(Env.PROGRAMM_DATA_PATH);
        		}
			}
		});
		
		add(labelUser);
		add(textFieldUser);
		add(labelPassword);
		add(textFieldPassword);
		add(labelHost);
		add(textFieldHost);
		add(labelDBName);
		add(textFieldDBName);
		add(labelDB);
		add(dbList);
		/*
		textFieldDB = new JTextField(20);
		textFieldDB.setText("MySQL");
		add(textFieldDB);*/
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (e.getActionCommand()){
					case "OK": 
						String user = textFieldUser.getText();
						if (user.equals("")) break;
						String password = textFieldPassword.getText();
						
						String host = textFieldHost.getText();
						if (host.equals("")) break;
						String dbName = textFieldDBName.getText();
						if (dbName.equals("")) break;
						String dbType = (String) dbList.getSelectedItem();
						if (dbType.equals("")) break;
						result = new String[5];
						result[0] = user;
						result[1] = password;
						result[2] = host;
						result[3] = dbName;
						result[4] = dbType;
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

	public String[] getResultDbName() {
		return result;
	}
}