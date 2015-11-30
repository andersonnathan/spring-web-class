package rewards.internal.account;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import rewards.InvalidCreditCardException;

/**
 * Loads accounts from a data source using pure JPA.
 */
@Profile("jpa")
@Repository("accountRespository")
public class JpaAccountRepository implements AccountRepository {

	EntityManager entityManager;

	/**
	 * Create an instance. Have to have a default constructor since JPA expects
	 * to do dependency injection via a setter (JavaBean style).
	 */
	public JpaAccountRepository() {
	}

	/**
	 * Set the entity manager - like @Autowired specifically for JPA. Doesn't
	 * support constructor injection, so we have to use a setter.
	 * 
	 * @param entityManager
	 *            When using Spring this is actually an entity manager proxy.
	 *            Each time it is used, it resolves to the current entity
	 *            manager for the current transaction and thread.
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Account> findAllAccounts() {
		return entityManager.createQuery("SELECT a FROM Account a",
				Account.class).getResultList();
	}

	@Override
	public List<Account> findAccounts(String name, int first, int nResults) {
		StringBuilder searchString = new StringBuilder("%").append(
				name.toUpperCase()).append("%");
		return entityManager
				.createQuery(
						"select a from Account a LEFT JOIN FETCH a.beneficiaries " +
						"WHERE upper(a.name) LIKE :name order by a.name",
						Account.class)
				.setParameter("name", searchString.toString())
				.setFirstResult(first).setMaxResults(nResults).getResultList();
	}

	@Override
	public Account findByCreditCard(String creditCardNumber)
			throws InvalidCreditCardException {
		try {
			return entityManager
					.createQuery(
							"SELECT a FROM Account a LEFT JOIN FETCH a.beneficiaries "
									+ "WHERE a.creditCardNumber = :cc",
							Account.class).setParameter("cc", creditCardNumber)
					.getSingleResult();
		} catch (NoResultException ex) {
			throw new InvalidCreditCardException(creditCardNumber);
		}
	}

	@Override
	public Account findByAccountNumber(String accountNumber) {
		return entityManager
				.createQuery(
						"SELECT a FROM Account a LEFT JOIN FETCH a.beneficiaries "
								+ "WHERE a.number = :number", Account.class)
				.setParameter("number", accountNumber).getSingleResult();
	}

	@Override
	public void update(Account account) {
		account.isValid();  // Ensure account is valid first

		if (account.getEntityId() == null)
			entityManager.persist(account);
		else if (!entityManager.contains(account))
			entityManager.merge(account);
	}

	@Override
	public void updateBeneficiaries(Account account) {
		account.isValid();  // Ensure account is valid first
		entityManager.merge(account);
	}

}
