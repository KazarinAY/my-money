package kazarin.my_money.gui;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Environment;
import kazarin.my_money.model.ModelException;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.table.TableModel;
import java.math.BigDecimal;
import java.util.Date;

/**
 *  Is a class for creating a dialog window for adding a operation
 */
public class AddOperationDialog {

	private String[] result;
	private JDialog dialog;
	private JTextField textFieldSum;
	private JTextField textFieldDate;
	private JTextField textFieldDescription;
	private JTextField textFieldTags;
	private TableModel tableModel;
	
	/**
 	 * Constructs an AddOperationDialog
 	 *
 	 * @param data model
 	 */
	public AddOperationDialog(TableModel tableModel){
		this.tableModel = tableModel;
	}
	
	/**
 	 * Shows add operation dialog window
 	 */
	public void show(){
		dialog = new JDialog(FrameHolder.getFrame(), "Add operation:", true);
	
		dialog.setLayout(new GridLayout(5, 2));
		dialog.setLocation(300, 350);
		
		JLabel labelSum = new JLabel("Sum:");
		dialog.add(labelSum);
		
		textFieldSum = new JTextField(20);
		dialog.add(textFieldSum);
		
		JLabel labelDate = new JLabel("Date(dd-mm-yyyy):");
		dialog.add(labelDate);
		
		textFieldDate = new JTextField(20);
		dialog.add(textFieldDate);

		JLabel labelDescription = new JLabel("Description:");
		dialog.add(labelDescription);
		
		textFieldDescription = new JTextField(20);
		dialog.add(textFieldDescription);
		
		JLabel labelTags = new JLabel("Tags:");
		dialog.add(labelTags);
		
		textFieldTags = new JTextField(20);
		dialog.add(textFieldTags);

		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (e.getActionCommand()){
					case "OK": 
						String sum = textFieldSum.getText().trim();
						if (!sum.trim().matches("[+-]?(?:\\d+(?:\\.\\d+)?|\\.\\d+)")) {
							textFieldSum.setText("Wrong number!");
							break;
						}
						String date = textFieldDate.getText().trim();
						if(date.equals("")){
							textFieldDate.setText("Enter the date name!");
							break;
						}
						if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
							textFieldSum.setText("Wrong date!");
							break;
						}
						String description = textFieldDescription.getText().trim();
						String tags = textFieldTags.getText().trim();
						result = new String[4];
						result[0] = sum;
						result[1] = date;
						result[2] = description;
						result[3] = tags;
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

	public String[] getResultDbName() {
		return result;
	}
}
