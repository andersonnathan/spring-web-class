package rewards.internal.restaurant;

import rewards.Dining;
import rewards.internal.account.Account;

/**
 * Creates different benefit availability policies. In this system they are
 * limited to always or never available. Other policies would be used in a real
 * system - such as varying reward percentages with day or week or time of
 * month.
 */
public class BenefitAvailabilityPolicyFactory {

	private BenefitAvailabilityPolicyFactory() {
	}

	public static BenefitAvailabilityPolicy getPolicy(char code) {
		if (code == 'A')
			return AlwaysAvailable.INSTANCE;
		if (code == 'N')
			return NeverAvailable.INSTANCE;
		throw new IllegalArgumentException(
				"Unrecognised BenefitAvailabilityPolicy code: " + code);
	}

	/**
	 * Returns true indicating benefit is always available.
	 */
	protected static class AlwaysAvailable implements BenefitAvailabilityPolicy {
		public static final BenefitAvailabilityPolicy INSTANCE = new AlwaysAvailable();

		public boolean isBenefitAvailableFor(Account account, Dining dining) {
			return true;
		}

		@Override
		public char code() {
			return 'A';
		}

		@Override
		public String toString() {
			return "alwaysAvailable";
		}
	}

	/**
	 * Returns false indicating benefit is never available.
	 */
	protected static class NeverAvailable implements BenefitAvailabilityPolicy {
		public static final BenefitAvailabilityPolicy INSTANCE = new NeverAvailable();

		public boolean isBenefitAvailableFor(Account account, Dining dining) {
			return false;
		}

		@Override
		public char code() {
			return 'N';
		}

		@Override
		public String toString() {
			return "neverAvailable";
		}
	}
}
