package kazarin.my_money;

import java.util.logging.*;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Environment;
import kazarin.my_money.model.WrongCommandException;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Main class.
 */
public final class Main {
	private static Logger logger;

	/**
	 * main method.
	 * @param args	arguments
	 * @throws IOException if...
	 * @throws ClassNotFoundException if...
	 */
	public static void main(final String[] args)
			throws IOException, ClassNotFoundException {
		
		logger = Logger.getLogger(Main.class.getName());
        FileHandler fh = new FileHandler("/tmp/Main.log");  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter); 
        logger.setUseParentHandlers(false);
		
		try (BufferedReader reader =
					new BufferedReader(new InputStreamReader(System.in))) {
		Environment env = Environment.getInstance();		
			while (!env.isReady()) {
				logger.info("env isn't ready...");			
				prepareDialog(reader);
			}		

			runCommandLine(reader);		
		} catch (IOException e) {
			logger.log(Level.WARNING, "BufferedReader", e);
		}
	} // main()	

	/**
	 * Constructor.
	 */
	private Main() {
		/*NON*/
	}

	/**
	 * Prints "My Money, enter command:".
	 */
	private static void printEnterCommand() {
		System.out.print("My Money, enter command:");
	}

	/**
	 * Prints "Wrong command!".
	 */
	private static void printWrongCommand() {
		System.out.println("Wrong command!");
	}

	/**
	 * Gets file neme from "saveto" command.
	 */
	private static String getFileName(final String line) throws WrongCommandException{
		String fileName = null;
		if (line.startsWith("saveto ")) {
			fileName = line.substring(7);
		} else if (line.startsWith("loadfrom ")) {
			fileName = line.substring(9);
		} else {
			throw new WrongCommandException();
		}
		
		System.out.println(fileName);
		return fileName;
	}

	private static void runCommandLine(BufferedReader reader) throws IOException {
		Environment env = Environment.getInstance();		

		Operations ops = Operations.getInstance(env.getDBType());		
		ops.printStatistic();

		printEnterCommand();
		
		while (true) {
			String line = reader.readLine();

			switch (line.split(" ")[0]) {
				case "show": 		ops.printAllList();
									printEnterCommand();
									break;

				case "add":			try {
										ops.add(line);
										ops.printStatistic();
									} catch (WrongCommandException e) {
										printWrongCommand();
									}
									printEnterCommand();
									break;

				case "delete":		try {
										ops.delete(line);
										ops.printStatistic();
									} catch (WrongCommandException e) {
										printWrongCommand();
									}
									printEnterCommand();
									break;

				case "change": 		try {
										ops.change(line);
										ops.printStatistic();
									} catch (WrongCommandException e) {
										printWrongCommand();
									}
									printEnterCommand();
									break;

				case "stat": 		//try {
									ops.printStatistic();
									//} catch (WrongCommandException e){
									//	System.out.println("Wrong command!");
									//}
									printEnterCommand();
									break;

				case "saveto":  	try {
										ops.saveToTxt(getFileName(line));
									} catch (WrongCommandException e) {
										printWrongCommand();
									}
									printEnterCommand();
									break;

				case "loadfrom": 	try {
										ops.loadFromTxt(getFileName(line));
									} catch (WrongCommandException e) {
										printWrongCommand();
									}
									printEnterCommand();
									break;

				case "exit":		//env.saveToTxt(env.getTxtDataFile());
									return;

				default: 			printWrongCommand();
									printEnterCommand();
									break;
			}
		}
		
	}

	private static void prepareDialog(BufferedReader reader) throws IOException {
		
		System.out.print("user: ");
		String user = reader.readLine();
		
		System.out.print("password: ");
		String password = reader.readLine();
		
		System.out.print("host: ");
		String host = reader.readLine();

		System.out.print("DB name: ");
		String dbName = reader.readLine();
		
		System.out.print("DB (MySQL or HSQL): ");
		String db = reader.readLine();
		
		Environment env = Environment.getInstance();
		env.prepare(user, password, host, dbName, db);
		
	}
}
