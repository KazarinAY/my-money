package kazarin.my_money.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class Environment{
	private static Environment instance;
	
	private Path dataDir;
	private Path dataFile;

	private Environment(){
		String directory = System.getProperty("user.home") + "/mymoney";
		String fileName = directory + "/data";
		dataDir = Paths.get(directory);
		dataFile = Paths.get(fileName);
	}
	
	public static Environment getInstance(){
		if (instance == null) {
			instance = new Environment();
		}
		return instance;
	}

	public Operations load(){
		Operations ops = null;
		if (Files.exists(dataFile)){
			try (FileInputStream fis = new FileInputStream(dataFile.toString());
				 ObjectInputStream ois = new ObjectInputStream(fis);){

				ops = (Operations) ois.readObject();
			} catch (ClassNotFoundException cnfe){

			} catch(IOException ioe){

			}			
		} else {
			try {
				createDataFile();
				ops = Operations.getInstance();
			} catch (IOException ioe){

			}
		}	
		return ops;
	}
	
	public void save(){
		if (Files.exists(dataFile)){
			try (FileOutputStream fos = new FileOutputStream(dataFile.toString());
				 ObjectOutputStream oos = new ObjectOutputStream(fos);){

				oos.writeObject(Operations.getInstance());
			} catch(IOException e){

			}
		} else {
			try {
				createDataFile();
				save(); 				// recursive calling
			} catch (IOException ioe){

			}
		}	
	}	

	private void createDataFile() throws IOException{
		if (!Files.exists(dataDir)){
			Files.createDirectories(dataDir);
		}
		Files.createFile(dataFile);
	}	
}