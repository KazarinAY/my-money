package kazarin.my_money.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;

public class TestOperation {
	@Test
	public void testAdd() {
		String str= "Junit is working fine";
		assertEquals("Junit is working fine", str);
	}
	@Test
	public void testSerialisation() 
				throws IOException, ClassNotFoundException {
		Operation op = new Operation(BigDecimal.valueOf(77.77d), 
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
		if (!op.equals(opLoaded)){
			throw new IOException("Bad serialisation"); //BAD EXCEPTION
		}
	}
}