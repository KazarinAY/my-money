package kazarin.my_money.gui;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Environment;

import java.util.logging.*;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Displays a window that contains a tabbed pane
 * with two panels that contains tables.
 */
public class MainScreen {
	private static Logger logger;
    static {
    	try {
            logger = Logger.getLogger(MainScreen.class.getName());
            FileHandler fh = new FileHandler("/tmp/MainScreen.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter); 
            logger.setUseParentHandlers(false);
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
    }
	
	public static JFrame frame;
	private JPanel mainPanel;
	OperationListPanel operationListPanel;
	private AccountingPanel accountingtListPanel;
	
	/**	
	 * Constructs a MainScreen and displays it.
	 */
	private MainScreen(){

	}
	
	private void run() {
		frame = new JFrame("My Money");		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel(new BorderLayout());	
		operationListPanel = OperationListPanel.getInstance();
		accountingtListPanel = AccountingPanel.getInstance();
		mainPanel.add(accountingtListPanel, BorderLayout.EAST);
		mainPanel.add(operationListPanel, BorderLayout.WEST);
		
		frame.getContentPane().add(mainPanel);
		
		frame.setLocation(200, 200);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args){
		MainScreen mainScreen = new MainScreen();
		mainScreen.run();
	}
}
