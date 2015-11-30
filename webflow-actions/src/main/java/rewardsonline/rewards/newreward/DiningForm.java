package rewardsonline.rewards.newreward;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.util.StringUtils;

import rewards.Dining;
import rewards.InvalidCreditCardException;
import rewardsonline.accounts.AccountManager;

import common.datetime.SimpleDate;
import common.money.MonetaryAmount;

/**
 * Backing bean holding values for the "Dining Form" that must be completed to
 * create a new reward.
 */
public class DiningForm implements Serializable {

	private static final long serialVersionUID = -1472633557308461762L;

	private String creditCardNumber;

	private String merchantNumber;

	private MonetaryAmount amount;

	private SimpleDate date;

	private transient AccountManager accountManager;

	/**
	 * Using a setter so Webflow can reset this each time a new state is
	 * entered.
	 * 
	 * @param accountManager
	 */
	@Autowired
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getMerchantNumber() {
		return merchantNumber;
	}

	public void setMerchantNumber(String merchantNumber) {
		this.merchantNumber = merchantNumber;
	}

	public MonetaryAmount getAmount() {
		return amount;
	}

	public void setAmount(MonetaryAmount amount) {
		this.amount = amount;
	}

	public SimpleDate getDate() {
		return date;
	}

	public void setDate(SimpleDate date) {
		this.date = date;
	}

	public Dining createDining() {
		return new Dining(getAmount(), getCreditCardNumber(),
				getMerchantNumber(), getDate());
	}

	/**
	 * Validates the dining information entered in the
	 * <code>enterDiningInformation</code> state of the <code>newReward</code>
	 * flow.
	 * 
	 * @param context
	 *            A validation context that allows you to add validation error
	 *            messages
	 */
	public void validateEnterDiningInformation(ValidationContext context) {
		// TODO-12: Validate the credit-card number.
		//   It must be 16 digits and there must be an account associated with
		//     this credit-card. In either case the error to fail with should
		//     use message-code:
		//            error.invalidFormat.DiningForm.creditCardNumber.
		//   The message code already exists in messages.properties.
		//   The accountManager has been injected into this class - it can be used
		//     to check for a corresponding account.
		//
		//   Try creating a new reward with a bogus credit-card number.
	}

	public String toString() {
		return "[DiningForm creditCardNumber = '" + creditCardNumber
				+ "', merchantNumber = '" + merchantNumber + "', amount = "
				+ amount + ", date = " + date + "]";
	}
}
