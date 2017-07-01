import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

public class Coverter {

	public static void main(String[] args) {

		/*
		 * Create JAVA POJO from JSON
		 * Sample JSON file in the project directory,namely, SampleJSON.txt
		 */

		Gson gson = new Gson();

		try(Reader reader = new FileReader(new File(System.getProperty("user.dir")+File.separator+"SampleJSON.txt")))
		{
			Identity identity = gson.fromJson(reader, Identity.class);
			System.out.println(identity.getPerson().getage());
		}
		catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println("Conversion of JAVA POJO from JSON object failed");
		}

		/*
		 * Create JSON from JAVA Objects
		 */

		/*
		 * Instantiate the object and set its parameters
		 */
		List<String> skillset = new ArrayList<>();
		Person person = new Person();
		person.setname("Arnab");
		person.setCity("Kolkata");
		person.setage(21);
		skillset.add("Selenium");
		skillset.add("Java");
		skillset.add("VBScript");
		person.setSkills(skillset);

		/*
		 * Convert the JAVA Object to JSON and store it in project directory
		 */
		try(FileWriter writer = new FileWriter(new File(System.getProperty("user.dir")+File.separator+"ResultantJSON_"+new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date())+".txt")))
		{
			String jsonline = gson.toJson(person);
			System.out.println(jsonline);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println("Conversion of JSON from JAVA object failed");
		}

	}

}
