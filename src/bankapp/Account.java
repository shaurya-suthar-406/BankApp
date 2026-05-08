package bankapp;

import java.util.ArrayList;

public class Account {

	String name;
	String mpin;
	double balance;
	
	ArrayList<String> transactions = new ArrayList<>();
	
	void deposit(double amount) {
		if(amount > 0) {
			balance += amount;
			String msg = "Deposited : "+amount;
			transactions.add(msg);
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
			String msg="Withdrawn : "+amount;
			transactions.add(msg);
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
			for(String t : transactions) {
				System.out.println(t);
			}
		}
	}
}
