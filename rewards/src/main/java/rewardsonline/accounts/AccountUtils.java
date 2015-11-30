package rewardsonline.accounts;

public class AccountUtils {

	/**
	 * Convert an account to a String representation - not very beautiful,
	 * intended for debugging and testing.
	 * 
	 * @param account
	 *            An account.
	 * @return Account details string.
	 */
	public static String toString(Account account) {
		return "Account [ number='" + account.getNumber() + "', name='"
				+ account.getName() + "', dob='" + account.getDateOfBirth()
				+ "', email='" + account.getEmail() + "', monthly-email='"
				+ (account.isReceiveMonthlyEmailUpdate() ? "yes" : "no")
				+ "', newsletter='"
				+ (account.isReceiveNewsletter() ? "yes" : "no")
				+ "']\n  Beneficiaries = " + account.getBeneficiaries();
	}

	/**
	 * Generate a unique hash for an account. This long, tedious code is placed
	 * here to keep the Account simpler.
	 * 
	 * @param account
	 *            An account.
	 * 
	 * @return The computed hash-value.
	 */
	public static int hashCode(Account account) {
		final int prime = 31;
		int result = 1;

		result = prime
				* result
				+ ((account.getBeneficiaries() == null) ? 0 : account
						.getBeneficiaries().size());

		result = prime
				* result
				+ ((account.getCreditCardNumber() == null) ? 0 : account
						.getCreditCardNumber().hashCode());

		result = prime
				* result
				+ ((account.getDateOfBirth() == null) ? 0 : account
						.getDateOfBirth().hashCode());

		result = prime
				* result
				+ ((account.getEmail() == null) ? 0 : account.getEmail()
						.hashCode());

		result = prime
				* result
				+ ((account.getEntityId() == null) ? 0 : account.getEntityId()
						.hashCode());

		result = prime
				* result
				+ ((account.getName() == null) ? 0 : account.getName()
						.hashCode());

		result = prime
				* result
				+ ((account.getNumber() == null) ? 0 : account.getNumber()
						.hashCode());

		result = prime * result
				+ (account.isReceiveMonthlyEmailUpdate() ? 1231 : 1237);

		result = prime * result + (account.isReceiveNewsletter() ? 1231 : 1237);
		return result;
	}

	/**
	 * Compare two accounts to see if they are the same. This long, tedious code
	 * is placed here to keep the Account simpler.
	 * 
	 * @param account
	 *            An account.
	 * @param obj
	 *            Some other object.
	 * @return Are they the same or not?
	 */
	public static boolean equals(Account account, Object obj) {
		if (account == obj)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof Account))
			return false;

		Account other = (Account) obj;

		if (account.getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!account.getName().equals(other.getName()))
			return false;

		if (account.getNumber() == null) {
			if (other.getNumber() != null)
				return false;
		} else if (!account.getNumber().equals(other.getNumber()))
			return false;

		if (account.getCreditCardNumber() == null) {
			if (other.getCreditCardNumber() != null)
				return false;
		} else if (!account.getCreditCardNumber().equals(
				other.getCreditCardNumber()))
			return false;

		if (account.getDateOfBirth() == null) {
			if (other.getDateOfBirth() != null)
				return false;
		} else if (!account.getDateOfBirth().equals(other.getDateOfBirth()))
			return false;

		if (account.getEmail() == null) {
			if (other.getEmail() != null)
				return false;
		} else if (!account.getEmail().equals(other.getEmail()))
			return false;

		if (account.isReceiveMonthlyEmailUpdate() != other
				.isReceiveMonthlyEmailUpdate())
			return false;

		if (account.isReceiveNewsletter() != other.isReceiveNewsletter())
			return false;

		if (account.getBeneficiaries() == null) {
			if (other.getBeneficiaries() != null)
				return false;
		} else if (!account.getBeneficiaries().equals(other.getBeneficiaries()))
			return false;

		return true;
	}

	private AccountUtils() {
	}

}
