package kazarin.my_money;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Operation;
import kazarin.my_money.model.Environment;
import kazarin.my_money.model.WrongCommandException;
import java.math.BigDecimal;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main{
	public static void main(String[] args) 
			throws IOException, ClassNotFoundException{	
		Environment env = Environment.getInstance();
		Operations ops = env.load();
		System.out.println("LOG Main.main(): env.load() = " + ops);
		ops.printStatistic();
		
		System.out.print("My Money, enter command:");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
			String line = "";
			while (!(line = reader.readLine()).split(" ")[0].equals("exit")){
				switch (line.split(" ")[0]){
					case "show": 	ops.showAllList();
									System.out.print("My Money, enter command:");
									break;

					case "add":		try {
										ops.add(line);
										ops.printStatistic();
									} catch (WrongCommandException e){
										System.out.println("Wrong command!");
									}
									System.out.print("My Money, enter command:");
									break;

					case "del":		try {
										ops.delete(line);
										ops.printStatistic();
									} catch (WrongCommandException e){
										System.out.println("Wrong command!");
									}
									System.out.print("My Money, enter command:");
									break;

					case "change": 	try {
										ops.change(line);
										ops.printStatistic();
									} catch (WrongCommandException e){
										System.out.println("Wrong command!");
									}
									System.out.print("My Money, enter command:");									
									break;

					default: 		System.out.println("Wrong command!");
									System.out.print("My Money, enter command:");
									break;
				}
			}
		} catch (IOException e){
			System.err.println(e.getMessage());
		}			
		
		env.save();
	} // main()
}