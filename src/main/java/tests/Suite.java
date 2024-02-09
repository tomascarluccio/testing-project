package tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

import configs.Config;
import utils.TestNGXMLBuilder;

public class Suite {

	public static void main(String[] args) throws IOException, URISyntaxException, Exception {
		List<String> testSuites = new ArrayList<String>();

		if (args != null) {
			for(String arg :args){
					if (arg.contains(".properties")) {
						Config.configPath = new File(args[0]).toURI().toURL();
					}
					if(arg.contains("v5")){
						Config.safewalkMode = "v5";
					}
					if(arg.contains("mt")){
						Config.safewalkMode = "mt";
					}
			}
		}

		Config.load();
		String runXML =  TestNGXMLBuilder.getTestSuiteXML(args);
		
		testSuites.add(createXMLFile( runXML, Config.testngPath));
		TestNG runner=new TestNG();
		runner.setTestSuites(testSuites);
		
		runner.run();
		System.exit(0);
	}
		
	private static String createXMLFile(String xmlString, String filePath) {
		File xml = new File(filePath) ;
		
        try (FileWriter fileWriter = new FileWriter(new File(filePath))) {
            // Write the XML string to the file
            fileWriter.write(xmlString);
            return xml.getPath(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
    }
	
}
