/*
 * MainScreen
 */
package kazarin.my_money.gui;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Environment;

import java.util.logging.*;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * Displays a window that contains a tabbed pane
 * with two panels that contains tables.
 */
public class MainScreen extends JFrame{	
	/**
     * An instance of MainScreen.
     */
    private static MainScreen instance;

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
	
	private JTabbedPane tabbedPane;
	private OperationListPanel operationListPanel;
	//private StudentListPanel studentListPanel;
	
	/**	
	 * Constructs a MainScreen and displays it.
	 */
	private MainScreen(){	
		super();		

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		tabbedPane = new JTabbedPane();
		operationListPanel = new OperationListPanel(this);
		//studentListPanel = new StudentListPanel(this);
		tabbedPane.addTab("operation list", operationListPanel);		
		//tabbedPane.addTab("Student list", studentListPanel);		
		
		getContentPane().add(tabbedPane);		
		
		setLocation(200, 200);
		pack();
		setVisible(true);		
	}



	/**
     * Gets instance of MainScreen.
     * @return instance
     */
    public static MainScreen getInstance() {
        if (instance == null) {
            instance = new MainScreen();
        }
        return instance;
    }
	
	/**
     * The main method is where the main screen are created.
     */
	public static void main(String[] args){
		

		JFrame frame = new JFrame();
		
		Environment env = Environment.getInstance();
		
		if (!env.isReady()) {
			logger.info("env is not ready");
			PrepareDialog pd = new PrepareDialog(frame);
		}

		Operations ops = Operations.getInstance(env.getDBType());
		
		MainScreen mainScreen = MainScreen.getInstance();
	}
}