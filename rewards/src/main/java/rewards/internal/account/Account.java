package rewards.internal.account;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import rewards.AccountContribution;
import rewards.AccountContribution.Distribution;

import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * An account for a member of the reward network. An account has one or more
 * beneficiaries whose allocations must add up to 100%. An aggregate entity.
 * 
 * An account can make contributions to its beneficiaries. Each contribution is
 * distributed among the beneficiaries based on an allocation.
 */
@Entity
@Table(name = "T_ACCOUNT")
public class Account {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer entityId;

	private String number;

	@NotEmpty
	private String name;

	@Column(name = "CREDIT_CARD")
	private String creditCardNumber;

	@Column(name = "DATE_OF_BIRTH")
	@NotNull
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dateOfBirth;

	@Pattern(regexp="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")
	private String email;

	@Column(name = "REWARDS_NEWSLETTER")
	private boolean receiveNewsletter;

	@Column(name = "MONTHLY_EMAIL_UPDATE")
	private boolean receiveMonthlyEmailUpdate;

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name = "ACCOUNT_ID")
	private Set<Beneficiary> beneficiaries = new LinkedHashSet<Beneficiary>();

	/**
	 * Required by JPA.
	 */
	protected Account() {
	}

	/**
	 * Create a new account.
	 * 
	 * @param number
	 *            the account number
	 * @param name
	 *            the name on the account
	 */
	public Account(String number, String name) {
		this.number = number;
		this.name = name;
	}

	/**
	 * Returns the entity identifier used to internally distinguish this entity
	 * among other entities of the same type in the system. Should typically
	 * only be called by privileged data access infrastructure code such as an
	 * Object Relational Mapper (ORM) and not by application code.
	 * 
	 * @return the internal entity identifier
	 */
	public Integer getEntityId() {
		return entityId;
	}

	/**
	 * Sets the internal entity identifier - should only be called by privileged
	 * data access code (repositories that work with an Object Relational Mapper
	 * (ORM)). Should never be set by application code explicitly.
	 * 
	 * @param entityId
	 *            the internal entity identifier
	 */
	protected void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	/**
	 * Returns the number used to uniquely identify this account.
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * Sets the number used to uniquely identify this account.
	 * 
	 * @param number
	 *            The number for this account
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * Returns the name on file for this account.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name on file for this account.
	 * 
	 * @param name
	 *            The name for this account
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Removes a single beneficiary from this account.
	 * 
	 * @param beneficiaryName
	 *            the name of the beneficiary (should be unique)
	 */
	public void removeBeneficiary(String beneficiaryName) {
		Beneficiary old = getBeneficiary(beneficiaryName);
		beneficiaries.remove(old);
		
		if (old.getAllocationPercentage().equals(Percentage.zero()))
			return;  // nothing to do

		// Rebalance percentages
		int nBeneficiaries = beneficiaries.size();
		Percentage allocation = old.getAllocationPercentage();
		Percentage extra = new Percentage(allocation.asBigDecimal().divide(new BigDecimal(nBeneficiaries)));
		MonetaryAmount savings = old.getSavings();
		MonetaryAmount dist = savings.divideBy(new BigDecimal(nBeneficiaries));
		Percentage totalAllcation = Percentage.zero();
		
		Beneficiary last = null;
		
		for (Beneficiary b2 : beneficiaries) {
			b2.credit(dist);
			b2.getAllocationPercentage().add(extra);
			totalAllcation.add(extra);
			last = b2;
		}
		
		Percentage ONE_HUNDRED = Percentage.oneHundred();
		
		if (!totalAllcation.equals(ONE_HUNDRED)) {
			BigDecimal diff = ONE_HUNDRED.asBigDecimal().subtract(totalAllcation.asBigDecimal());
			last.getAllocationPercentage().add(new Percentage(diff));
		}
		
		assert(isValid());
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isReceiveNewsletter() {
		return receiveNewsletter;
	}

	public void setReceiveNewsletter(boolean receiveNewsletter) {
		this.receiveNewsletter = receiveNewsletter;
	}

	public boolean isReceiveMonthlyEmailUpdate() {
		return receiveMonthlyEmailUpdate;
	}

	public void setReceiveMonthlyEmailUpdate(boolean receiveMonthlyEmailUpdate) {
		this.receiveMonthlyEmailUpdate = receiveMonthlyEmailUpdate;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	/**
	 * Make a monetary contribution to this account. The contribution amount is
	 * distributed among the account's beneficiaries based on each beneficiary's
	 * allocation percentage.
	 * 
	 * @param amount
	 *            the total amount to contribute
	 * @param contribution
	 *            the contribution summary
	 */
	public AccountContribution makeContribution(MonetaryAmount amount) {
		if (!isValid()) {
			throw new IllegalStateException(
					"Cannot make contributions to this account: it has invalid beneficiary allocations");
		}
		Set<Distribution> distributions = distribute(amount);
		return new AccountContribution(getNumber(), amount, distributions);
	}

	/**
	 * Distribute the contribution amount among this account's beneficiaries.
	 * 
	 * @param amount
	 *            the total contribution amount
	 * @return the individual beneficiary distributions
	 */
	private Set<Distribution> distribute(MonetaryAmount amount) {
		Set<Distribution> distributions = new HashSet<Distribution>(
				beneficiaries.size());
		for (Beneficiary beneficiary : beneficiaries) {
			MonetaryAmount distributionAmount = amount.multiplyBy(beneficiary
					.getAllocationPercentage());
			beneficiary.credit(distributionAmount);
			Distribution distribution = new Distribution(beneficiary.getName(),
					distributionAmount, beneficiary.getAllocationPercentage(),
					beneficiary.getSavings());
			distributions.add(distribution);
		}
		return distributions;
	}

	/**
	 * Add a single beneficiary with a 100% allocation percentage.
	 * 
	 * @param beneficiaryName
	 *            the name of the beneficiary (should be unique)
	 */
	public void addBeneficiary(String beneficiaryName) {
		addBeneficiary(beneficiaryName, Percentage.oneHundred());
	}

	/**
	 * Add a single beneficiary with the specified allocation percentage.
	 * 
	 * @param beneficiaryName
	 *            the name of the beneficiary (should be unique)
	 * @param allocationPercentage
	 *            the beneficiary's allocation percentage within this account
	 */
	public void addBeneficiary(String beneficiaryName,
			Percentage allocationPercentage) {
		beneficiaries
				.add(new Beneficiary(beneficiaryName, allocationPercentage));
	}

	/**
	 * Returns the beneficiaries for this account.
	 * <p>
	 * Callers should not attempt to hold on or modify the returned set. This
	 * method should only be used transitively; for example, called to
	 * facilitate account reporting.
	 * 
	 * @return the beneficiaries of this account
	 */
	public Set<Beneficiary> getBeneficiaries() {
		return Collections.unmodifiableSet(beneficiaries);
	}

	/**
	 * Returns a single account beneficiary. Callers should not attempt to hold
	 * on or modify the returned object. This method should only be used
	 * transitively; for example, called to facilitate reporting or testing.
	 * 
	 * @param name
	 *            the name of the beneficiary e.g "Annabelle"
	 * @return the beneficiary object
	 */
	public Beneficiary getBeneficiary(String name) {
		for (Beneficiary b : beneficiaries) {
			if (b.getName().equals(name)) {
				return b;
			}
		}
		throw new IllegalArgumentException("No such beneficiary with name '"
				+ name + "'");
	}

	public void setBeneficiaries(Set<Beneficiary> beneficiaries) {
		this.beneficiaries = beneficiaries;
	}

	/**
	 * Validation check that returns true only if the total beneficiary
	 * allocation adds up to 100%.
	 */
	public boolean isValid() {
		Percentage totalPercentage = Percentage.zero();
		for (Beneficiary b : beneficiaries) {
			try {
				totalPercentage = totalPercentage.add(b
						.getAllocationPercentage());
			} catch (IllegalArgumentException e) {
				// total would have been over 100% - return invalid
				return false;
			}
		}
		if (totalPercentage.equals(Percentage.oneHundred())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Convert an account to a String representation - not very beautiful,
	 * intended for debugging and testing.
	 * 
	 * @param account
	 *            An account.
	 * @return Account details string.
	 */
	public String toString() {
		return "Account [ number='" + this.getNumber() + "', name='"
				+ this.getName() + "', dob='" + this.getDateOfBirth()
				+ "', email='" + this.getEmail() + "', monthly-email='"
				+ (this.isReceiveMonthlyEmailUpdate() ? "yes" : "no")
				+ "', newsletter='"
				+ (this.isReceiveNewsletter() ? "yes" : "no")
				+ "']\n  Beneficiaries = " + this.getBeneficiaries();
	}

}