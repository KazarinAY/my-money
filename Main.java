package kazarin.my_money;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Operation;
import kazarin.my_money.model.Environment;
import java.math.BigDecimal;
import java.io.IOException;
import java.lang.ClassNotFoundException;



public class Main{
	public static void main(String[] args) 
			throws IOException, ClassNotFoundException{	
		Environment env = Environment.getInstance(false);
		Operations ops = env.load();
		
		ops.showAllList();
		env.save();
	} // main()
}