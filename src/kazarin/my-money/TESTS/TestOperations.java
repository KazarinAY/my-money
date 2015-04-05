package kazarin.my_money.TESTS;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Operation;

import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;

public class TestOperations {	
	@Test
	public void testSerialisation() 
				throws IOException, ClassNotFoundException {
		Operations ops = Operations.getInstance(); 
		//String = opStr = "add +500 : 21-03-2015"
		//ops.add(opStr);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		ops.writeExternal(oos);
		baos.close(); oos.close();

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		Operations opsLoaded = Operations.getInstance();
		opsLoaded.readExternal(ois);	
		bais.close(); ois.close();
		if (ops != opsLoaded){
			throw new IOException("Bad serialisation"); //BAD EXCEPTION
		}
	}
}