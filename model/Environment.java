package kazarin.my_money.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Properties;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;

public class Environment{
	private static Environment instance;

	private Path dataDir;
	private String deafultFileName = "data";
	private String testFileName = "test";
	private Properties properties;		
	private Path dataFile;

	private Environment(boolean isTest){
		//System.out.println("\nnew Environment()");
		String directory = System.getProperty("user.home") + "/mymoney";
		dataDir = Paths.get(directory);	
		//System.out.println("dataDir = " + dataDir);
		if (!Files.exists(dataDir)){
			createAllFiles();
		}
		properties = new Properties();		
		
		if (!isTest) loadProperties(deafultFileName);
		else loadProperties(testFileName);		

		String fileName = properties.getProperty("data file");
		//System.out.println("fileName = " + fileName);
		if (fileName != null){			
			dataFile = Paths.get(directory + "/" + fileName);
		} else {
			System.err.println("Failed to get property"); 
		}
		//System.out.println("dataFile = " + dataFile + "\n");
	}

	private void loadProperties(String fileName){
		String fileNamePath = dataDir.toString() + 
										"/" + fileName + ".properties";
		try (FileInputStream fis = new FileInputStream(fileNamePath)){
			properties.load(fis);
		} catch (FileNotFoundException fnfe){
			System.err.println("ERROR: failed to find properties file!");
		} catch (IOException ioe){
			System.err.println("ERROR: failed to load properties from file!");
		}
	}
	
	public static Environment getInstance(boolean isTest){
		if (instance == null) {
			instance = new Environment(isTest);
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
			createDataFile();
			ops = Operations.getInstance();			
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
			createDataFile();
			save(); 				// recursive calling
			
		}	
	}	

	private void createDataFile(){
		try {
			Files.createFile(dataFile);
		} catch (IOException e){
			System.err.println("ERROR: failed to create data file!");
		}
	}

	private void createAllFiles(){
		createDirectories();
		createPropertyFile(deafultFileName);
		createPropertyFile(testFileName);		
	}	

	private void createDirectories(){
		try{
			Files.createDirectories(dataDir);	
		} catch (IOException e){
			System.err.println("ERROR: failed to create data directories!");
		}
	}

	private void createPropertyFile(String type){
		String fileName = "";
		try {
			fileName = dataDir.toString() + "/" + type + ".properties";
			Path path = Paths.get(fileName);	
			Files.createFile(path);
		} catch (IOException e){
			System.err.println("ERROR: failed to create properties file!");
			return;
		}
		
		try (FileWriter fw = new FileWriter(fileName)){
			Properties pr = new Properties();
			pr.setProperty("data file", type);
			pr.store(fw, "Where data file is");
		} catch (IOException e){
			System.err.println("ERROR: failed to write to properties file!");
			return;
		}
	}	
}