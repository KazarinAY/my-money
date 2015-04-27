package kazarin.my_money.gui;

import kazarin.my_money.model.Environment;
import kazarin.my_money.model.ModelException;

import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JComboBox;

public class NewAccountingDialog extends JDialog {
		
		private String[] result;

		private JTextField textFieldUser;
		private JTextField textFieldPassword;
		private JTextField textFieldHost;
		private JTextField textFieldDBName;
		private JTextField textFieldDB;
		private JComboBox<String> dbList;
	
	public NewAccountingDialog() {
		super(FrameHolder.getFrame(), "Enter information:", true);

		setLayout(new GridLayout(6, 2));
		setLocation(300, 350);
		
		JLabel labelUser = new JLabel("User:");
		add(labelUser);
		
		textFieldUser = new JTextField(20);
		textFieldUser.setText(System.getProperty("user.name"));
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
		
		JLabel labelDB = new JLabel("DB (HSQL only):");
		add(labelDB);
		String[] dbStrings = {"HSQL"};
		dbList = new JComboBox<String>(dbStrings);
		dbList.setSelectedIndex(0);
		dbList.setEditable(false);
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
						String db = (String) dbList.getSelectedItem();
						if (db.equals("")) break;
						result = new String[2];
						result[0] = dbName;
						
						try {
							Environment env = Environment.getInstance();
							env.createNewAccounting(user, password,
															host, dbName, db);							
							result[1] = "OK";						
						} catch (ModelException me) {
							String message = me.getMessage();
							GuiLogger.warning("env.createNewAccounting\n" + message);
							result[1] = message;
						}

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