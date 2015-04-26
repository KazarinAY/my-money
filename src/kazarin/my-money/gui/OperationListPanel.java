package kazarin.my_money.gui;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Operation;
import kazarin.my_money.model.Environment;

import javax.swing.JPanel;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent;
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
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Is a panel with table of groups.
 */
public class OperationListPanel extends JPanel implements ActionListener{
	private static OperationListPanel instance;
	
	private JTable table;
	TableModel tableModel;
	private Operations operations;
	public List<Operation> dataList;
	private JScrollPane scrollPane;
	
	/**	
     * Constructs a OperationListPanel.
	 *
	 */
	private OperationListPanel() {
		super();
		Environment env = Environment.getInstance();
		if (env.isReady()){
			AccountingPanel ap = AccountingPanel.getInstance();		
			String dbName = ap.getCurrentAccounting();
			operations = env.getOperationsByName(dbName);
			dataList = operations.getList();
		} else {
			dataList = new ArrayList<Operation>();
		}		
		
		setLayout(new BorderLayout());
		
		table = new JTable();
		
		tableModel = new AbstractTableModel() {
	
			private String[] columnNames = {"How Much",
								            "Date",
								            "Description",
								            "Tags",
								            "Id"
								            };		
			
			
			/**
		 	 * Gets the size of groups list
		 	 */
			public int getDataListSize() {
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
					case 0: return operation.getSum();
					case 1: return operation.getDate();
					case 2: return operation.getDescription();
					case 3: return operation.getTagsStr();
					case 4: return operation.getId();
					default: return "";
				}		
			}
			
			@Override
			public String getColumnName(int column) {return columnNames[column];}
			
			@Override
			public boolean isCellEditable(int row, int col) {return false;}
			
			@Override
			public void setValueAt(Object aValue, int row, int col) {
				if(dataList.size() < row) { 								//add new row
					Operation operation = new Operation();
					operation.setSum((BigDecimal)aValue);
					dataList.add(operation);			
				} else {													//change row
					Operation operation = dataList.get(row);
					switch(col){
						case 0: operation.setSum((BigDecimal)aValue);
								break;
						case 1: operation.setDate((Date)aValue);
								break;
						case 2: operation.setDescription((String)aValue);
								break;
						case 3: operation.setTags((String[])aValue);
								break;
						case 4: operation.setId((Integer)aValue);
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
			public void deleteRow(int row) {
				dataList.remove(row);
				fireTableRowsDeleted(row, row);
				
			}
			
			@Override
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}			
		}; //TableModel tableModel = new AbstractTableModel() {

		table.setModel(tableModel);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		/* Сustom column width: */
		TableColumn column = null;
		column = table.getColumnModel().getColumn(0);
    	column.setPreferredWidth(100); 
    	column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(100);
        column = table.getColumnModel().getColumn(2);
    	column.setPreferredWidth(200); 
    	column = table.getColumnModel().getColumn(3);
        column.setPreferredWidth(300);

		scrollPane = new JScrollPane(table);

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
		Environment env = Environment.getInstance();
		
		switch(e.getActionCommand()) {
			
			case "Add":
				System.out.println("dataList: " + dataList.size()); 
				AddOperationDialog addOperationDialog = new AddOperationDialog(tableModel);
				addOperationDialog.show();
				//operations = env.getOperationsByName();				
				dataList = operations.getList();
				System.out.println("dataList: " + dataList.size());
				table.repaint();
			//scrollPane.revalidate();
			//	scrollPane.repaint();
				break;
			case "Change": 
				selectedRow = table.getSelectedRow();
				if(selectedRow == -1){break;}	//Do nothing if don't selected any row
				Operation operationToChange = new Operation();
				operationToChange.setSum((BigDecimal)table.getValueAt(selectedRow, 0));
				operationToChange.setDate((Date)table.getValueAt(selectedRow, 1));
				operationToChange.setDescription((String)table.getValueAt(selectedRow, 2));
				operationToChange.setTags(((String )table.getValueAt(selectedRow, 3)).split(","));
				operationToChange.setId((Integer )table.getValueAt(selectedRow, 4));
				ChangeOperationDialog changeOperationDialog	= new ChangeOperationDialog(
												tableModel, selectedRow, operationToChange);
				changeOperationDialog.show();
				dataList = operations.getList();
				table.repaint();
				break;
			case "Delete": 				
				selectedRow = table.getSelectedRow();
				if(selectedRow == -1){break;}	//Do nothing if don't selected any row				
				Operation operationToDelete = new Operation();
				operationToDelete.setSum((BigDecimal)table.getValueAt(selectedRow, 0));
				operationToDelete.setDate((Date)table.getValueAt(selectedRow, 1));
				operationToDelete.setDescription((String)table.getValueAt(selectedRow, 2));
				operationToDelete.setTags(((String )table.getValueAt(selectedRow, 3)).split(","));
				operationToDelete.setId((Integer )table.getValueAt(selectedRow, 4));
				DeleteOperationDialog deleteOperationDialog = new DeleteOperationDialog(
												tableModel, selectedRow, operationToDelete);
				deleteOperationDialog.show();
				dataList = operations.getList();
				table.repaint();
				break;	
			default:
				break;
		}
		
	}

	public static OperationListPanel getInstance() {
		if (instance == null) {
            instance = new OperationListPanel();
        }
        return instance;
	}

	public void refreshDataList() {
		AccountingPanel ap = AccountingPanel.getInstance();
		operations = new Operations(ap.getCurrentAccounting());
		dataList = operations.getList();
		table.repaint();
	}
}
