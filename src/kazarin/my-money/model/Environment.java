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
		dataDir = Paths.get(directory);	
		
		if (!Files.exists(dataDir)){
			createDirectories();
		}	
		dataFile = Paths.get( directory + "/data");		
	}	
	
	/**
	 * @return
	 */
	public static Environment getInstance(){
		if (instance == null) {
			instance = new Environment();
		}
		return instance;
	}

	/**
	 * @return
	 */
	public Operations load(){
		Operations ops = Operations.getInstance();
		if (Files.exists(dataFile)){
			System.out.println("LOG Operations.load(): dataFile.toString() = " + dataFile.toString());
			try (FileInputStream fis = new FileInputStream(dataFile.toString());
				 ObjectInputStream ois = new ObjectInputStream(fis);){
				System.out.println("LOG Operations.load(), from dataFile: ois = " + ois);
				ops.readExternal(ois);	
				System.out.println("LOG Operations.load(), from dataFile: ops = " + ops);							
			} catch (ClassNotFoundException cnfe){
				System.err.println("Load ClassNotFoundException");
			} catch(IOException ioe){
				System.err.println("Load IOException");
			}			
		} else {
			createDataFile();
			ops = Operations.getInstance();			
		}	
		System.out.println("LOG Operations.load(): ops = " + ops);
		return ops;
	}
	
	/**
	 * 
	 */
	public void save(){
		if (Files.exists(dataFile)){
			try (FileOutputStream fos = new FileOutputStream(dataFile.toString());
				 ObjectOutputStream oos = new ObjectOutputStream(fos);){
				System.out.println("LOG Operations.save(): Operations.getInstance() = " + 
									Operations.getInstance());
				Operations ops = Operations.getInstance(); 
				ops.writeExternal(oos);
			} catch(IOException e){
				System.err.println("Save IOException");
			}
		} else {			
			createDataFile();
			save(); 			// recursive calling
			
		}	
	}	

	/**
	 * 
	 */
	private void createDataFile(){
		try {
			Files.createFile(dataFile);
		} catch (IOException e){
			System.err.println("ERROR: failed to create data file!");
		}
	}

	/**
	 * 
	 */
	private void createDirectories(){
		try{
			Files.createDirectories(dataDir);	
		} catch (IOException e){
			System.err.println("ERROR: failed to create data directories!");
		}
	}	
}