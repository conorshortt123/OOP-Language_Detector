package ie.gmit.sw;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author Conor Shortt
 * @version 1.0
 * @since 1.8
 *
 * Menu class for the project. This class contains a simple menu inside a while loop.
 * The Menu allows user to input location of Subject and Query files.
 * The Menu runs the {@link Parser}, creates {@link Thread}s.
 */
public class Menu {
	
	private String subjectLocation;
	private String queryLocation;
	private int k = 4;
	private String choice;
	
	/**
	 * Runs the printHeader and printMenu methods.
	 * @throws Throwable
	 */
	public void runMenu() throws Throwable {
		printHeader();
		printMenu();
	}
	
	/**
	 * Prints out the header of the menu.
	 */
	private void printHeader(){
		System.out.println("***************************************************");
		System.out.println("* GMIT - Dept. Computer Science & Applied Physics *");
		System.out.println("*                                                 *");
		System.out.println("*             Text Language Detector              *");
		System.out.println("*                                                 *");
		System.out.println("***************************************************");
	}
	
	/**
	 * printMenu allows user input of subject and query file location.
	 * It also calculates run time of the program.
	 * Creates the {@link Database} and {@link Thread}s
	 * @throws Throwable
	 */
	private void printMenu() throws Throwable {
		
		Scanner input = new Scanner(System.in);
		
		System.out.print("Enter WiLi Data Location>");
		subjectLocation = input.next();
		
		System.out.print("Enter Query File Location>");
		queryLocation = input.next();
		
		System.out.print("Do you wish to enter a specific number of kmers? (Y/N)>");
		choice = input.next();
		
		if(choice.equalsIgnoreCase("y")) {
			System.out.println("Enter 'k' in kmers:");
			k = input.nextInt();
		} else if (choice.equalsIgnoreCase("n")) {
			System.out.println("\nYou chose no, defaulting to k = 4...");
		} else {
			System.out.println("Invalid choice. Try again.");
			System.out.print("Do you wish to enter a specific number of kmers? (Y/N)>");
			choice = input.next();
		}
		
		System.out.println("\nProcessing query... Please wait...\n");
		
		long start = System.currentTimeMillis();

		Parser p = new Parser(subjectLocation, k);
		
		Database db = new Database();
		p.setDb(db);
		Thread t = new Thread(p);
		t.start();
		t.join();

		db.resize(300);

		p.readQueryFile(queryLocation);

		long end = System.currentTimeMillis();
		NumberFormat formatter = new DecimalFormat("#0.00000");
		System.out.println("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");
		
		System.out.print("\nDo you wish to enter another query file?(Y/N)>");
		choice = input.next();
		while(choice.equalsIgnoreCase("y")) {
			System.out.print("\nEnter the query file location you wish to parse>");
			queryLocation = input.next();
			System.out.println("Processing query.. Please wait...");
			p.readQueryFile(queryLocation);
			
			System.out.print("\nDo you wish to enter another query file?(Y/N)>");
			choice = input.next();
		}
		if(choice.equalsIgnoreCase("n")) {
			System.out.println("Thank you for using the Multi-Threaded Language Detector.");
		}
		
		input.close();
	}
	
}
