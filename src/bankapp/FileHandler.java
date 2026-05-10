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
			for(Account a:accounts) {
				writer.write(
						a.accountNumber+", "+
						a.name+", "+
						a.mpin+", "+
						a.balance
				);
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
			while(fileReader.hasNextLine()) {
				String line=fileReader.nextLine();
				String[] parts=line.split(", ");
				Account acc=new Account();
				acc.accountNumber=Integer.parseInt(parts[0]);
				acc.name=parts[1];
				acc.mpin=parts[2];
				acc.balance=Double.parseDouble(parts[3]);
				accounts.add(acc);
				if(acc.accountNumber>=Account.nextAccountNumber) {
					Account.nextAccountNumber = acc.accountNumber + 1;
				}
			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
		}
	}
}
