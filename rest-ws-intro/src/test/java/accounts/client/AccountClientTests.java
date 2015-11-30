package accounts.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Random;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import rewardsonline.accounts.Account;
import rewardsonline.accounts.Beneficiary;
import rewardsonline.accounts.test.AccountTestConstants;

import common.money.Percentage;

public class AccountClientTests implements AccountTestConstants {

	/**
	 * Server URL ending with the servlet mapping on which the application can
	 * be reached. NOTE: if you use the TCP monitor, remember to change this
	 * port number.
	 */
	private static final String BASE_URL = "http://localhost:8080/rest-ws-intro/";

	private RestTemplate restTemplate = new RestTemplate();
	private Random random = new Random();

	@Test
	public void listAccounts() {
		// TODO-01: Deploy the application to the server.
		// Access the home page at http://localhost:8080/rest-ws-intro.
		// Remove any previously deployed applications from your server to
		//   allow for faster startup.
		// Leave the server running, all changes will be to the client (this
		//   test class)
		
		// Use the restTemplate to retrieve an array containing all Account
		//  instances. The URL needed can be created using BASE_URL above.
		//  Refer to the the [JSON] link on the web-application home page to
		//  see what the URL should be.
		Account[] accounts = null;

		assertNotNull(accounts);

		// There are 21 accounts originally, but if you can run this client
		// successfully there will be more.
		assertTrue(accounts.length >= 21);

		// First account is 123456789
		assertEquals(ACC_123456789, accounts[0].getNumber());
		assertEquals("Keith and Keri Donald", accounts[0].getName());
		assertEquals(2, accounts[0].getBeneficiaries().size());
		assertEquals(Percentage.valueOf("50%"),
				accounts[0].getBeneficiary("Annabelle")
						.getAllocationPercentage());
	}

	@Test
	public void getAccount() {
		// TODO-02: Use the restTemplate to retrieve the Account with number
		// 123456789 using a URI template - don't hard code the URL string.
		// Again refer to the [JSON] link on the accounts page of the
		// web-application to see what the URL should be.
		Account account = null;

		assertNotNull(account);
		assertEquals("Keith and Keri Donald", account.getName());
		assertEquals(2, account.getBeneficiaries().size());
		assertEquals(Percentage.valueOf("50%"),
				account.getBeneficiary("Annabelle").getAllocationPercentage());
	}

	@Test
	public void createAccount()  throws Exception {
		// Use a unique number to avoid conflicts
		String number = String.format("12345%4d", random.nextInt(10000));
		Account account = new Account(number, "John Doe");
		account.setCreditCardNumber("1111222233334444");
		account.setDateOfBirth(new SimpleDateFormat("dd MMM yyyy")
				.parse("22 May 1980"));
		account.setEmail("jdoe@somewhere.or.other.net");

		// TODO-03: Use the appropriate method on the RestTemplate to create a
		// new Account by POSTing it to /accounts and store the new location in
		// a variable

		// TODO-04: Retrieve the Account you just created from the location that
		// was returned (using restTemplate)
		Account retrievedAccount = null;

		assertNotNull(retrievedAccount);
		assertNotNull(retrievedAccount.getEntityId());
		assertEquals(account, retrievedAccount);
	}

	@Test
	public void addAndDeleteBeneficiary() {
		// perform both add and delete to avoid issues with side effects

		// TODO-05: Create a new Beneficiary called "David" for the account with
		// number 123456789 using a URI Template and store its location in a
		// variable (using restTemplate)

		// TODO-06: Retrieve the Beneficiary you just created from the location
		// that was returned (using restTemplate)
		Beneficiary newBeneficiary = null;

		assertNotNull(newBeneficiary);
		assertEquals("David", newBeneficiary.getName());

		// TODO-07: delete the new Beneficiary (using restTemplate)

		try {
			// TODO-08: try to retrieve the new Beneficiary again (using
			// restTemplate): this should give a 404 Not Found

			fail("Should have received 404 Not Found after deleting beneficiary");
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
		}
	}
}
