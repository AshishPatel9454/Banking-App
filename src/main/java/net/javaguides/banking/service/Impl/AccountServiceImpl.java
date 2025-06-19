package net.javaguides.banking.service.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.javaguides.banking.dto.AccountDto;
import net.javaguides.banking.dto.TransactionDto;
import net.javaguides.banking.dto.TransferFundDto;
import net.javaguides.banking.entity.Account;
import net.javaguides.banking.entity.Transaction;
import net.javaguides.banking.exception.AccountException;
import net.javaguides.banking.mapper.AccountMapper;
import net.javaguides.banking.repository.AccountRepository;
import net.javaguides.banking.repository.TransactionRepository;
import net.javaguides.banking.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	private AccountRepository accountRepository;

	private TransactionRepository transactionRepository;

	private static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
	private static final String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";
	private static final String TRANSACTION_TYPE_TRANSFER = "TRANSFER";

	public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {

		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	@Override
	public AccountDto createAccount(AccountDto accountDto) {
		Account account = AccountMapper.mapToAccount(accountDto);
		Account savedAccount = accountRepository.save(account);
		return AccountMapper.mapToAccountDto(savedAccount);
	}

	@Override
	public AccountDto getAccountById(Long id) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountException("Account does not exist"));
		return AccountMapper.mapToAccountDto(account);
	}

	@Override
	public AccountDto deposit(Long id, Double amount) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountException("Account does not exist"));
		double total = account.getBalance() + amount;
		account.setBalance(total);
		Account savedAccount = accountRepository.save(account);

		Transaction transaction = new Transaction();
		transaction.setAccountId(id);
		transaction.setAmount(amount);
		transaction.setTransactionType(TRANSACTION_TYPE_DEPOSIT);
		transaction.setTimestamp(LocalDateTime.now());
		transactionRepository.save(transaction);
		return AccountMapper.mapToAccountDto(savedAccount);
	}

	@Override
	public AccountDto withdraw(Long id, Double amount) {

		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountException("Account does not exist"));
		if (account.getBalance() < amount) {
			throw new AccountException("Insufficient balance");
		}
		double total = account.getBalance() - amount;
		account.setBalance(total);
		Account savedAccount = accountRepository.save(account);
		Transaction transaction = new Transaction();
		transaction.setAccountId(id);
		transaction.setAmount(amount);
		transaction.setTransactionType(TRANSACTION_TYPE_WITHDRAW);
		transaction.setTimestamp(LocalDateTime.now());
		transactionRepository.save(transaction);
		return AccountMapper.mapToAccountDto(savedAccount);
	}

	@Override
	public List<AccountDto> getAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account)).collect(Collectors.toList());

	}

	@Override
	public void deleteAccount(Long id) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountException("Account does not exist"));

		accountRepository.deleteById(id);

	}

	@Override
	public void transferFunds(TransferFundDto transferFundDto) {
		// retrieve the account from with we send amount
		Account fromAccount = accountRepository.findById(transferFundDto.fromAccountId())
				.orElseThrow(() -> new AccountException("Account does not exists"));
		// retrieve the account to which we send amount
		Account toAccount = accountRepository.findById(transferFundDto.toAccountId())
				.orElseThrow(() -> new AccountException("Account does not exists"));
		if (fromAccount.getBalance() < transferFundDto.amount()) {
			throw new RuntimeException("Insufficient amount");
		}

		// Debit the amount from fromAccount object
		fromAccount.setBalance(fromAccount.getBalance() - transferFundDto.amount());
		// Credit the amount to toAccount object
		toAccount.setBalance(toAccount.getBalance() + transferFundDto.amount());

		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);

		Transaction transaction = new Transaction();
		transaction.setAccountId(transferFundDto.fromAccountId());
		transaction.setAmount(transferFundDto.amount());
		transaction.setTransactionType(TRANSACTION_TYPE_TRANSFER);
		transaction.setTimestamp(LocalDateTime.now());
		transactionRepository.save(transaction);

	}

	@Override
	public List<TransactionDto> getAccountTransactions(Long id) {
		List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTimestampDesc(id);
		List<TransactionDto> dtoList= transactions.stream().map((transaction) -> convertEntityToDto(transaction)).collect(Collectors.toList());
		return dtoList;
	}

	private TransactionDto convertEntityToDto(Transaction transaction) {
		return new TransactionDto(transaction.getId(), transaction.getAccountId(), transaction.getAmount(),
				transaction.getTransactionType(), transaction.getTimestamp());

	}

}
