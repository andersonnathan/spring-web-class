package accounts.client;

import org.junit.Before;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

import java.io.File;

/**
 * A self-contained version of the {@link AccountClientTests} that runs up
 * Tomcat automatically before the tests start. Uses Spring Boot to run Tomcat
 * in embedded mode and to configure the Spring MVC.
 * <p>
 * Because we are not running as a WAR, the resources in WEB-INF have to be
 * referenced explicitly as files.
 * <p>
 * Note that this code may not work in IntelliJ, depending on what is its
 * current directory.  The code below outputs the current directory and
 * you will need to modify the file: resources to be relative to that
 * directory.
 */
@ImportResource({ "file:src/main/webapp/WEB-INF/spring/app-config.xml",
		"file:src/main/webapp/WEB-INF/spring/mvc-config.xml" })
@EnableAutoConfiguration
public class SpringBootAccountClientTests extends AccountClientTests {

	private static boolean startServer = true;

	@Before
	public void startTomcat() {
		if (startServer) {
			// Current directory:
			//  - This lab's project directory in STS/Eclipse
			//  - Workspce directory in IntelliJ IDEA
			System.out.println("Current dir = " + new File(".").getAbsoluteFile());

			String[] urlBits = BASE_URL.split("8080");
			System.setProperty("server.contextPath", urlBits[1]);
			SpringApplication.run(SpringBootAccountClientTests.class, new String[0]);
			startServer = false;
		}
	}

}
