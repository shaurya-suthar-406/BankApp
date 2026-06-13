package bankapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountDAO {

    public static boolean saveAccount(Account acc) {
        // 1. We mapped "password" in the DB column to "mpin" from your Java class
        String sql = "INSERT INTO accounts (account_number, name, balance, password) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 2. Direct field access matching your Account.java naming exactly
            pstmt.setInt(1, acc.accountNumber); 
            pstmt.setString(2, acc.name);          
            pstmt.setDouble(3, acc.balance);      
            pstmt.setString(4, acc.mpin); // Changed from password to mpin
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; 
            
        } catch (SQLException e) {
            System.out.println("Error saving account to DB: " + e.getMessage());
            return false;
        }
    }
    
    public static Account getAccount(int accNumber, String enteredMpin) {
        // 1. Blueprint: Look for a row matching BOTH the account number and the mpin
        String sql = "SELECT * FROM accounts WHERE account_number = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 2. Map the login credentials to the question marks
            pstmt.setInt(1, accNumber);
            pstmt.setString(2, enteredMpin);
            
            // 3. Execute the query. SELECT queries return data inside a ResultSet container
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                
                // 4. If rs.next() is true, MySQL found a matching row!
                if (rs.next()) {
                    // Create a blank Java Account object
                    Account acc = new Account();
                    
                    // Reconstruct the object using the data from the database row
                    acc.accountNumber = rs.getInt("account_number");
                    acc.name = rs.getString("name");
                    acc.balance = rs.getDouble("balance");
                    acc.mpin = rs.getString("password");
                    
                    return acc; // Return the fully populated account object
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error loading account from DB: " + e.getMessage());
        }
        
        return null; // Return null if no account matched or if an error occurred
    }
    
    public static Account getAccount(int accNumber) {
    	String sql = "SELECT * FROM accounts WHERE account_number = ?";
    	try(Connection conn = DatabaseConnection.getConnection();
    			PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.setInt(1, accNumber);
    		try(java.sql.ResultSet rs = pstmt.executeQuery()) {
    			if(rs.next()) {
    				Account acc = new Account();
    				acc.accountNumber = rs.getInt("account_number");
    				acc.name = rs.getString("name");
    				acc.balance = rs.getDouble("balance");
    				acc.mpin = rs.getString("password");
    				return acc;
    			}
    		}
    	} catch (SQLException e) {
    		System.out.println("Error fetching receiver Account: "+e.getMessage());
    	}
    	return null;
    }
    
    public static boolean updateBalance(int accNumber, double newBalance) {
    	String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
    	try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
        	pstmt.setInt(2, accNumber);
        	pstmt.setDouble(1, newBalance);
        	
        	int rowsAffected = pstmt.executeUpdate();
        	
        	return rowsAffected > 0;
    	}
        catch (SQLException e) {
    		System.out.println("Error updating balance: " + e.getMessage());
    	}
    return false;
    }
    
    public static int getNextAccountNumber() {
        String sql = "SELECT MAX(account_number) FROM accounts";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                int maxId = rs.getInt(1);
                if (maxId > 0) {
                    return maxId + 1; // Next number is highest + 1
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching next account number: " + e.getMessage());
        }
        return 1001; // Default fallback if table is empty
    }
    
    public static boolean logTransaction(int accNumber, String desc) {
    	String sql = "INSERT INTO transactions (account_number, description) VALUES (?,?)";
    	try (Connection conn = DatabaseConnection.getConnection();
    			PreparedStatement pstmt = conn.prepareStatement(sql);){
    		pstmt.setInt(1, accNumber);
    		pstmt.setString(2, desc);
    		int rowsInserted = pstmt.executeUpdate();
    		return rowsInserted > 0;
    	} catch (Exception e) {
			System.out.println("Error Logging The Transaction: " + e.getMessage());
			return false;
		}
    }
    
    public static ArrayList<String> getTransactionHistory(int accNumber){
    	ArrayList<String> list = new ArrayList<>();
    	String sql = "SELECT description FROM transactions WHERE account_number=? ORDER BY transaction_id ASC";
    	try (Connection conn = DatabaseConnection.getConnection();
    			PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setInt(1, accNumber);
			try(java.sql.ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					String desc = rs.getString("description");
					list.add(desc);
				}
			}
		} catch (Exception e) {
			System.out.println("Error fetching Transaction History: "+e.getMessage());
		}
    	return list;
    }
    
    public static boolean updateMPIN(int accNumber, String newMpin) {
    	String sql = "UPDATE accounts SET password = ? WHERE account_number = ?";
    	try(Connection conn = DatabaseConnection.getConnection();
    			PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, newMpin);
			pstmt.setInt(2, accNumber);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.out.println("Error updating MPIN in Database: "+e.getMessage());
			return false;
		}
    }
}