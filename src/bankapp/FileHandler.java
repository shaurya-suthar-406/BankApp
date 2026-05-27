package bankapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {

	static void saveData(ArrayList<Account> accounts) {
		try {
			FileWriter writer = new FileWriter("accounts.txt");
			for (Account a : accounts) {
				String allTransactions = String.join("|", a.transactions);
				writer.write(a.accountNumber + "," + a.name + "," + a.mpin + "," + a.balance + "," + a.isBlocked
						+ "," + allTransactions);
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Error Saving File!");
		}
	}

	static void loadData(ArrayList<Account> accounts) {
		try {
			File file = new File("accounts.txt");
			Scanner fileReader = new Scanner(file);
			while (fileReader.hasNextLine()) {
				String line = fileReader.nextLine();
				String[] parts = line.split(",",6);
				Account acc = new Account();
				acc.accountNumber = Integer.parseInt(parts[0].trim());
				acc.name = parts[1].trim();
				acc.mpin = parts[2].trim();
				acc.balance = Double.parseDouble(parts[3].trim());
				if (parts[4].equals("true") || parts[4].equals("false")) {
					acc.isBlocked = Boolean.parseBoolean(parts[4]);
					if (parts.length > 5 && !parts[5].trim().isEmpty()) {
						String[] txns = parts[5].split("\\|");
						for (String t : txns) {
							acc.transactions.add(t);
						}
					}
				} else {
					acc.isBlocked = false;
					String[] txns = parts[4].split("\\|");
					for (String t : txns) {
						acc.transactions.add(t);
					}
				}
				accounts.add(acc);
				if(acc.accountNumber>=Account.nextAccountNumber) {
					Account.nextAccountNumber=acc.accountNumber+1;
				}

			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
		}
	}
}
