import static org.junit.Assert.*;
import java.io.*;
import org.junit.*;

public class MyParserTest {

	BufferedWriter bw;
	String output;

	private final ByteArrayOutputStream outContent =
			new ByteArrayOutputStream();

	@Before
	public void setUp() throws Exception {
		File outputFile = new File ("testFile.txt");
		FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		System.setOut(new PrintStream(outContent));

	}

	@After
	public void tearDown() throws Exception {
		Author.getTotalCoAuthors().clear();
	}

	@Test
	public void testNoCoAuthors() {

		try {
			bw.write(
			"http://scholar.google.ca/citations?user=LDDgpsMAAAAJ&hl=en&oi=ao");
			bw.close();

		} catch (IOException e) {
			fail("false URL");
		}

		String output =
				"------------------------------------------\n"+
				"i. Name of Author:\n" +
			    "\t"+ "Abbas Attarwala " + "\n" +
			    "ii. Number of All Citations:\n" +
			    "\t"+ "4" + "\n" +
			    "iii. Number of i10-index after 2008:\n" +
			    "\t"+ "0" + "\n" +
			    "iv. Title of the first three publications:\n" +
			    "\t1-\t"+ "Real time collaborative video annotation using "
			    		+ "Google App Engine and XMPP protocol" + "\n" +
			    "\t2-\t"+ "Accessible, large-print, listening &amp; talking"
			    		+ " e-book (ALLT)" + "\n" +
			    "\t3-\t"+ "Reading together as a Leisure Activity: "
			    		+ "Implications for E-reading" + "\n" +
			  	"v. Total paper citation of the first five papers:\n" +
			    "\t"+ "4" + "\n"+
			  	"vi. Total Co-Authors:\n" +
			    "\t"+ "0" + "\n"+
			    "------------------------------------------";

		MyParser.main(new String[] {"testFile.txt"});
		String s = outContent.toString();
		assertEquals(output, s);

	}

	@Test
	public void testCrossedCitations() {

		try {
			bw.write(
			"http://scholar.google.ca/citations?user=JicYPdAAAAAJ&hl=en&oi=ao");
			bw.close();

		} catch (IOException e) {
			fail("false URL");
		}

		String output =
				"------------------------------------------\n"+
				"i. Name of Author:\n" +
			    "\t"+ "Geoffrey Hinton " + "\n" +
			    "ii. Number of All Citations:\n" +
			    "\t"+ "69285" + "\n" +
			    "iii. Number of i10-index after 2008:\n" +
			    "\t"+ "164" + "\n" +
			    "iv. Title of the first three publications:\n" +
			    "\t1-\t"+ "Learning representations by "
			    		+ "back-propagating errors" + "\n" +
			    "\t2-\t"+ "Learning internal representations "
			    		+ "by error propagation" + "\n" +
			    "\t3-\t"+ "Learning internal representations "
			    		+ "by error propagation" + "\n" +
			  	"v. Total paper citation of the first five papers:\n" +
			    "\t"+ "71110" + "\n"+
			  	"vi. Total Co-Authors:\n" +
			    "\t"+ "12" + "\n"+
			    "------------------------------------------\n" +
			    "Abdel-rahman Mohamed\nChris Williams\nDavid C. "
			    + "Plaut\nDemetri Terzopoulos\nGeorge E. Dahl\nJames"
			    + " L. McClelland\nRadford Neal\n"+
			    "Rama Chellappa\nSidney Fels\nTerrence Sejnowski\n"
			    + "Vinod Nair\nrobert tibshirani";

		MyParser.main(new String[] {"testFile.txt"});
		String s = outContent.toString();
		assertEquals(output, s);

	}
}
