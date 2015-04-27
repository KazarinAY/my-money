package kazarin.my_money.gui;

import kazarin.my_money.model.Operation;
import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Environment;
import kazarin.my_money.model.ModelException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent;
import java.awt.Color;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import java.math.BigDecimal;



/**
 * Displays a window that contains a tabbed pane
 * with two panels that contains tables.
 */
public class MainScreen extends JPanel{

	private Environment env;
	private Operations operations;
	private List<Operations> accList;
	private List<Operation> opList;

	private JFrame frame;	
	//WEST
	private JTable table;
	private TableModel tableModel;
	private JScrollPane scrollPane;
	//EAST
	private JButton newEntry;
	private List<BunchOfButtons> bunchOfButtons;
	private String currentAccounting;
	private ButtonGroup buttonGroup;	
	private JPanel eastPanel;
	private JPanel radioButtonsPanel;
	private JPanel buttonsPanel;
	private JButton createNewButton;
	private JButton conToExButton;
	//SOUTH
	private JPanel southPanel;
	private JTextArea labelTextErea;
	private	JTextArea statTextArea;
	private JTextArea textArea;
	private	JScrollPane scrollPaneForText;
	

	/**	
	 * Constructs a MainScreen and displays it.
	 */
	private MainScreen(JFrame frame){
		super(new BorderLayout());		
		
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GuiLogger.info("ACTION: " + e.getActionCommand());
			}
		};
		accList = new ArrayList<Operations>(); // = env.getAccountings();
		bunchOfButtons = new ArrayList<BunchOfButtons>();
		buttonGroup = new ButtonGroup();
		if (accList.size() > 0) {
			for (Operations acc : accList) {			
				JRadioButton rButton = new JRadioButton(acc.getName());
				rButton.addActionListener(actionListener);
				JButton edit = new JButton("edit");
				edit.addActionListener(actionListener);
				JButton del = new JButton("del");
				del.addActionListener(actionListener);
				bunchOfButtons.add(new BunchOfButtons(rButton, edit, del));
			}
			bunchOfButtons.get(0).getJRButton().setSelected(true);
			currentAccounting = bunchOfButtons.get(0).getJRButton().getActionCommand();
			CurrentAccountingHolder.setCurrentAccounting(currentAccounting);
			for (BunchOfButtons btn : bunchOfButtons) {
				buttonGroup.add(btn.getJRButton());
			}
		}
		eastPanel = new JPanel(new GridLayout(3, 0));
		newEntry = new JButton("New Entry");
		newEntry.addActionListener(actionListener);
		eastPanel.add(newEntry);
		radioButtonsPanel = new JPanel(new GridLayout(0, 3));
		for (BunchOfButtons btn : bunchOfButtons) {
			radioButtonsPanel.add(btn.getJRButton());
			radioButtonsPanel.add(btn.getEdit());
			radioButtonsPanel.add(btn.getDel());
		}
		eastPanel.add(radioButtonsPanel);
		buttonsPanel = new JPanel(new GridLayout(2, 0));
		
		createNewButton = new JButton("New accounting");		
		createNewButton.addActionListener(actionListener);
		buttonsPanel.add(createNewButton);
       
		conToExButton = new JButton("Connect to existing");
		conToExButton.addActionListener(actionListener);
        buttonsPanel.add(conToExButton);	
		
		eastPanel.add(buttonsPanel);
		
		add(eastPanel, BorderLayout.EAST);

		southPanel = new JPanel(new FlowLayout());
		labelTextErea = new JTextArea("total income:\n"
							 + "total consumption:\n"
							 + "balance:", 3, 20);
		labelTextErea.setEditable(false);
		labelTextErea.setBackground(Color.LIGHT_GRAY);
		southPanel.add(labelTextErea);

		statTextArea = new JTextArea(3, 7);
		statTextArea.setEditable(false);
		southPanel.add(statTextArea);

		textArea = new JTextArea(3, 40);
		scrollPaneForText = new JScrollPane(textArea); 
		textArea.setEditable(false);
		southPanel.add(scrollPaneForText);
		add(southPanel, BorderLayout.SOUTH);
		
		try {
			env = Environment.getInstance();			
		} catch (ModelException me) {
			textArea.setText(me.getMessage());
			GuiLogger.warning("Environment.getInstance()");
		}

		/*
		String dbName = ap.getCurrentAccounting();
		operations = env.getOperationsByName(dbName);
		dataList = operations.getList();
		*/
		opList = new ArrayList<Operation>(); 

		table = new JTable();
		tableModel = new AbstractTableModel() {	
			private String[] columnNames = {"How Much",
								            "Date",
								            "Description",
								            "Tags",
								            "Id"								            
								            };		
						
			@Override
			public int getColumnCount() { 
				return columnNames.length; 
			}
			
			@Override
			public int getRowCount() {
				return opList.size();
			}
			
			@Override
			public Object getValueAt(int row, int col) {
				Operation operation = opList.get(row);
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
			public boolean isCellEditable(int row, int col) {
				if (col > 3) return false;

				return true;
			}
			
			@Override
			public void setValueAt(Object aValue, int row, int col) {
				if(opList.size() < row) { 								//add new row
					Operation operation = new Operation();
					operation.setSum((BigDecimal)aValue);
					opList.add(operation);			
				} else {													//change row
					Operation operation = opList.get(row);
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
				opList.remove(row);
				fireTableRowsDeleted(row, row);				
			}
			
			@Override
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}			
		}; 
		
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
        column = table.getColumnModel().getColumn(4);
        column.setMinWidth(0);
   		column.setMaxWidth(0);
   		column.setWidth(0);

		scrollPane = new JScrollPane(table);

		table.setPreferredScrollableViewportSize(new Dimension(600, 250));
		table.setFillsViewportHeight(true);
		
		add(scrollPane, BorderLayout.WEST);

	}

	private class BunchOfButtons {
		private JRadioButton jrButton;
		private JButton edit;
		private JButton del;

		public BunchOfButtons(JRadioButton jrButton, JButton edit, JButton del) {
			this.jrButton =jrButton;
			this.edit = edit;
			this.edit.setActionCommand("edit:" + jrButton.getActionCommand());
			this.del = del;
			this.del.setActionCommand("del:" + jrButton.getActionCommand());
		}
		public JRadioButton getJRButton() {return jrButton;}
		public JButton getEdit() {return edit;}
		public JButton getDel() {return del;}
	}
	
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("My Money");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MainScreen newContentPane = new MainScreen(frame);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
