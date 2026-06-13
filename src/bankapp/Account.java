package bankapp;

public class Account {

	String name;
	String mpin;
	double balance;
	int accountNumber;
	boolean isBlocked = false;
	
	boolean deposit(double amount) {
		if (amount > 0) {
			balance += amount;
			return true;
		} else {
			return false;
		}
	}
	
	boolean withdraw(double amount) {
		if (amount > 0 && amount <= balance) {
			balance -= amount;
			return true;
		} else {
			return false;
		}
	}
	
	void showBalance() {
		System.out.println("Current Balance : " + balance);
	}
	
	// Expects the collection to be passed from the DAO straight into the display loop
	void showTransactions(java.util.ArrayList<String> history) {
		System.out.println("----- Transaction History -----");
		if (history == null || history.isEmpty()) {
			System.out.println("No Transactions Yet. Transactions You Make Will Appear Here.");
		} else {
			int i = 1;
			for (String t : history) {
				System.out.println(i + ". " + t);
				i++;
			}
		}
	}
	
	String transfer(Account receiver, double amount) {
		if (amount <= 0) {
			return "INVALID_AMOUNT";
		}
		
		if (amount > balance) {
			return "INSUFFICIENT_BALANCE";
		}
		
		this.balance -= amount;
		receiver.balance += amount;
		
		return "SUCCESS";
	}
	
	// Expects the collection to be passed from the DAO straight into the mini statement loop
	void showMiniStatement(java.util.ArrayList<String> history) {
		System.out.println("---------- MINI STATEMENT ----------");
		if (history == null || history.isEmpty()) {
			System.out.println("No Transactions Yet.");
		} else {
			int start = Math.max(0, history.size() - 5);
			for (int i = start; i < history.size(); i++) {
				System.out.println((i + 1) + ". " + history.get(i));
			}
		}
		System.out.println("Current Balance : " + balance);
	}
	
	void changeMPIN(String oldMPIN, String newMPIN) {
		if (!mpin.equals(oldMPIN)) {
			System.out.println("Old MPIN Is Incorrect!");
		} else if (oldMPIN.equals(newMPIN)) {
			System.out.println("Your Old MPIN Cannot Be The New MPIN!");
		} else {
			mpin = newMPIN;
			System.out.println("MPIN Changed Successfully!");
		}
	}
	
	boolean isValidMPIN(String mpin) {
		return mpin.matches("\\d{6}");
	}
}