/*
 * DeleteOperationDialog
 */
package kazarin.my_money.gui;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Operation;
import kazarin.my_money.model.Environment;
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

/**
 * Is a class for creating a dialog window for deleting a operation
 */
public class DeleteOperationDialog {
	private JDialog dialog;
	private TableModel tableModel;
	private int row;
	private Operation operation;
	
	/**
 	 * Constructs an DeleteOperationDialog
 	 *
 	 * @param operation data model
 	 */
	public DeleteOperationDialog(TableModel tableModel, int row, Operation operation){
		this.tableModel = tableModel;
		this.row = row;
		this.operation = operation;
	}
	
	/**
 	 * Shows delete operation dialog window
 	 */
	public void show(){
		dialog = new JDialog(MainScreen.frame, "Delete operation:", true);
		dialog.setLayout(new GridLayout(5, 2));
		dialog.setLocation(300, 350);
		
		JLabel labelSum = new JLabel("Sum: " + operation.getSum());
		dialog.add(labelSum);
		
		JLabel labelBlank = new JLabel("");
		dialog.add(labelBlank);
		
		JLabel labelDate = new JLabel("Date: " + operation.getDateStr());
		dialog.add(labelDate);
		
		JLabel labelBlank2 = new JLabel("");
		dialog.add(labelBlank2);

		JLabel labelDescription = new JLabel("Description: " + operation.getDescription());
		dialog.add(labelDescription);
		
		JLabel labelBlank3 = new JLabel("");
		dialog.add(labelBlank3);

		JLabel labelTags = new JLabel("Tags: " + operation.getTagsStr());
		dialog.add(labelTags);
		
		JLabel labelBlank4 = new JLabel("");
		dialog.add(labelBlank4);

		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(e.getActionCommand()){
					case "OK": 
						String command = String.format("delete %d",
															operation.getId());

//LOG						System.out.println("Command: " + command);
						
						Environment env = Environment.getInstance();
						AccountingPanel ap = AccountingPanel.getInstance();
						Operations operations = env.getOperationsByName(ap.getCurrentAccounting());
						try {
							operations.delete(command);
						} catch (WrongCommandException wce) {
							System.err.println("ERROR: failed to delete!");
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
