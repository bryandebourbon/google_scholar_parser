import java.util.*;
import java.util.Map.Entry;
import java.util.regex.*;



/**
 * The <code>Author</code> class represents authors from Google Scholar
 * Pages. It finds, stores and organizes information from its profile.
 *
 * @author Bryan de Bourbon
 * @version 1.0
 * @see java.util.TreeMap
 */
public class Author {

	private String profile = "";
	private TreeMap <String, ArrayList<String>> authorInfo =
			new TreeMap<String, ArrayList<String>>();
	private static TreeSet <String> totalCoAuthors =
			new TreeSet<String>();


	/**
	 * Creates an Author Object that represents an author from Google Scholar.
	 * An Author knows its name, number of citations, i10index (after 2008),
	 * some publications, citations and all co-authors.
	 *
	 * @param profile the html profile of the author
	 */
	public Author(String profile){

		this.profile = profile;

		authorInfo.put("i. Name of Author:",
				new ArrayList<String>(Arrays.asList(
				"(<title>)(.+?)- Google Scholar Citations", "1")));
		authorInfo.put("ii. Number of All Citations:",
				new ArrayList<String>(Arrays.asList(
				"(>Citations</a></td><td class=\"cit-borderleft"
				+ " cit-data\">)([0-9]+)</td>", "1")));
		authorInfo.put("iii. Number of i10-index after 2008:",
				new ArrayList<String>(Arrays.asList(
				"(i10-index</a></td><td class=\"cit-borderleft "+
				"cit-data\">[0-9]+</td><td class=\"cit-borderleft"+
				" cit-data\">)([0-9]+)</td>", "1")));
		authorInfo.put("iv. Title of the first three publications:",
				new ArrayList<String>(Arrays.asList(
				"(class=\"cit-dark-large-link\">)(.+?)</a>", "3")));
		authorInfo.put("v. Total paper citation of the first five papers:",
				new ArrayList<String>(Arrays.asList(
				"[^ ]+(>|;)([0-9]+?)(</a>|&)", "5")));
		authorInfo.put("vi. Total Co-Authors:",
				new ArrayList<String>(Arrays.asList(
				"(title=\")((.{1,21})((.+?)|(\\.\\.\\.)))\">\\3", "-1")));

		this.findInfo();
		this.addToTotal();

	}

	/**
	 * Returns a collection of all the sorted, distinct co-authors of the
	 * Authors that exist.
	 *
	 * @return the collection of sorted, distinct co-authors
	 */
	public static TreeSet<String> getTotalCoAuthors() {
		return totalCoAuthors;
	}

	/**
	 * Based on the given pattern and upperBound from the ArrayList,
	 * finds upperBound instances of the pattern, storing the matches
	 *
	 */
	public void findInfo(){

		for (ArrayList<String> value : authorInfo.values()) {

			Pattern authorPattern = Pattern.compile(value.get(0));
			Matcher authorMatcher = authorPattern.matcher(
													this.profile);

			//indicates the upper bound on the amount information that
			//the user wants to receive (ex. 5 as 5 authors)
			int upperBound = new Integer(value.get(1));

			//as stated in the JavaDoc, -1 is the marker for no bound
			if (upperBound == -1){
				upperBound = Integer.MAX_VALUE;
			}

			int i = 0;
			while (authorMatcher.find() && i < upperBound){
				value.add(authorMatcher.group(2));
				i++;
			}

		}
	}

	/**
	 * Updates the total collection of CoAuthors with those found from the
	 * new author
	 */
	public void addToTotal(){
		ArrayList<String>  authors = authorInfo.get("vi. Total Co-Authors:");

		for (int i = 2; i < authors.size(); i++){

			totalCoAuthors.add(authors.get(i));

		}
	}

	/**
	 * Appropriately formats the author information to standardize it.
	 *
	 * @param result
	 * @return String formatted author information
	 */
	public String formatResult(List<String> result){

		String output = "";


		if (result.get(1) == "-1") {
				output += "\t" + (result.size() - 2) + "\n";

		} else if (result.size() == 3) {
			output += "\t"+ result.get(2) + "\n";

		} else if (result.get(3).matches("[0-9]+")) {

			int num = 0;
			for (int i = 2; i < result.size(); i++ ){
				num +=  new Integer(result.get(i));
			}
			output += "\t" + num + "\n";

		} else {

			for (int i = 2; i < result.size(); i++ ){
				output += "\t"+ (i-1) +"-\t"+ result.get(i) + "\n";
			}
		}
		return output;

	}


	/**
	 * (note implicit inheritance)
	 */
	public String toString(){
		String output = "";

		for (Entry<String,ArrayList<String>> entry : authorInfo.entrySet()){

			String key = entry.getKey();

			ArrayList<String> value = entry.getValue();

			output +=
				key + "\n" + this.formatResult(value);
		}
		return output;
	}

}
