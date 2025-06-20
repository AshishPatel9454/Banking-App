package net.javaguides.banking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.javaguides.banking.dto.AccountDto;
import net.javaguides.banking.dto.TransactionDto;
import net.javaguides.banking.dto.TransferFundDto;
import net.javaguides.banking.service.AccountService;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
	private AccountService accountService;

	public AccountController(AccountService accountService) {

		this.accountService = accountService;
	}

	// add Account Rest api
	@PostMapping
	public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto) {
		return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);

	}

	// get account
	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
		AccountDto accountDto = accountService.getAccountById(id);
		return ResponseEntity.ok(accountDto);

	}

	// deposit rest api
	@PutMapping("/{id}/deposit")
	public ResponseEntity<AccountDto> deposit(@PathVariable Long id, @RequestBody Map<String, Double> request) {
		AccountDto accountDto = accountService.deposit(id, request.get("amount"));

		return ResponseEntity.ok(accountDto);

	}

	// withdraw rest api
	@PutMapping("/{id}/withdraw")
	public ResponseEntity<AccountDto> withdraw(@PathVariable Long id, @RequestBody Map<String, Double> request) {
		double amount = request.get("amount");
		AccountDto accountDto = accountService.withdraw(id, amount);
		return ResponseEntity.ok(accountDto);

	}

	// get all account rest api
	@GetMapping
	public ResponseEntity<List<AccountDto>> getAllAccounts() {
		List<AccountDto> accounts = accountService.getAllAccounts();
		return ResponseEntity.ok(accounts);

	}

	// delete account rest api
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
		accountService.deleteAccount(id);
		return ResponseEntity.ok("Account is deleted successfully");

	}

	// build transfer rest api
	@PostMapping("/transfer")
	public ResponseEntity<String> transferFund(@RequestBody TransferFundDto transferfundDto) {
		accountService.transferFunds(transferfundDto);
		return ResponseEntity.ok("Tranfer successfull");
	}

	// build transaction rest api
	@GetMapping("/{id}/transactions")
	public ResponseEntity<List<TransactionDto>> fetchAccountTransactions(@PathVariable Long id) {
		List<TransactionDto> transactions = accountService.getAccountTransactions(id);
		return ResponseEntity.ok(transactions);
	}

}