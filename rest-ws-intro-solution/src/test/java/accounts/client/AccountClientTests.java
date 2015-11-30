package accounts.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
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
	 * be reached. 
	 *   NOTE: if you use the TCP monitor, remember to change this port number.
	 */
	protected static final String BASE_URL = "http://localhost:8080/rest-ws-intro-solution";

	private RestTemplate restTemplate = new RestTemplate();
	private Random random = new Random();

	@Test
	public void listAccounts() {
		String url = BASE_URL + "/accounts";
		// we have to use Account[] instead of List<Account>, or Jackson won't
		// know what type to unmarshal to
		Account[] accounts = restTemplate.getForObject(url, Account[].class);

		// Initially 21 accounts in database, but will go up if you rerun this test
		// multiple times
		assertTrue(accounts.length >= 21);
		assertEquals("Keith and Keri Donald", accounts[0].getName());
		assertEquals(2, accounts[0].getBeneficiaries().size());
		assertEquals(Percentage.valueOf("50%"),
				accounts[0].getBeneficiary("Annabelle")
						.getAllocationPercentage());
	}

	@Test
	public void getAccount() {
		String url = BASE_URL + "/accounts/{accountId}";
		Account account = restTemplate.getForObject(url, Account.class,
				ACC_123456789);
		assertEquals("Keith and Keri Donald", account.getName());
		assertEquals(2, account.getBeneficiaries().size());
		assertEquals(Percentage.valueOf("50%"),
				account.getBeneficiary("Annabelle").getAllocationPercentage());
	}

	@Test
	public void createAccount() throws Exception {
		String url = BASE_URL + "/accounts";
		// use a unique number to avoid conflicts
		String number = String.format("12345%4d", random.nextInt(10000));
		Account account = new Account(number, "John Doe");
		account.setCreditCardNumber("1111222233334444");
		account.setDateOfBirth(new SimpleDateFormat("dd MMM yyyy")
				.parse("22 May 1980"));
		account.setEmail("jdoe@somewhere.or.other.net");

		URI newAccountLocation = restTemplate.postForLocation(url, account);

		Account retrievedAccount = restTemplate.getForObject(
				newAccountLocation, Account.class);
		assertEquals(account, retrievedAccount);
		assertNotNull(retrievedAccount.getEntityId());
	}

	@Test
	public void addAndDeleteBeneficiary() {
		// perform both add and delete to avoid issues with side effects
		String addUrl = BASE_URL + "/accounts/{accountNumber}/beneficiaries";
		URI newBeneficiaryLocation = restTemplate.postForLocation(addUrl,
				"David", ACC_123456789);
		Beneficiary newBeneficiary = restTemplate.getForObject(
				newBeneficiaryLocation, Beneficiary.class);
		assertEquals("David", newBeneficiary.getName());

		restTemplate.delete(newBeneficiaryLocation);
		try {
			restTemplate
					.getForObject(newBeneficiaryLocation, Beneficiary.class);
			fail("Should have received 404 Not Found after deleting beneficiary at "
					+ newBeneficiaryLocation);
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
		}
	}
}
