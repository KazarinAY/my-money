package kazarin.my_money.model;

import java.util.logging.*;
import java.io.IOException;

public class ModelLogger {

	private static Logger logger;
        
    private FileHandler fh;

    private ModelLogger() {
    	try {
    		logger = Logger.getLogger(ModelLogger.class.getName());
	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("/tmp/modellogger.log");  
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
	            new ModelLogger();	        
	    }
	    return logger;
	}

	public static void log(Level level, String msg){
	    getLogger().log(level, msg);
	}

	public static void log(Level level, String msg, Throwable t){
	    getLogger().log(level, msg, t);
	}

	public static void info(String msg, Throwable t){
	    getLogger().log(Level.INFO, msg, t);
	}

	public static void info(String msg){
	    getLogger().log(Level.INFO, msg);
	}

	public static void warning(String msg){
	    getLogger().log(Level.WARNING, msg);
	}

	public static void warning(String msg, Throwable t){
	    getLogger().log(Level.WARNING, msg, t);
	}
}
