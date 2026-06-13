package bankapp;

import java.util.ArrayList;
import java.util.Scanner;

public class MainApp {

	public static void main(String[] args) {

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
                        sc.next(); 
                    }
                }
                
                // DATABASE MIGRATION HERE: Get unique next number from DB and save it
                acc.accountNumber = AccountDAO.getNextAccountNumber(); 
                
                if (AccountDAO.saveAccount(acc)) {
                    System.out.println();
                    System.out.println("Account Opened Successfully and Saved to DB! 🎉");
                    System.out.println();
                    System.out.println("Account Number : " + acc.accountNumber);
                    System.out.println("Name : " + acc.name);
                    System.out.println("Current Balance : " + acc.balance);
                } else {
                    System.out.println("Signup Failed! Database connection error.");
                }
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

				System.out.print("Enter MPIN: ");
				String mpinInput = sc.next();

				// DATABASE MIGRATION: Fetch authenticated user row directly via SQL
				Account targetedUser = AccountDAO.getAccount(accountNumber, mpinInput);

				if (targetedUser == null) {
					System.out.println("Invalid Account Number or MPIN! Login Failed.");
					break;
				}

				if (targetedUser.isBlocked) {
					System.out.println(
							"This account has been temporarily blocked due to multiple failed login attempts. Please contact support.");
					break;
				}

				// If the database returns a valid object, credentials match perfectly!
				System.out.println("Login Successful! Welcome back, " + targetedUser.name + ".");
				String userChoice; 

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
					    Double amount = sc.nextDouble();
					    
					    if (targetedUser.deposit(amount)) {
					        boolean dbUpdated = AccountDAO.updateBalance(targetedUser.accountNumber, targetedUser.balance);
					        
					        if (dbUpdated) {
					        	String txnDesc = "DEPOSIT +"+amount;
					        	AccountDAO.logTransaction(targetedUser.accountNumber, txnDesc);
					        	
					            System.out.println("Deposit Successful and saved to Database! 🎉");
					            System.out.println("Current Balance : " + targetedUser.balance);
					        } else {
					            System.out.println("Database sync failed! Balance updated in memory only.");
					        }
					    } else {
					        System.out.println("Please Enter A Valid Amount For Deposit!");
					    }
					    break;

					case "3":
					    System.out.print("Enter Amount To Withdraw : ");
					    Double wAmount = sc.nextDouble();
					    
					    if(targetedUser.withdraw(wAmount)) {
					    	boolean dbUpdated = AccountDAO.updateBalance(targetedUser.accountNumber, targetedUser.balance);
					    	
					    	if(dbUpdated) {
					    		String txnDesc = "WITHDRAW -"+wAmount;
					    		AccountDAO.logTransaction(targetedUser.accountNumber, txnDesc);
					    		
					    		System.out.println("Withdrawal Successful! Saved to Database! 🎉");
					    		System.out.println("Current Balance : " + targetedUser.balance);
					    	} else {
					    		System.out.println("Database sync failed! Balance updated in memory only.");
					    	}
					    } else {
					    	System.out.println("Please Enter A Valid Amount For Withdrawal!");
					    }
					    break;

					case "4":
						System.out.print("Enter Reciever Account Number : ");
						if (!sc.hasNextInt()) {
							System.out.println("Invalid Account Number Format!");
							sc.next(); 
							break;
						}
						int receiverAccNo = sc.nextInt();
						
						if (receiverAccNo == targetedUser.accountNumber) {
							System.out.println("You Cannot Transfer Money To Your Own Account!");
							break;
						}

						Account receiver = AccountDAO.getAccount(receiverAccNo);
						
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
									boolean senderUpdated = AccountDAO.updateBalance(targetedUser.accountNumber	, targetedUser.balance);
									boolean receiverUpdated = AccountDAO.updateBalance(receiver.accountNumber, receiver.balance);
									
									if(senderUpdated && receiverUpdated) {
										String txnDesc="TRANSFERRED -"+trAmount+" TO ACCOUNT NUMBER "+receiver.accountNumber;
										AccountDAO.logTransaction(targetedUser.accountNumber, txnDesc);
										String recDesc="RECEIVED +"+trAmount+" FROM ACCOUNT NUMBER "+targetedUser.accountNumber;
										AccountDAO.logTransaction(receiver.accountNumber, recDesc);
										
										System.out.println("Transfer Successful! Both Accounts Updated in Database! 🎉");
									} else {
										System.out.println("Critical Error: Databse Sync Failed for One or Both Accounts!");
									}
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
						java.util.ArrayList<String> fullHistory = AccountDAO.getTransactionHistory(targetedUser.accountNumber);
						targetedUser.showTransactions(fullHistory);
						break;

					case "6":
						java.util.ArrayList<String> miniHistory = AccountDAO.getTransactionHistory(targetedUser.accountNumber);
						targetedUser.showMiniStatement(miniHistory);
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
							if(targetedUser.mpin.equals(oldMPIN) && !oldMPIN.equals(newMPIN)) {
								targetedUser.changeMPIN(oldMPIN, newMPIN);
								
								boolean mpinUpdated = AccountDAO.updateMPIN(targetedUser.accountNumber, newMPIN);
								if(mpinUpdated) {
									System.out.println("Database Sync Complete! New MPIN is Permanent!");
								} else {
									System.out.println("Database Sync Failed!");
								}
							} else {
								targetedUser.changeMPIN(oldMPIN, newMPIN);
							}
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