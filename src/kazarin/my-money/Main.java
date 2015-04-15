package kazarin.my_money;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Environment;
import kazarin.my_money.model.WrongCommandException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

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
		Environment env = Environment.getInstance();
		env.loadFromTxt(env.getTxtDataFile());
		Operations ops = Operations.getInstance();
		ops.printStatistic();

		System.out.print("My Money, enter command:");
		try (BufferedReader reader =
					new BufferedReader(new InputStreamReader(System.in))) {
			while (true) {
				String line = reader.readLine();

				switch (line.split(" ")[0]) {
					case "show": 	ops.showAllList();
									printEnterCommand();
									break;

					case "add":		try {
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

					case "change": 	try {
										ops.change(line);
										ops.printStatistic();
									} catch (WrongCommandException e) {
										printWrongCommand();
									}
									printEnterCommand();
									break;

					case "stat": 	//try {
										ops.printStatistic();
									//} catch (WrongCommandException e){
									//	System.out.println("Wrong command!");
									//}
									printEnterCommand();
									break;

					case "exit":	env.saveToTxt(env.getTxtDataFile());
									return;

					default: 		printWrongCommand();
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
}
