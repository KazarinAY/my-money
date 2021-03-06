/*
 * ChangeOperationDialog
 */
package kazarin.my_money.gui;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Operation;
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
 * Is a class for creating a dialog window for changing a group
 */
public class ChangeOperationDialog {
	private JDialog dialog;
	private JTextField textFieldSum;
	private JTextField textFieldDate;
	private JTextField textFieldDescription;
	private JTextField textFieldTags;
	private TableModel tableModel;
	private int row;
	private Operation oldOperation;
	
	/**
 	 * Constructs an ChangeOperationDialog
 	 *
 	 * @param operation data model
 	 * @param row to change
 	 * @param operation to change
 	 */
	public ChangeOperationDialog(TableModel tableModel, 
											int row, Operation oldOperation) {
		this.tableModel = tableModel;
		this.row = row;
		this.oldOperation = oldOperation;
	}
	
	/**
 	 * Shows change operation dialog window
 	 */
	public void show(){
		dialog = new JDialog(FrameHolder.getFrame(), "Change operation:", true);
		dialog.setLayout(new GridLayout(5, 2));
		dialog.setLocation(300, 350);
		
		JLabel labelSum = new JLabel("Sum:");
		dialog.add(labelSum);
		
		textFieldSum = new JTextField(20);
		textFieldSum.setText(String.valueOf(oldOperation.getSum()));		
		dialog.add(textFieldSum);
		
		JLabel labelDate = new JLabel("Date:");
		dialog.add(labelDate);
		
		textFieldDate = new JTextField(20);
		textFieldDate.setText(oldOperation.getDateStr());
		dialog.add(textFieldDate);

		JLabel labelDescription = new JLabel("Description:");
		dialog.add(labelDescription);
		
		textFieldDescription = new JTextField(20);
		textFieldDescription.setText(oldOperation.getDescription());
		dialog.add(textFieldDescription);

		JLabel labelTags = new JLabel("Tags:");
		dialog.add(labelTags);
		
		textFieldTags = new JTextField(20);
		textFieldTags.setText(oldOperation.getTagsStr());
		dialog.add(textFieldTags);

		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(e.getActionCommand()){
					case "OK": 
						String howMuch = textFieldSum.getText();
						if (!howMuch.trim().matches("[+-]?(?:\\d+(?:\\.\\d+)?|\\.\\d+)")) {
							textFieldSum.setText("Wrong number!");
							break;
						}
						String date = textFieldDate.getText();
						if(date.equals("")){
							textFieldDate.setText("Enter the date!");
							break;
						}
						if (!date.trim().matches("\\d{2}-\\d{2}-\\d{4}")) {
							textFieldSum.setText("Wrong date!");
							break;
						}
						String description = textFieldDescription.getText();
						String tags = textFieldTags.getText();						
						
						String command = String.format("change %d:%s:%s:%s#%s",
													oldOperation.getId(), howMuch,
													date, description, tags);
						
						GuiLogger.info("Command: " + command);
						/*
						Environment env = Environment.getInstance();
						AccountingPanel ap = AccountingPanel.getInstance();
						Operations operations = env.getOperationsByName(ap.getCurrentAccounting());
						try {
							operations.change(command);
						} catch (ModelException wce) {
							textFieldSum.setText("Wrong command!");
							break;
						}
						*/				
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
