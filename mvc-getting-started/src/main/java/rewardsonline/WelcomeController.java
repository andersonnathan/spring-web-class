package rewardsonline;


/**
 * Handles requests for the application welcome page.
 */
// TODO-01: Deploy the application (right-click on the project folder, select
//          Run As -> Run on Server).  If you aren't sure how to do this,
//          ask your teacher now.  STS should pop up a browser tab open at
//          the home page, but if not, go to this URL:
//                        http://localhost:8080/mvc-getting-started
//          You should get a 404 page-not-found error
//          Continue with the following steps to fix the problem ...
//
//          NOTE: Once you have run the server the first time, you can
//            start and stop it using the green Go and red Stop buttons
//            in the Servers window.

// TODO-02: Annotate this as an MVC Controller
public class WelcomeController {
	
	// TODO-03: Create a method to handle welcome requests
	//   The method needs annotating to respond to URLs ending in /home
	//   The page to display is welcome.jsp. Use CTRL-R to find it.
	//   Test it works and you don't get 404 any more.

	public String welcome() {
		return "";    // Return the correct view name
	}
}
