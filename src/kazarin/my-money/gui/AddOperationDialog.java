package kazarin.my_money.gui;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.WrongCommandException;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.Date;

/**
 *  Is a class for creating a dialog window for adding a operation
 */
public class AddOperationDialog {
	private JFrame frame;
	private JDialog dialog;
	private JTextField textFieldHowMuch;
	private JTextField textFieldDate;
	private JTextField textFieldDescription;
	private JTextField textFieldTags;
	private TableModel tableModel;
	
	/**
 	 * Constructs an AddOperationDialog
 	 *
 	 * @param frame
 	 * @param data model
 	 */
	public AddOperationDialog(JFrame frame, TableModel tableModel){
		this.frame = frame;	
		this.tableModel = tableModel;
	}
	
	/**
 	 * Shows add operation dialog window
 	 */
	public void show(){
		dialog = new JDialog(frame, "Add operation:", true);
	
		dialog.setLayout(new GridLayout(5, 2));
		dialog.setLocation(300, 350);
		
		JLabel labelHowMuch = new JLabel("How much:");
		dialog.add(labelHowMuch);
		
		textFieldHowMuch = new JTextField(20);
		dialog.add(textFieldHowMuch);
		
		JLabel labelDate = new JLabel("Date:");
		dialog.add(labelDate);
		
		textFieldDate = new JTextField(20);
		dialog.add(textFieldDate);

		JLabel labelDescription = new JLabel("Description:");
		dialog.add(labelDescription);
		
		textFieldDescription = new JTextField(20);
		dialog.add(textFieldDescription);
		
		JLabel labelTags = new JLabel("Date:");
		dialog.add(labelTags);
		
		textFieldTags = new JTextField(20);
		dialog.add(textFieldTags);

		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (e.getActionCommand()){
					case "OK": 
						String howMuch = textFieldHowMuch.getText();
						if (!howMuch.trim().matches("[+-]?(?:\\d+(?:\\.\\d+)?|\\.\\d+)")) {
							textFieldHowMuch.setText("Wrong number!");
							break;
						}
						String date = textFieldDate.getText();
						if(date.equals("")){
							textFieldDate.setText("Enter the date name!");
							break;
						}
						if (!date.trim().matches("\\d{2}-\\d{2}-\\d{4}")) {
							textFieldHowMuch.setText("Wrong date!");
							break;
						}
						String description = textFieldDescription.getText();
						String tags = textFieldTags.getText();						
						
						String command = String.format("add %s:%s:%s#%s",
														howMuch, date, description,tags);		
						
						Operations operations = Operations.getInstance();
						try {
							operations.add(command);
						} catch (WrongCommandException wce) {
							textFieldHowMuch.setText("Wrong command!");
							break;
						}
						
						dialog.dispose();
						
						break;
					case "Cancel":
						dialog.dispose();
						break;
				}
			}
		};
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(actionListener);
		dialog.add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(actionListener);
		dialog.add(cancelButton);
		
		dialog.pack();
		dialog.setVisible(true);	
	}	
	
}