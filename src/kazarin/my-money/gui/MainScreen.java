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
import javax.swing.JOptionPane;
import javax.swing.JLabel;

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
	private static final String CONNECT_TO_EXISTING = "Connect to existing";
	private static final String NEW_ACCOUNTING = "New accounting";
	private static final int MAX_ACCAUNTINGS_NUMBET = 5;
	private static final boolean NEW = true;
	private static final boolean CONNECT = false;
	private static final String NEW_ENTRY = "New entry";

	private Environment env;
	private Operations operations;
	private List<Operations> accList;
	private List<Operation> opList;
	private String currentAccounting;
	String[] result;

	private JFrame frame;
	private ActionListener actionListener;	
	//WEST
	private JTable table;
	private TableModel tableModel;
	private JScrollPane scrollPane;
	//EAST
	private JButton newEntry;
	private List<BunchOfButtons> bunchOfButtons;	
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
	private MainScreen(final JFrame frame){
		super(new BorderLayout());
		
		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();							
				switch (command) {
					case CONNECT_TO_EXISTING:
						if (accList.size() >= MAX_ACCAUNTINGS_NUMBET) {
							JOptionPane.showMessageDialog(frame,
                                    "Alredy to mutch accountings.",
                                    "Warning",
                                    JOptionPane.WARNING_MESSAGE);
									break;
						}
						AccountingDialog connectDialog = new AccountingDialog(CONNECT);
						result = connectDialog.getResultDbName();
						if (result == null) break; //if Canceled
						GuiLogger.info("resultDbName: " + result[3]);
						if (isAlredyExists(result[3])) {							
							textArea.append("\n" + "such accounting alredy exists");
							break;
						}
						try {
							//Environment env = Environment.getInstance();
							env.connectToExistingAccounting(result[0], result[1],				//(user, password,
													result[2], result[3], result[4]);	//host, dbName, dbType);						
							textArea.append("\n" + "Connected to accounting.");
						} catch (ModelException me) {
							String message = me.getMessage();
							GuiLogger.warning("env.connectToExistingAccounting\n" + message);
							textArea.append("\n" + message);
						}
						addNewJRButton(result[3]);
						operations = env.getOperationsByName(currentAccounting);
						opList = operations.getList();
						revalidate();
						repaint();
						break;
					case NEW_ACCOUNTING:						
						if (accList.size() >= MAX_ACCAUNTINGS_NUMBET) {
							JOptionPane.showMessageDialog(frame,
                                    "Alredy to mutch accountings.",
                                    "Warning",
                                    JOptionPane.WARNING_MESSAGE);
									break;
						}
						AccountingDialog newDialog = new AccountingDialog(NEW);
						result = newDialog.getResultDbName();
						if (result == null) break; //if Canceled
						GuiLogger.info("resultDbName: " + result[3]);
						if (isAlredyExists(result[3])) {							
							textArea.append("\n" + "such accounting alredy exists");
							break;
						}
						try {							
							env.createNewAccounting(result[0], result[1],				//(user, password,
													result[2], result[3], result[4]);	//host, dbName, dbType);						
							textArea.append("\n" + "New accounting created.");
						} catch (ModelException me) {
							String message = me.getMessage();
							GuiLogger.warning("env.createNewAccounting\n" + message);
							textArea.append("\n" + message);
						}
						addNewJRButton(result[3]);
						revalidate();
						repaint();
						break;

					case NEW_ENTRY:
						AddOperationDialog addOperationDialog = new AddOperationDialog(tableModel);
						addOperationDialog.show();						
						result = addOperationDialog.getResultDbName();
						if (result == null) break; //if Canceled
						Operations operations = env.getOperationsByName(currentAccounting);
						try {
							Operation addedOperation = new Operation(result[0], result[1], result[2], result[3]);
							operations.add(addedOperation);
						} catch (ModelException me) {
							String message = me.getMessage();
							GuiLogger.warning("operations.add(command)\n" + message);
							textArea.append("\n" + message);
							break;
						}
						opList = operations.getList();
						revalidate();
						repaint();						
						break;
					default:
						GuiLogger.info("ACTION: " + command);
						break;
				}
			}
		};
		accList = new ArrayList<Operations>(); // = env.getAccountings();
		opList = new ArrayList<Operation>(); 
		try {
			env = Environment.getInstance();
			accList = env.getAccountings();
		} catch (ModelException me) {
			textArea.setText(me.getMessage());
			GuiLogger.warning("Environment.getInstance()");
		}
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

			for (BunchOfButtons btn : bunchOfButtons) {
				buttonGroup.add(btn.getJRButton());
			}

			operations = env.getOperationsByName(currentAccounting);
			opList = operations.getList();

		}
		eastPanel = new JPanel(new GridLayout(3, 0));
		newEntry = new JButton(NEW_ENTRY);
		newEntry.addActionListener(actionListener);
		eastPanel.add(newEntry);
		radioButtonsPanel = new JPanel(new GridLayout(0, 3));
		JLabel label1 = new JLabel("Accountings:");
		JLabel label2 = new JLabel("");
		JLabel label3 = new JLabel("");
		radioButtonsPanel.add(label1);
		radioButtonsPanel.add(label2);
		radioButtonsPanel.add(label3);
		for (BunchOfButtons btn : bunchOfButtons) {
			radioButtonsPanel.add(btn.getJRButton());
			radioButtonsPanel.add(btn.getEdit());
			radioButtonsPanel.add(btn.getDel());
		}
		eastPanel.add(radioButtonsPanel);
		buttonsPanel = new JPanel(new GridLayout(2, 0));
		
		createNewButton = new JButton(NEW_ACCOUNTING);		
		createNewButton.addActionListener(actionListener);
		buttonsPanel.add(createNewButton);
       
		conToExButton = new JButton(CONNECT_TO_EXISTING);
		conToExButton.addActionListener(actionListener);
        buttonsPanel.add(conToExButton);	
		
		eastPanel.add(buttonsPanel);
		
		add(eastPanel, BorderLayout.EAST);

		southPanel = new JPanel(new FlowLayout());
		labelTextErea = new JTextArea("total income\n"
							 + "total consumption:\n"
							 + "balance:", 3, 20);
		labelTextErea.setEditable(false);
		labelTextErea.setBackground(Color.LIGHT_GRAY);
		southPanel.add(labelTextErea);

		statTextArea = new JTextArea(3, 7);
		statTextArea.setEditable(false);
		southPanel.add(statTextArea);

		textArea = new JTextArea(5, 40);
		scrollPaneForText = new JScrollPane(textArea); 
		textArea.setEditable(false);
		southPanel.add(scrollPaneForText);
		add(southPanel, BorderLayout.SOUTH);
		

		

		table = new JTable();
		tableModel = new AbstractTableModel() {	
			private String[] columnNames = {"Sum",
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
    private void addNewJRButton(String dbName) {
    			try {
					Operations newOps = new Operations(dbName);
					accList.add(newOps);
					JRadioButton rButton = new JRadioButton(dbName);
					rButton.addActionListener(actionListener);
					JButton edit = new JButton("edit");
					edit.addActionListener(actionListener);
					JButton del = new JButton("del");
					del.addActionListener(actionListener);
					bunchOfButtons.add(new BunchOfButtons(rButton, edit, del));
					buttonGroup.add(rButton);
					radioButtonsPanel.add(rButton);
					radioButtonsPanel.add(edit);
					radioButtonsPanel.add(del);
					rButton.setSelected(true);
					currentAccounting = rButton.getActionCommand();					
				} catch (ModelException me) {
					textArea.append("\n" + me.getMessage());
				}
	}

	private boolean isAlredyExists(String newAcc) {
		for (Operations ops : accList) {
			if (ops.getName().equals(newAcc))
				return true;
		}
		return false;
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
