package bankapp;

import java.util.ArrayList;
import java.util.Scanner;

public class MainApp {

	public static void main(String[] args) {
		
		ArrayList<Account> accounts = new ArrayList<>();
		
		FileHandler.loadData(accounts);
		
		Scanner sc=new Scanner(System.in);
		int choice;

		System.out.println("--------------- BANK APP ---------------");
		do {
			System.out.println();
			System.out.println("1. Signup");
			System.out.println("2. Login");
			System.out.println("3. Exit");
			System.out.println();
			System.out.print("Enter Number Corresponding To Your Choice : ");
			choice=sc.nextInt();
			
			switch(choice) {
			case 1:
				System.out.println();
				System.out.println("---------- SIGNUP ----------");
				System.out.println();
				Account acc=new Account();
				System.out.print("Enter Your Name : ");
				acc.name=sc.next();
				System.out.print("Set MPIN : ");
				acc.mpin=sc.next();
				System.out.print("Enter Initial Balance : ");
				acc.balance=sc.nextDouble();
				acc.accountNumber=Account.nextAccountNumber;
				Account.nextAccountNumber++;
				accounts.add(acc);
				FileHandler.saveData(accounts);
				System.out.println();
				System.out.println("Account Opened Successfully!");
				System.out.println();
				System.out.println("Account Number : "+acc.accountNumber);
				System.out.println("Name : "+acc.name);
				System.out.println("Current Balance : "+acc.balance);
				System.out.println();
				break;

			case 2:
				int attempts = 0;
				boolean loggedIn = false;
				while(attempts < 3 && !loggedIn) {
					System.out.println();
					System.out.println("---------- LOGIN ----------");
					System.out.println();
					System.out.print("Enter A/c Number : ");
					int accountNumber=sc.nextInt();
					System.out.print("Enter MPIN : ");
					String mpin=sc.next();
					
					Account loggedInUser = null;
					for(Account a:accounts) {
						if(a.accountNumber == accountNumber && a.mpin.equals(mpin)) {
							loggedInUser=a;
							break;
						}
					}
					
					if(loggedInUser!=null) {
						loggedIn = true;
						int userChoice;
						
						do {
							System.out.println();
							System.out.println("---------- USER MENU ----------");
							System.out.println("1. Check Balance");
							System.out.println("2. Deposit");
							System.out.println("3. Withdraw");
							System.out.println("4. Transfer Money");
							System.out.println("5. View Transactions");
							System.out.println("6. Logout");
							System.out.println();
							System.out.print("Enter Choice : ");
							userChoice = sc.nextInt();
							System.out.println();
							switch(userChoice) {
							case 1:
								loggedInUser.showBalance();
								break;
								
							case 2:
								System.out.print("Enter Amount To Deposit : ");
								double amount=sc.nextDouble();
								loggedInUser.deposit(amount);
								FileHandler.saveData(accounts);
								break;
								
							case 3:
								System.out.print("Enter Amount To Withdraw : ");
								double withdrawAmount=sc.nextDouble();
								loggedInUser.withdraw(withdrawAmount);
								FileHandler.saveData(accounts);
								break;
								
							case 4:
								System.out.print("Enter Reciever Account Number : ");
								int receiverAccNo = sc.nextInt();
								if(receiverAccNo==loggedInUser.accountNumber) {
									System.out.println("You Cannot Transfer Money To Your Own Account!");
									break;
								}
								
								Account receiver = null;
								
								for(Account a:accounts) {
									if(a.accountNumber == receiverAccNo) {
										receiver=a;
										break;
									}
								}
									if(receiver != null) {
										System.out.print("Enter Amount To Transfer : ");
										double trAmount=sc.nextDouble();
										loggedInUser.transfer(receiver, trAmount);
										FileHandler.saveData(accounts);
									}
									else {
										System.out.println("Receiver Account Not Found!");
									}
								break;
								
							case 5:
								loggedInUser.showTransactions();
								break;
								
							case 6:
								System.out.println("Logging Out...");
								System.out.println("Logged Out Successfully!");
								break;
							default:
								System.out.println("Invalid Choice! Please Choose A Valid Number.");
							}
						} while(userChoice != 6);
					}
					else {
						attempts++;
						System.out.println("The Credentials Are Invalid! You Have "+(3-attempts)+" Attempts Left.");
					}
				}
				if(!loggedIn) {
					System.out.println("Access To The Account Has Been Temporarily Blocked Due To Multiple Failed Login Attempts. Please Try Again Later!");
				}
				
				break;
				
			case 3:
				System.out.println("Exiting...");
				System.out.println("\n* Thank You For Using Our Services *");
				System.out.println("\nHave A Nice Day :)");
				break;
				
			default:
				System.out.println("Invalid Choice! Please Choose A Valid Number.");
				break;
			}
		} while(choice!=3);
		
		sc.close();
	}

}
