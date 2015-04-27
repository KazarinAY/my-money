package kazarin.my_money.gui;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Environment;
import kazarin.my_money.model.ModelException;

import javax.swing.JPanel;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Is a panel with table of groups.
 */
public class AccountingPanel extends JPanel implements ActionListener{

	public String currentAccounting;

	private List<Operations> dataList;

	List<BunchOfButtons> bunchOfButtons;

	ButtonGroup buttonGroup;
	
	JPanel northPanel;	

	/**	
     * Constructs a AccountingPanel.
	 *
	 */
	public AccountingPanel() {
		super();
		try {
			Environment env = Environment.getInstance();
			dataList = env.getAccountings();
		} catch (ModelException me) {
			GuiLogger.warning("Environment.getInstance()");
		}		
		bunchOfButtons = new ArrayList<BunchOfButtons>();
		buttonGroup = new ButtonGroup();
		if (dataList.size() > 0) {
			for (Operations acc : dataList) {			
				JRadioButton rButton = new JRadioButton(acc.getName());
				rButton.addActionListener(this);
				JButton edit = new JButton("edit");
				edit.addActionListener(this);
				JButton del = new JButton("del");
				del.addActionListener(this);
				bunchOfButtons.add(new BunchOfButtons(rButton, edit, del));
			}
			bunchOfButtons.get(0).getJRButton().setSelected(true);
			currentAccounting = bunchOfButtons.get(0).getJRButton().getActionCommand();
			CurrentAccountingHolder.setCurrentAccounting(currentAccounting);
			for (BunchOfButtons btn : bunchOfButtons) {
				buttonGroup.add(btn.getJRButton());
			}
		}		
		setLayout(new BorderLayout());
		
		northPanel = new JPanel(new GridLayout(0, 3));
		
		for (BunchOfButtons btn : bunchOfButtons) {
			northPanel.add(btn.getJRButton());
			northPanel.add(btn.getEdit());
			northPanel.add(btn.getDel());
		}		
		
		add("North", northPanel);
				
		JPanel southPanel = new JPanel();

		JButton createNewButton = new JButton("New accounting");		
		createNewButton.addActionListener(this);
		southPanel.add(createNewButton);
       
		JButton conToExButton = new JButton("Connect to existing");
		conToExButton.addActionListener(this);
        southPanel.add(conToExButton);

		
		add("South", southPanel); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = buttonGroup.getSelection().getActionCommand();		

		GuiLogger.info(e.getActionCommand());
		GuiLogger.info(command);
		
	}

	private class BunchOfButtons {
		private JRadioButton jrButton;
		private JButton edit;
		private JButton del;

		public BunchOfButtons(JRadioButton jrButton, JButton edit,
																JButton del) {
			this.jrButton =jrButton;
			this.edit = edit;
			this.edit.setActionCommand("edit:" + jrButton.getActionCommand());
			this.del = del;
			this.del.setActionCommand("del:" + jrButton.getActionCommand());
		}

		public JRadioButton getJRButton() {
			return jrButton;
		}

		public JButton getEdit() {
			return edit;
		}

		public JButton getDel() {
			return del;
		}
	}

	public String getCurrentAccounting() {
		return currentAccounting;
	}

	public void addNewJRButton(String dbName) {
				JRadioButton rButton = new JRadioButton(dbName);
				rButton.addActionListener(this);
				JButton edit = new JButton("edit");
				edit.addActionListener(this);
				JButton del = new JButton("del");
				del.addActionListener(this);
				bunchOfButtons.add(new BunchOfButtons(rButton, edit, del));
				buttonGroup.add(rButton);
				northPanel.add(rButton);
				northPanel.add(edit);
				northPanel.add(del);
				rButton.setSelected(true);
				currentAccounting = rButton.getActionCommand();
				CurrentAccountingHolder.setCurrentAccounting(currentAccounting);
				northPanel.revalidate();
				northPanel.repaint();				
	}
}
