package rewardsonline.rewards.newreward;

import java.io.Serializable;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;

import rewards.Dining;

import common.datetime.SimpleDate;
import common.money.MonetaryAmount;

/**
 * Backing bean holding values for the "Dining Form" that must be completed to create a new reward.
 */
public class DiningForm implements Serializable {

	private static final long serialVersionUID = 1933107105727203303L;

	private String creditCardNumber;

	private String merchantNumber;

	private MonetaryAmount amount;

	private SimpleDate date;
	
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
		return new Dining(getAmount(), getCreditCardNumber(), getMerchantNumber(), getDate());
	}
	
	/**
	 * Validates the dining information entered in the <code>enterDiningInformation</code> state 
	 * of the <code>newReward</code> flow.
	 * @param messageContext a message context that allows you to build validation error messages
	 */
	public void validateEnterDiningInformation(MessageContext messageContext){
		if (StringUtils.hasText(creditCardNumber)){
			if (creditCardNumber.length() != 16){
				messageContext.addMessage(new MessageBuilder().error()
						.source("creditCardNumber")
						.code("error.invalidFormat.DiningForm.creditCardNumber").build());
			}
		}
	}
	
	public String toString() {
		return "[DiningForm creditCardNumber = '" + creditCardNumber + "', merchantNumber = '" + merchantNumber + "', amount = " + amount + ", date = " + date + "]";
	}
}
