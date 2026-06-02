package bankapp;

import java.util.ArrayList;
import java.util.Scanner;

public class MainApp {

	public static void main(String[] args) {

		ArrayList<Account> accounts = new ArrayList<>();

		FileHandler.loadData(accounts);

		Scanner sc = new Scanner(System.in);
		String choice; // Changed from int to String for crash-proof menu input

		System.out.println("--------------- BANK APP ---------------");
		do {
			System.out.println();
			System.out.println("1. Signup");
			System.out.println("2. Login");
			System.out.println("3. Exit");
			System.out.println();
			System.out.print("Enter Number Corresponding To Your Choice : ");
			choice = sc.next(); // Safely reads any input as a string

			switch (choice) {
			case "1": // Use string literals for menu options
				System.out.println();
				System.out.println("---------- SIGNUP ----------");
				System.out.println();
				Account acc = new Account();
				System.out.print("Enter Your Name : ");
				sc.nextLine(); // Clear buffer
				acc.name = sc.nextLine();
				while (true) {
					System.out.print("Set MPIN (6 Digits) : ");
					String tempMPIN = sc.next();
					if (acc.isValidMPIN(tempMPIN)) {
						acc.mpin = tempMPIN;
						break;
					} else {
						System.out.println("Invalid MPIN! Enter Exactly 6 Digits.");
					}
				}
				
				// Infinite loop with validation to handle invalid types for initial balance
				while (true) {
					System.out.print("Enter Initial Balance : ");
					if (sc.hasNextDouble()) {
						acc.balance = sc.nextDouble();
						if (acc.balance >= 0) {
							break;
						}
						System.out.println("Initial balance cannot be negative!");
					} else {
						System.out.println("Invalid Input! Please enter a valid number.");
						sc.next(); // Clear the bad tokens out of scanner memory
					}
				}
				
				acc.accountNumber = Account.nextAccountNumber;
				Account.nextAccountNumber++;
				accounts.add(acc);
				FileHandler.saveData(accounts);
				System.out.println();
				System.out.println("Account Opened Successfully!");
				System.out.println();
				System.out.println("Account Number : " + acc.accountNumber);
				System.out.println("Name : " + acc.name);
				System.out.println("Current Balance : " + acc.balance);
				System.out.println();
				break;

			case "2":
				System.out.println();
				System.out.println("---------- LOGIN ----------");
				System.out.println();
				System.out.print("Enter A/c Number : ");
				
				// Validate account number token type
				if (!sc.hasNextInt()) {
					System.out.println("Invalid Account Number Format! Numbers only.");
					sc.next(); // Clear bad token
					break;
				}
				int accountNumber = sc.nextInt();

				// Find the account first
				Account targetedUser = null;
				for (Account a : accounts) {
					if (a.accountNumber == accountNumber) {
						targetedUser = a;
						break;
					}
				}

				if (targetedUser == null) {
					System.out.println("Account Number Not Found!");
					break;
				}

				if (targetedUser.isBlocked) {
					System.out.println(
							"This account has been temporarily blocked due to multiple failed login attempts. Please contact support.");
					break;
				}

				int attempts = 0;
				boolean loggedIn = false;

				while (attempts < 3) {
					System.out.print("Enter MPIN for Account " + accountNumber + " (Attempts left: " + (3 - attempts) + "): ");
					String mpin = sc.next();

					if (targetedUser.mpin.equals(mpin)) {
						loggedIn = true;
						break; // Exit the login loop immediately on success
					} else {
						attempts++;
						if (attempts < 3) {
							System.out.println("The Credentials Are Invalid!");
						}
					}
				}

				if (loggedIn) {
					String userChoice; // Changed from int to String for crash-proof sub-menu input

					do {
						System.out.println();
						System.out.println("---------- USER MENU ----------");
						System.out.println("1. Check Balance");
						System.out.println("2. Deposit");
						System.out.println("3. Withdraw");
						System.out.println("4. Transfer Money");
						System.out.println("5. View Transactions");
						System.out.println("6. View Mini Statement");
						System.out.println("7. Change MPIN");
						System.out.println("8. Logout");
						System.out.println();
						System.out.print("Enter Choice : ");
						userChoice = sc.next();
						System.out.println();
						
						switch (userChoice) {
						case "1":
							targetedUser.showBalance();
							break;

						case "2":
							System.out.print("Enter Amount To Deposit : ");
							Integer amount=sc.nextInt();
							if (targetedUser.deposit(amount)) {
							    System.out.println("Deposit Successful!");
							    System.out.println("Current Balance : " + targetedUser.balance);
							    FileHandler.saveData(accounts);
							} else {
							    System.out.println("Please Enter A Valid Amount For Deposit!");
							}
							break;

						case "3":
						    System.out.print("Enter Amount To Withdraw : ");
						    if (sc.hasNextDouble()) {
						        double withdrawAmount = sc.nextDouble();
						        
						        // Call the method and check if it returned true or false
						        if (targetedUser.withdraw(withdrawAmount)) {
						            System.out.println("Withdrawal Successful!");
						            System.out.println("Current Balance : " + targetedUser.balance);
						            
						            // Save data to the text file since the balance changed successfully
						            FileHandler.saveData(accounts);
						        } else {
						            System.out.println("Withdrawal Failed! Insufficient Balance Or Invalid Amount.");
						        }
						    } else {
						        System.out.println("Invalid Amount Format!");
						        sc.next(); // Clear the invalid input buffer
						    }
						    break;

						case "4":
							System.out.print("Enter Reciever Account Number : ");
							if (!sc.hasNextInt()) {
								System.out.println("Invalid Account Number Format!");
								sc.next(); // Clear bad input
								break;
							}
							int receiverAccNo = sc.nextInt();
							
							if (receiverAccNo == targetedUser.accountNumber) {
								System.out.println("You Cannot Transfer Money To Your Own Account!");
								break;
							}

							Account receiver = null;
							for (Account a : accounts) {
								if (a.accountNumber == receiverAccNo) {
									receiver = a;
									break;
								}
							}
							if (receiver != null) {
								System.out.print("Enter Amount To Transfer : ");
								if (sc.hasNextDouble()) {
									double trAmount = sc.nextDouble();
									String status = targetedUser.transfer(receiver, trAmount);
									if(status.equals("INVALID_AMOUNT")) {
										System.out.println("Invalid Amount! Please Enter A Valid Amount!");
									}
									else if(status.equals("INSUFFICIENT_BALANCE")) {
										System.out.println("Transfer Failed! You Do Not Have Sufficient Balance!");
									}
									else if(status.equals("SUCCESS")) {
										System.out.println("Transfer Successful!");
										System.out.println("\nTransferred Amount : "+trAmount);
										System.out.println("Receiver Name : "+receiver.name);
										System.out.println("\nCurrent Balance : "+targetedUser.balance);
										FileHandler.saveData(accounts);
									}
								}
								else {
									System.out.println("Invalid Transfer Amount Format!");
									sc.next();
								}
							}
							else {
								System.out.println("Receiver Account Not Found!");
							}
							break;

						case "5":
							targetedUser.showTransactions();
							break;

						case "6":
							targetedUser.showMiniStatement();
							break;

						case "7":
							System.out.print("Enter Old MPIN : ");
							String oldMPIN = sc.next();
							String newMPIN;
							while (true) {
								System.out.print("Enter New MPIN (6 Digits) : ");
								newMPIN = sc.next();
								if (targetedUser.isValidMPIN(newMPIN)) {
									break;
								} else {
									System.out.println("Invalid MPIN! Enter Exactly 6 Digits.");
								}
							}
							System.out.print("Confirm New MPIN : ");
							String confirmMPIN = sc.next();
							if (newMPIN.equals(confirmMPIN)) {
								targetedUser.changeMPIN(oldMPIN, newMPIN);
								FileHandler.saveData(accounts);
							} else {
								System.out.println("MPIN Confirmation Failed!");
							}
							break;

						case "8":
							System.out.println("Logging Out...");
							System.out.println("Logged Out Successfully!");
							break;
						default:
							System.out.println("Invalid Choice! Please Choose A Valid Number.");
						}
					} while (!userChoice.equals("8"));
					
				} else if (attempts == 3) { // Triggers block ONLY if max failed entry attempts hit
					targetedUser.isBlocked = true; 
					FileHandler.saveData(accounts); 
					System.out.println("Access To The Account Has Been Temporarily Blocked Due To 3 Failed Login Attempts. Please Contact Support!");
				}
				break;

			case "3":
				System.out.println("Exiting...");
				System.out.println("\n* Thank You For Using Our Services *");
				System.out.println("\nHave A Nice Day :)");
				break;

			default:
				System.out.println("Invalid Choice! Please Choose A Valid Number.");
				break;
			}
		} while (!choice.equals("3"));

		sc.close();
	}
}