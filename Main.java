package kazarin.my_money;

import kazarin.my_money.model.Operation;
import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;


public class Main{
	public static void main(String[] args) 
			throws IOException, ClassNotFoundException{	
		
		Operation op = new Operation(BigDecimal.valueOf(77.77d);, 
										"Test description", "tag1", "tag2");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(op);
		baos.close(); oos.close();

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		Operation opLoaded = new Operation(BigDecimal.valueOf(99.99d), 
									"description", "tag3", "tag4");
		opLoaded = (Operation) ois.readObject();		
		bais.close(); ois.close();
		System.out.println(op.equals(opLoaded));
	} // main()
}