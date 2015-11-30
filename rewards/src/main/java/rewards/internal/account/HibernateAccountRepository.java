package rewards.internal.account;

import java.beans.ConstructorProperties;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import rewards.InvalidCreditCardException;

/**
 * An AccountRepository that uses Hibernate to work with accounts.
 */
@Profile("hibernate")
@Repository("accountRespository")
public class HibernateAccountRepository implements AccountRepository {

	private SessionFactory sessionFactory;

	/**
	 * Creates a new Hibernate account repository.
	 * 
	 * @param sessionFactory
	 *            the Hibernate session factory
	 */
	@Autowired
	@ConstructorProperties(value = "sessionFactory")
	public HibernateAccountRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<Account> findAllAccounts() {
		return getCurrentSession().createQuery("from Account").list();
	}

	@SuppressWarnings("unchecked")
	public List<Account> findAccounts(String name, int first, int nResults) {
		StringBuilder searchString = new StringBuilder("%").append(
				name.toUpperCase()).append("%");
		return getCurrentSession()
				.createQuery(
						"from Account a where upper(a.name) like :name order by a.name")
				.setString("name", searchString.toString())
				.setFirstResult(first).setMaxResults(nResults).list();
	}

	public Account findByAccountNumber(String number) {
		return (Account) getCurrentSession()
				.createQuery(
						"from Account a left join fetch a.beneficiaries where a.number = :cc")
				.setString("cc", number).uniqueResult();
	}

	public Account findByCreditCard(String creditCardNumber)
			throws InvalidCreditCardException {
		Account account = (Account) getCurrentSession()
				.createQuery(
						"from Account a left join fetch a.beneficiaries where a.creditCardNumber = :cc")
				.setString("cc", creditCardNumber).uniqueResult();

		if (account == null)
			throw new InvalidCreditCardException(creditCardNumber);

		return account;
	}

	public void update(Account account) {
		account.isValid(); // Ensure account is valid first
		getCurrentSession().saveOrUpdate(account);
	}

	public void updateBeneficiaries(Account account) {
		account.isValid(); // Ensure account is valid first

		Session session = getCurrentSession();

		if (account.getEntityId() == null)
			throw new EmptyResultDataAccessException(
					"Entity not yet persistent, use update(account) instead: "
							+ account, 1);

		if (session.get(Account.class, account.getEntityId()) == null)
			throw new EmptyResultDataAccessException("Not found: "
					+ account, 1);

		session.update(account);
	}

	/**
	 * Returns the session associated with the ongoing transaction.
	 * 
	 * @return the current session
	 */
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}