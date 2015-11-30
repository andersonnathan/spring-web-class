package rewardsonline.rewards.newreward;

import java.io.Serializable;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.util.StringUtils;

import rewards.Dining;

import common.datetime.SimpleDate;
import common.money.MonetaryAmount;

/**
 * Backing bean holding values for the "Dining Form" that must be completed to
 * create a new reward.
 */
public class DiningForm implements Serializable {

	private static final long serialVersionUID = -8408311399052530092L;

	private String creditCardNumber;

	private String merchantNumber;

	private MonetaryAmount amount;

	private SimpleDate date;

	private boolean oneClickReward;
	
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

	public boolean isOneClickReward() {
		return oneClickReward;
	}

	public void setOneClickReward(boolean oneClickReward) {
		this.oneClickReward = oneClickReward;
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
	 *            a validation context that allows you to add validation error
	 *            messages
	 */
	public void validateEnterDiningInformation(ValidationContext context) {
		if (StringUtils.hasText(creditCardNumber)) {
			if (creditCardNumber.length() != 16) {
				context
						.getMessageContext()
						.addMessage(
								new MessageBuilder()
										.error()
										.source("creditCardNumber")
										.code(
												"error.invalidFormat.DiningForm.creditCardNumber")
										.build());
			}
		}
	}

	public String toString() {
		return "[DiningForm creditCardNumber = '" + creditCardNumber
				+ "', merchantNumber = '" + merchantNumber + "', amount = "
				+ amount + ", date = " + date + "]";
	}
}
