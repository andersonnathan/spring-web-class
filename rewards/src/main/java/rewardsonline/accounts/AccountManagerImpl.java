package rewardsonline.accounts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import common.money.Percentage;

import rewards.InvalidCreditCardException;
import rewards.internal.account.AccountRepository;

/**
 * Implements the AccountManager service using the {@link AccountRepository}
 * underneath to fetch and manage data. Uses the DTO pattern to hide domain
 * objects from the view layer. This is good practice and, in particular, avoids
 * lazy loading problems when domain objects are loaded via an ORM.
 */
@Service("accountManager")
public class AccountManagerImpl implements AccountManager, Serializable {

	/**
	 * Serialization version number.
	 */
	private static final long serialVersionUID = -7489715763051120509L;

	private AccountRepository accountRepository;

	@Autowired
	public AccountManagerImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Account> findAllAccounts() {
		return copyAccountsToDTOs(accountRepository.findAllAccounts());
	}

	@Transactional(readOnly = true)
	public List<Account> findAccounts(AccountSearchCriteria searchCriteria) {
		StringBuilder searchString = new StringBuilder("%").append(
				searchCriteria.getSearchString().toUpperCase()).append("%");
		return copyAccountsToDTOs(accountRepository.findAccounts(
				searchString.toString(), searchCriteria.getPage()
						* searchCriteria.getMaximumResults(),
				searchCriteria.getMaximumResults()));
	}

	@Override
	@Transactional(readOnly = true)
	public Account findAccount(String accountNumber) {
		return makeDTO(accountRepository.findByAccountNumber(accountNumber));
	}

	@Override
	public Account findByCreditCard(String creditCardNumber)
			throws InvalidCreditCardException{
		return makeDTO(accountRepository.findByCreditCard(creditCardNumber));
	}

	@Override
	@Transactional
	public void update(Account dto) {
		rewards.internal.account.Account account = accountRepository
				.findByAccountNumber(dto.getNumber());

		// Account number should not have changed so the account we found should
		// have the same entity id as saved in the DTO
		if (dto.getEntityId() != account.getEntityId())
			throw new IllegalStateException(
					"Account number and entity id not consistent");

		account.setCreditCardNumber(dto.getCreditCardNumber());
		account.setDateOfBirth(dto.getDateOfBirth());
		account.setEmail(dto.getEmail());
		account.setName(dto.getName());
		account.setReceiveMonthlyEmailUpdate(dto.isReceiveMonthlyEmailUpdate());
		account.setReceiveNewsletter(dto.isReceiveNewsletter());
	}

	@Override
	@Transactional
	public void addBeneficiary(String accountNumber, String beneficiaryName) {
		// Look up persistent account
		rewards.internal.account.Account account = accountRepository
				.findByAccountNumber(accountNumber);

		if (account.getBeneficiaries().isEmpty())
			account.addBeneficiary(beneficiaryName, Percentage.oneHundred());
		else
			// Should rebalance contributions, but that's too hard for now
			account.addBeneficiary(beneficiaryName, Percentage.zero());

		accountRepository.updateBeneficiaries(account);
	}

	@Override
	@Transactional
	public void removeBeneficiary(String accountNumber, String beneficiaryName) {
		rewards.internal.account.Account account = accountRepository
				.findByAccountNumber(accountNumber);

		account.removeBeneficiary(beneficiaryName);
		accountRepository.updateBeneficiaries(account);
	}

	@Override
	@Transactional
	public Account save(Account dto) {
		rewards.internal.account.Account account = new rewards.internal.account.Account(
				dto.getNumber(), dto.getName());

		account.setCreditCardNumber(dto.getCreditCardNumber());
		account.setDateOfBirth(dto.getDateOfBirth());
		account.setEmail(dto.getEmail());
		account.setName(dto.getName());
		account.setReceiveMonthlyEmailUpdate(dto.isReceiveMonthlyEmailUpdate());
		account.setReceiveNewsletter(dto.isReceiveNewsletter());

		for (Beneficiary b: dto.getBeneficiaries()) {
			account.addBeneficiary(b.getName(), b.getAllocationPercentage());
		}
		
		accountRepository.update(account);
		
		// Copy entity ids into DTOs
		dto.setEntityId(account.getEntityId());

		for (Beneficiary b: dto.getBeneficiaries()) {
			b.setEntityId(account.getBeneficiary(b.getName()).getEntityId());
		}
		
		return dto;
	}

	/**
	 * Use a list of persistent Accounts to create a list to Account DTOs.
	 * 
	 * @param accounts
	 *            A list of persistent Accounts
	 * @return A list of Account DTOs
	 */
	private List<Account> copyAccountsToDTOs(
			List<rewards.internal.account.Account> accounts) {
		List<Account> results = new ArrayList<Account>(accounts.size());

		for (rewards.internal.account.Account account : accounts) {
			results.add(makeDTO(account));
		}

		return results;
	}

	/**
	 * Convert the Account domain object (in <tt>rewards.internal.account</tt>)
	 * into a Data Transfer Object (in the <tt>rewardsonline</tt>) package.
	 * <p>
	 * See <a href="http://en.wikipedia.org/wiki/Data_transfer_object">DTO
	 * Pattern</a> for more on the DTO pattern.
	 * 
	 * @param account
	 *            An account domain object.
	 * @return An account DTO.
	 */
	private Account makeDTO(rewards.internal.account.Account account) {
		Account dto = new Account();

		dto.setEntityId(account.getEntityId());
		dto.setNumber(account.getNumber());
		dto.setName(account.getName());
		dto.setCreditCardNumber(account.getCreditCardNumber());
		dto.setDateOfBirth(account.getDateOfBirth());
		dto.setReceiveMonthlyEmailUpdate(account.isReceiveMonthlyEmailUpdate());
		dto.setReceiveNewsletter(account.isReceiveNewsletter());
		dto.setEmail(account.getEmail());

		if (account.getBeneficiaries().size() > 0) {
			Set<Beneficiary> beneficiaries = new HashSet<Beneficiary>();
			for (rewards.internal.account.Beneficiary beneficiary : account
					.getBeneficiaries()) {
				Beneficiary dto2 = new Beneficiary(beneficiary.getName(),
						beneficiary.getAllocationPercentage(),
						beneficiary.getSavings());
				beneficiaries.add(dto2);
			}
			dto.setBeneficiaries(beneficiaries);
		}

		return dto;
	}

}
