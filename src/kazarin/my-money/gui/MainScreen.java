package kazarin.my_money.gui;

import kazarin.my_money.model.Entry;
import kazarin.my_money.model.Accounting;
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
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

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
	private static final String NEW_ACCOUNTING = "new accounting";
	private static final int MAX_ACCAUNTINGS_NUMBET = 5;
	private static final boolean NEW = true;
	private static final boolean CONNECT = false;
	private static final String NEW_ENTRY = "New entry";

	private static JFrame frame;

	private Environment env;
	private Accounting accounting;
	private List<Accounting> accList;
	private List<Entry> opList;
	private String currentAccounting;
	String[] result;

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
//NORTH
	private JPanel northPanel;
	//MENU
	private JMenuBar menuBar;	
	private JMenu file;	
	private JMenuItem login;
	private JMenuItem logout;
	private JMenu exportMenu;
	private JMenuItem toTxt;
	private JMenuItem toXls;
	private JMenu importMenu;
	private JMenuItem fromTxt;
	private JMenuItem fromXls;
	private JMenuItem print;
	private JMenuItem exit;
	private JMenu accMenu;
	private JMenuItem newAcc;
	private JMenuItem delAcc;
	private JMenuItem connectToAcc;
	private JMenuItem closeAcc;
	private JMenu languages;
	private ButtonGroup menuButtonGroup;
	private JRadioButtonMenuItem english;
	private JRadioButtonMenuItem russian;
	private JMenu help;
	private JMenuItem documentation;
	private JMenuItem about;	
	
	private JPanel filterPanel;
	private JButton filterButton;
	private JTextArea filterTextArea;
	/**	
	 * Constructs a MainScreen and displays it.
	 */
	private MainScreen(){
		super(new BorderLayout());

		actionListener = new Listener();

		try {
			env = Environment.getInstance();
			accList = env.getAccountings();
		} catch (ModelException me) {
			textArea.setText(me.getMessage());
			GuiLogger.warning("Environment.getInstance()");
		}

//EAST		
		bunchOfButtons = new ArrayList<BunchOfButtons>();
		buttonGroup = new ButtonGroup();
		if (accList.size() > 0) {
			for (Accounting acc : accList) {
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

			accounting = env.getOperationsByName(currentAccounting);
			opList = accounting.getList();

		} else {		// no accountings
			opList = new ArrayList<Entry>();
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

//SOUTH
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

//WEST
		table = new JTable();
		tableModel = new AbstractTableModel() {	
			private String[] columnNames = {"Date",
											"Sum",								            
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
				Entry entry = opList.get(row);
				switch(col){
					case 0: return entry.getDate();
					case 1: return entry.getSum();
					case 2: return entry.getDescription();
					case 3: return entry.getTagsStr();
					case 4: return entry.getId();
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
					Entry entry = new Entry();
					entry.setSum((BigDecimal)aValue);
					opList.add(entry);			
				} else {													//change row
					Entry entry = opList.get(row);
					switch(col){
						case 0: entry.setDate((Date)aValue);
								break;
						case 1: entry.setSum((BigDecimal)aValue);
								break;
						case 2: entry.setDescription((String)aValue);
								break;
						case 3: entry.setTags((String[])aValue);
								break;
						case 4: entry.setId((Integer)aValue);
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
        column.setMinWidth(0);	//hide
   		column.setMaxWidth(0);	//id
   		column.setWidth(0);		//column

		scrollPane = new JScrollPane(table);

		table.setPreferredScrollableViewportSize(new Dimension(600, 250));
		table.setFillsViewportHeight(true);
		
		add(scrollPane, BorderLayout.WEST);

//NORTH
		northPanel = new JPanel(new GridLayout(2, 0));

		menuBar = new JMenuBar();
		
		file = new JMenu("file");
			login = new JMenuItem("login");
			login.addActionListener(actionListener);
			file.add(login);		
			
			logout = new JMenuItem("logout");
			logout.addActionListener(actionListener);
			file.add(logout);
			
			file.addSeparator();

			exportMenu = new JMenu("export");
				toTxt = new JMenuItem("to txt file");
				toTxt.addActionListener(actionListener);
				exportMenu.add(toTxt);

				toXls = new JMenuItem("to xls file");
				toXls.addActionListener(actionListener);
				exportMenu.add(toXls);
			file.add(exportMenu);

			importMenu = new JMenu("import");
				fromTxt = new JMenuItem("from txt file");
				fromTxt.addActionListener(actionListener);
				importMenu.add(fromTxt);

				fromXls = new JMenuItem("from xls file");
				fromXls.addActionListener(actionListener);
				importMenu.add(fromXls);
			file.add(importMenu);

			file.addSeparator();

			print = new JMenuItem("print");
			print.addActionListener(actionListener);
			file.add(print);

			file.addSeparator();

			exit = new JMenuItem("exit");
			exit.addActionListener(actionListener);
			file.add(exit);

		accMenu = new JMenu("accountings");
			newAcc = new JMenuItem(NEW_ACCOUNTING);
			newAcc.addActionListener(actionListener);
			accMenu.add(newAcc);

			delAcc = new JMenuItem("delete accounting");
			delAcc.addActionListener(actionListener);
			accMenu.add(delAcc);

			connectToAcc = new JMenuItem(CONNECT_TO_EXISTING);
			connectToAcc.addActionListener(actionListener);
			accMenu.add(connectToAcc);

			closeAcc = new JMenuItem("close accounting");
			closeAcc.addActionListener(actionListener);
			accMenu.add(closeAcc);

		languages = new JMenu("languages");
				menuButtonGroup = new ButtonGroup();
				english = new JRadioButtonMenuItem("english");
				english.addActionListener(actionListener);
				english.setSelected(true);
				menuButtonGroup.add(english);
				languages.add(english);

				russian = new JRadioButtonMenuItem("russian");
				russian.addActionListener(actionListener);
				menuButtonGroup.add(russian);
				languages.add(russian);
		
		help = new JMenu("help");
			documentation = new JMenuItem("documentation");
			documentation.addActionListener(actionListener);
			help.add(documentation);

			about = new JMenuItem("about");
			about.addActionListener(actionListener);
			help.add(about);

		menuBar.add(file);
		menuBar.add(accMenu);
		menuBar.add(languages);
		menuBar.add(help);

		northPanel.add(menuBar);
		
		filterPanel = new JPanel(new FlowLayout());
		filterButton = new JButton("Filter:");
		filterButton.addActionListener(actionListener);
		filterTextArea = new JTextArea("All", 1, 40);
		filterTextArea.setEditable(false);
		filterPanel.add(filterButton);
		filterPanel.add(filterTextArea);
		northPanel.add(filterPanel);
		add(northPanel, BorderLayout.NORTH);

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
	
	private void addNewJRButton(String dbName) {
    			try {
					Accounting newOps = new Accounting(dbName);
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
		for (Accounting ops : accList) {
			if (ops.getName().equals(newAcc))
				return true;
		}
		return false;
	}
	/*
	wtf is this?
	*/
	private boolean recalculateStatistic() {
		//statTextArea
		return false;
	}

	private class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			GuiLogger.info("command = " + command);			
			switch (command) {
				case CONNECT_TO_EXISTING:
					if (accList.size() >= MAX_ACCAUNTINGS_NUMBET) {
						JOptionPane.showMessageDialog(frame,
                                "Alredy to mutch accountings.",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
								break;
					}
					//AccountingDialog connectDialog = new AccountingDialog(CONNECT);
					//result = connectDialog.getResultDbName();
					if (result == null) break; //if Canceled
					GuiLogger.info("resultDbName: " + result[3]);
					if (isAlredyExists(result[3])) {							
						textArea.append("\n" + "such accounting alredy exists");
						break;
					}
					try {
						env.connectToExistingAccounting(result[0], result[1],				//(user, password,
												result[2], result[3], result[4]);	//host, dbName, dbType);						
						textArea.append("\n" + "Connected to accounting.");
					} catch (ModelException me) {
						String message = me.getMessage();
						GuiLogger.warning("env.connectToExistingAccounting\n" + message);
						textArea.append("\n" + message);
					}
					addNewJRButton(result[3]);
					accounting = env.getOperationsByName(currentAccounting);
					opList = accounting.getList();
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
					//AccountingDialog newDialog = new AccountingDialog(NEW);
					//result = newDialog.getResultDbName();
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
					//AddOperationDialog addOperationDialog = new AddOperationDialog(tableModel);
					//addOperationDialog.show();						
					//result = addOperationDialog.getResultDbName();
					if (result == null) break; //if Canceled
					Accounting accounting = env.getOperationsByName(currentAccounting);
					try {
						Entry addedOperation = new Entry(result[0], result[1], result[2], result[3]);
						accounting.add(addedOperation);
					} catch (ModelException me) {
						String message = me.getMessage();
						GuiLogger.warning("accounting.add(command)\n" + message);
						textArea.append("\n" + message);
						break;
					}
					opList = accounting.getList();
					revalidate();
					repaint();						
					break;
				default:

					break;
			}
		}
	}

	private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("My Money");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MainScreen newContentPane = new MainScreen();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static JFrame getFrame() {
    	return frame;
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
