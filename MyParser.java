import java.io.*;
import java.net.*;

/**
 * The <code>MyParser</code> class represents  a web crawler for Google
 * Scholar Pages. Given an input file, it extracts information from a
 * given Google Scholar web page
 *
 * @author Bryan de Bourbon
 * @version 1.0
 * @see java.io.*
 * @see java.net.*
 */
public class MyParser {

	/**
 	 * Returns html contents of the given URL as a String
 	 *
 	 * @param inputFileLine a URL link from a Google Scholar Author page
 	 * @return the html contained within the webpage of the profile
 	 * @throws MalformedURLException
 	 * @throws IOException
 	 */
	public  String getURLProflle(String inputFileLine)
			throws MalformedURLException, IOException {

		URL articleUrl = new URL(inputFileLine);
		URLConnection connection = articleUrl.openConnection();
		BufferedReader urlBr =
		    new BufferedReader(new InputStreamReader(
		    			connection.getInputStream()));

		String webLine;
		String profile = "";
		while((webLine = urlBr.readLine()) != null){
			profile += webLine + "\n";
		}
		return profile;
	}

	/**
	 * Returns a string updated with the all co-authors of all the
	 * URL authors added.
	 *
	 * @param output the user message containing all single author info
	 * @return a String containing the co-authors of all authors
	 */
	public  String addCoAuthors(String output) {

		output += "------------------------------------------\n"+
				  "vii. Co-author list sorted (Total:"+
				  Author.getTotalCoAuthors().size()+"):";

		for (Object author: Author.getTotalCoAuthors().toArray()){
			output += "\n" + author;
		}

		return output;
	}

	/**
	 * Appropriately selects the correct output stream. Based on the user
	 * input will create a file with output contents or print output contents
	 *
	 * @param args the arguments of the main function
	 * @param output the programs message to the user
	 * @throws IOException
	 */
	public  void formatOutput(String[] args, String output)
		throws IOException {

		if (args.length  == 2){
			File outputFile = new File (args[1]);
			FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(output);
			bw.close();

		} else{
			System.out.print(output);
		}

	}
	public static void main(String[] args) {

		MyParser parser = new MyParser();

		try {

			FileInputStream fileStream = new FileInputStream(args[0]);
			BufferedReader fileBr = new BufferedReader(
									new InputStreamReader(fileStream));

			String inputFileLine;
			String output = "";
			while ((inputFileLine = fileBr.readLine()) != null) {

				//get the html profile and create an author object with it
				String profile = parser.getURLProflle(inputFileLine);
				Author author = new Author(profile);
				output += "------------------------------------------\n" +
						author.toString();
			}

			//add the final section (vii.) - all co-authors
			output = parser.addCoAuthors(output);
			parser.formatOutput(args, output);

			fileStream.close();
	        fileBr.close();

		} catch (Exception e) {
			System.out.println("Something went wrong! Please try again!");
		}

	}

}
