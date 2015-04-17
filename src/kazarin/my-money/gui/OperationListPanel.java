/*
 * OperationListPanel
 */
package kazarin.my_money.gui;
import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Operation;

import javax.swing.JPanel;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Is a panel with table of groups.
 */
public class OperationListPanel extends JPanel implements ActionListener{

	private JFrame frame;
	private JTable table;
	private TableModel groupTableModel;	
	private List<Operation> dataList;
	
	/**	
     * Constructs a OperationListPanel.
	 *
	 * @param frame is passed to the modal dialogs
	 */
	public OperationListPanel(JFrame frame){
		super();

		this.frame = frame;

		this.dataList = Operations.getInstance().getList();
		
		setLayout(new BorderLayout());
		
		table = new JTable();
		
		TableModel tableModel = new AbstractTableModel(){
	
			private String[] columnNames = {"How Much",
								            "Date",
								            "Description",
								            "Tags"
								            };		
			
			
			/**
		 	 * Gets the size of groups list
		 	 */
			public int getDataListSize(){
				return dataList.size();
			}
			
			@Override
			public int getColumnCount() { 
				return columnNames.length; 
			}
			
			@Override
			public int getRowCount() {
				return dataList.size();
			}
			
			@Override
			public Object getValueAt(int row, int col) {
				Operation operation = dataList.get(row);
				switch(col){
					case 0: return operation.getHowMuch();
					case 1: return operation.getDate();
					case 2: return operation.getDescription();
					case 3: return operation.getTags();
					default: return "";
				}		
			}
			
			@Override
			public String getColumnName(int column) {return columnNames[column];}
			
			@Override
			public boolean isCellEditable(int row, int col) {return false;}
			
			@Override
			public void setValueAt(Object aValue, int row, int col) {
				if(dataList.size() < row){ 								//add new row
					Operation operation = new Operation();
					operation.setHowMuch((BigDecimal)aValue);
					dataList.add(operation);			
				}else {													//change row
					Operation operation = dataList.get(row);
					switch(col){
						case 0: operation.setHowMuch((BigDecimal)aValue);
								break;
						case 1: operation.setDate((Date)aValue);
								break;
						case 2: operation.setDescription((String)aValue);
								break;
						case 3: operation.setTags((String[])aValue);
								break;	
					}
		        	fireTableDataChanged();		
				}				
			}
			
			/**
			 * Deletes row
			 *
			 * @param row to delete
			 */
			public void deleteRow(int row){
				dataList.remove(row);
				fireTableRowsDeleted(row, row);
				
			}
			
			@Override
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}			
		}; //TableModel tableModel = new AbstractTableModel(){

		table.setModel(tableModel);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		/* Сustom column width: */
		TableColumn column = null;
		column = table.getColumnModel().getColumn(0);
    	column.setPreferredWidth(200); 
    	column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(100);
        column = table.getColumnModel().getColumn(2);
    	column.setPreferredWidth(200); 
    	column = table.getColumnModel().getColumn(3);
        column.setPreferredWidth(500);

		JScrollPane scrollPane = new JScrollPane(table);

		table.setPreferredScrollableViewportSize(new Dimension(600, 250));
		table.setFillsViewportHeight(true);
		
		add("North", scrollPane);
				
		JPanel southPanel = new JPanel();
		
		JButton addButton = new JButton("Add");		
		addButton.addActionListener(this);
		southPanel.add(addButton);
       
		JButton changeButton = new JButton("Change");
		changeButton.addActionListener(this);
        southPanel.add(changeButton);
		
		JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        southPanel.add(deleteButton);
		
		add("South", southPanel); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int selectedRow;
		/*
		switch(e.getActionCommand()){
			case "Add": 
				AddGroupDialog addGroupDialog = new AddGroupDialog(frame, groupTableModel);
				addGroupDialog.show();
				table.repaint();
				break;
			case "Change": 
				selectedRow = table.getSelectedRow();
				if(selectedRow == -1){break;}	//Do nothing if don't selected any row
				Group groupToChange = new Group();		
				groupToChange.setNumber((Integer)table.getValueAt(selectedRow, 0));
				groupToChange.setFaculty((String)table.getValueAt(selectedRow, 1));	
				ChangeGroupDialog changeGroupDialog	= new ChangeGroupDialog(frame, groupTableModel, selectedRow, groupToChange);
				changeGroupDialog.show();	
				table.repaint();
				break;
			case "Delete": 				
				selectedRow = table.getSelectedRow();
				if(selectedRow == -1){break;}	//Do nothing if don't selected any row				
				Group groupToDelete = new Group();
				int groupNumber = (Integer)table.getValueAt(selectedRow, 0);		
				groupToDelete.setNumber(groupNumber);
				groupToDelete.setFaculty((String)table.getValueAt(selectedRow, 1));
				StudentDao studentDao = new StudentDao();
				long groupId = studentDao.getGroupIdForNumber(Integer.toString(groupNumber));
				groupToDelete.setId(groupId);
				DeleteGroupDialog deleteGroupDialog = new DeleteGroupDialog(frame, groupTableModel, selectedRow, groupToDelete);
				deleteGroupDialog.show();	
				table.repaint();
				break;	
			default:
				break;
		}
		*/
	}
}
