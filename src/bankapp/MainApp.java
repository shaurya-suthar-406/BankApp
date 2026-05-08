package bankapp;

import java.util.ArrayList;
import java.util.Scanner;

public class MainApp {

	public static void main(String[] args) {
		
		ArrayList<Account> accounts = new ArrayList<>();
		Scanner sc=new Scanner(System.in);
		int choice;
		
		System.out.println("*****************BANK APP******************");
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
				System.out.println("**********SIGNUP***********");
				System.out.println();
				Account acc=new Account();
				System.out.print("Enter Your Name : ");
				acc.name=sc.next();
				System.out.print("Set MPIN : ");
				acc.mpin=sc.next();
				System.out.print("Enter Initial Balance : ");
				acc.balance=sc.nextDouble();
				accounts.add(acc);
				System.out.println();
				System.out.println("Account Created Successfully!");
				System.out.println();
				break;

			case 2:
				int attempts = 0;
				boolean loggedIn = false;
				while(attempts < 3 && !loggedIn) {
					System.out.println();
					System.out.println("*********LOGIN*********");
					System.out.println();
					System.out.print("Enter Name : ");
					String name=sc.next();
					System.out.print("Enter MPIN : ");
					String mpin=sc.next();
					
					Account loggedInUser = null;
					for(Account a:accounts) {
						if(a.name.equals(name)&&a.mpin.equals(mpin)) {
							loggedInUser=a;
							break;
						}
					}
					
					if(loggedInUser!=null) {
						loggedIn = true;
						int userChoice;
						
						do {
							System.out.println();
							System.out.println("*********USER MENU*********");
							System.out.println("1. Check Balance");
							System.out.println("2. Deposit");
							System.out.println("3. Withdraw");
							System.out.println("4. View Transactions");
							System.out.println("5. Logout");
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
								break;
								
							case 3:
								System.out.print("Enter Amount To Withdraw : ");
								double withdrawAmount=sc.nextDouble();
								loggedInUser.withdraw(withdrawAmount);
								break;
								
							case 4:
								loggedInUser.showTransactions();
								break;
								
							case 5:
								System.out.println("Logging Out...");
								System.out.println("Logged Out Successfully!");
								break;
							default:
								System.out.println("Invalid Choice! Please Choose A Valid Number.");
							}
						} while(userChoice != 5);
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
				System.out.println("* Thank You For Using Our Services *");
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
