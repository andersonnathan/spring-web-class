package rewards.internal.account;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * A single beneficiary allocated to an account. Each beneficiary has a name
 * (e.g. Annabelle) and a savings balance tracking how much money has been saved
 * for he or she to date (e.g. $1000.00). Scoped by the Account aggregate.
 */
@Entity
@Table(name = "T_ACCOUNT_BENEFICIARY")
public class Beneficiary {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer entityId;

	@Column(name = "NAME")
	private String name;

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "ALLOCATION_PERCENTAGE"))
	private Percentage allocationPercentage;

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "SAVINGS"))
	private MonetaryAmount savings = MonetaryAmount.valueOf("0.00");

	@SuppressWarnings("unused")
	private Beneficiary() {
	}

	public Beneficiary(String beneficiaryName, Percentage allocationPercentage,
			MonetaryAmount savings) {
		this.name = beneficiaryName;
		this.allocationPercentage = allocationPercentage;
		this.savings = savings;
	}

	public Beneficiary(String beneficiaryName, Percentage allocationPercentage) {
		this.name = beneficiaryName;
		this.allocationPercentage = allocationPercentage;
	}

	/**
	 * Allow access to entity id. Needed to initialise DTOs.
	 * 
	 * @return The id, if it exists, or null otherwise.
	 */
	public Integer getEntityId() {
		return entityId;
	}

	/**
	 * Allow protected coded to set entity id.
	 * 
	 * @param id
	 *            Internal id (unique key) for this beneficiary.
	 */
	protected void setEntityId(int id) {
		entityId = id;
	}

	/**
	 * Returns the name of the beneficiary.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the percentage this beneficiary has been allocated vs other
	 * beneficiaries of the same account.
	 * 
	 * @return the allocation percentage
	 */
	public Percentage getAllocationPercentage() {
		return allocationPercentage;
	}

	/**
	 * Returns the total savings accrued by this beneficiary.
	 * 
	 * @return the total savings
	 */
	public MonetaryAmount getSavings() {
		return savings;
	}

	/**
	 * Credit the amount to this beneficiary's saving balance.
	 * 
	 * @param amount
	 *            the amount to credit
	 */
	public void credit(MonetaryAmount amount) {
		savings = savings.add(amount);
	}

	@Override
	public String toString() {
		return "[Beneficiary name = '" + name + "', allocationPercentage = "
				+ allocationPercentage + ", savings = " + savings + "]";
	}
}