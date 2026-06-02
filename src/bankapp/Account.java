package bankapp;

import java.util.ArrayList;

public class Account {

	String name;
	String mpin;
	double balance;
	int accountNumber;
	static int nextAccountNumber=1001;
	boolean isBlocked=false;
	
	ArrayList<String> transactions = new ArrayList<>();
	
	boolean deposit(double amount) {
		if(amount > 0) {
			balance += amount;
			transactions.add("DEPOSIT +"+amount);
			return true;
		}
		else {
			return false;
		}
	}
	
	boolean withdraw(double amount) {
		if(amount>0 && amount<=balance) {
			balance -= amount;
			transactions.add("WITHDRAW -"+amount);
			return true;
		}
		else {
			return false;
		}
	}
	
	void showBalance() {
		System.out.println("Current Balance : "+balance);
	}
	
	void showTransactions() {
		System.out.println("----- Transaction History -----");
		if(transactions.isEmpty()) {
			System.out.println("No Transactions Yet. Transactions You Make Will Appear Here.");
		}
		else {
			int i=1;
			for(String t : transactions) {
				System.out.println(i+". "+t);
				i++;
			}
		}
	}
	
	String transfer(Account receiver, double amount) {
		if(amount <= 0) {
			return "INVALID_AMOUNT";
		}
		
		if(amount > balance) {
			return "INSUFFICIENT_BALANCE";
		}
		
		this.balance -= amount;
		receiver.balance += amount;
		
		transactions.add("TRANSFERRED -"+amount+" TO A/C NO. "+receiver.accountNumber);
		receiver.transactions.add("RECEIVED +"+amount+" FROM A/C NO. "+accountNumber);
		
		return "SUCCESS";
	}
	
	void showMiniStatement() {
		System.out.println("---------- MINI STATEMENT ----------");
		int start=Math.max(0,transactions.size()-5);
		for(int i=start;i<transactions.size();i++){
		 System.out.println((i+1)+". "+transactions.get(i));
		}
		System.out.println("Current Balance : "+balance);
	}
	
	void changeMPIN(String oldMPIN, String newMPIN) {
		if(!mpin.equals(oldMPIN)) {
			System.out.println("Old MPIN Is Incorrect!");
		}
		else if(oldMPIN.equals(newMPIN)) {
			System.out.println("Your Old MPIN Cannot Be The New MPIN!");
		}
		else {
			mpin=newMPIN;
			System.out.println("MPIN Changed Successfully!");
		}
	}
	
	boolean isValidMPIN(String mpin) {
		return mpin.matches("\\d{6}");
	}
}
