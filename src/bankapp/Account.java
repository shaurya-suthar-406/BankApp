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
	
	void deposit(double amount) {
		if(amount > 0) {
			balance += amount;
			transactions.add("DEPOSIT +"+amount);
			System.out.println("Deposit Successful!");
			System.out.println("Current Balance : "+balance);
		}
		else {
			System.out.println("Please Enter A Valid Amount For Deposit!");
		}
	}
	
	boolean withdraw(double amount) {
		if(amount>0 && amount<=balance) {
			balance -= amount;
			transactions.add("WITHDRAW -"+amount);
			System.out.println("Withdrawal Successful!");
			System.out.println("Current Balance : "+balance);
			return true;
		}
		else {
			System.out.println("Insufficient Balance Or Invalid Amount!");
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
	
	void transfer(Account receiver, double amount) {
		if(amount>0 && amount<=balance) {
			balance -= amount;
			receiver.balance += amount;
			transactions.add("TRANSFERRED -"+amount+" TO A/C "+receiver.accountNumber);
			receiver.transactions.add("RECEIVED +"+amount+" FROM A/C "+accountNumber);
			System.out.println("Transfer Successful!");
			System.out.println("\nTransferred Amount : "+amount);
			System.out.println("Receiver Name : "+receiver.name);
			System.out.println("\nCurrent Balance : "+balance);
		}
		else {
			System.out.println("\nInsufficient Balance Or Invalid Amount!");
		}
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
