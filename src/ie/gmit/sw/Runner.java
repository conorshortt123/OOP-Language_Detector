package ie.gmit.sw;

/**
 * @author Conor Shortt
 * @version 1.0
 * @since 1.8
 * 
 * A simple Runner class for the program.
 * Creates a Menu object and then calls the runMenu method.
 */
public class Runner {

	/**
	 * @param args
	 * @throws Throwable
	 */
	public static void main(String[] args) throws Throwable {

		Menu menu = new Menu();
		menu.runMenu();

	}

}
