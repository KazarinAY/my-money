package kazarin.my_money;

import java.util.logging.*;
import java.io.IOException;

public class MyLogger {

	private static Logger logger;
        
    private FileHandler fh;

    private MyLogger() {
    	try {
    		logger = Logger.getLogger(MyLogger.class.getName());
	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("/tmp/mymoney.log");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter); 
	        logger.setUseParentHandlers(false); 

	        // the following statement is used to log any messages  
	        logger.info("Start logging...");  

	    } catch (SecurityException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }  
    }

    private static Logger getLogger(){
	    if(logger == null){	        
	            new MyLogger();	        
	    }
	    return logger;
	}

	public static void log(Level level, String msg){
	    getLogger().log(level, msg);    
	}

}
