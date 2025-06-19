package net.javaguides.banking.service;

import java.util.List;

import net.javaguides.banking.dto.AccountDto;
import net.javaguides.banking.dto.TransactionDto;
import net.javaguides.banking.dto.TransferFundDto;
import net.javaguides.banking.entity.Account;

public interface AccountService {
	AccountDto createAccount(AccountDto accountDto);

	AccountDto getAccountById(Long id);

	AccountDto deposit(Long id, Double amount);

	AccountDto withdraw(Long id, Double amount);

	List<AccountDto> getAllAccounts();

	void deleteAccount(Long id);

	void transferFunds(TransferFundDto tranferFundDto);

	List<TransactionDto> getAccountTransactions(Long id);
}
