package banking;

import java.util.HashMap;
import java.util.Map;

public class Bank {
	private Map<String, Accounts> accounts;

	Bank() {
		accounts = new HashMap<>();
	}

	public Map<String, Accounts> getAccount() {
		return accounts;
	}

	public void addAccount(String id, Accounts account) {
		accounts.put(id, account);
	}

	public int getNumberOfAccounts() {
		return accounts.size();
	}

	public void addDeposit(String id, double amount) {
		getAccount().get(id).addDeposit(amount);
	}

	public void withdraw(String id, double amount) {
		getAccount().get(id).withdraw(amount);
	}

	public boolean accountExists(String accountId) {
		return accounts.containsKey(accountId);
	}

	public String getAccountType(String accountId) {
		if (accountExists(accountId)) {
			return accounts.get(accountId).getAccountType();
		} else {
			return null;
		}
	}
}
