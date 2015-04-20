package kazarin.my_money;

import kazarin.my_money.db.OperationsDao;
import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Operation;
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
	/**
	 * main method.
	 * @param args	arguments
	 * @throws IOException if...
	 * @throws ClassNotFoundException if...
	 */
	public static void main(final String[] args)
			throws IOException, ClassNotFoundException {

		OperationsDao opDao = new OperationsDao();
		System.out.println("My Money:");
		
		Environment env = Environment.getInstance();		
		Operations ops = Operations.getInstance();
		ops.setList(opDao.getAll());
		ops.printStatistic();

		System.out.print("My Money, enter command:");
		try (BufferedReader reader =
					new BufferedReader(new InputStreamReader(System.in))) {
			while (true) {
				String line = reader.readLine();

				switch (line.split(" ")[0]) {
					case "show": 		ops.showAllList();
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
											env.saveToTxt(getFileName(line));
										} catch (WrongCommandException e) {
											printWrongCommand();
										}
										printEnterCommand();
										break;

					case "loadfrom": 	try {
											env.loadFromTxt(getFileName(line));
										} catch (WrongCommandException e) {
											printWrongCommand();
										}
										printEnterCommand();
										break;

					case "exit":		env.saveToTxt(env.getTxtDataFile());
										return;

					default: 			printWrongCommand();
										printEnterCommand();
										break;
				}
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
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
}
