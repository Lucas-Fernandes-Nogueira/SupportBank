package training.supportbank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;

public class Bank {
    private static final Logger LOGGER = LogManager.getLogger(Bank.class);
    HashMap<String, Account> accounts = new HashMap<String, Account>();
    LinkedList<Transaction>  transactionHistory = new LinkedList<Transaction>();

    void addAccount(Account newAccount){
        this.accounts.put(newAccount.name, newAccount);
    }

    void addTransaction(Transaction newTransaction){
        this.transactionHistory.add(newTransaction);
        String toAccountName = newTransaction.toAccount;
        String fromAccountName = newTransaction.fromAccount;
        if (!this.accounts.containsKey(newTransaction.fromAccount)){
            this.accounts.put(fromAccountName, new Account(fromAccountName, -1*newTransaction.valueP));
        }
        else{
            this.accounts.put(fromAccountName, new Account(fromAccountName, this.accounts.get(newTransaction.fromAccount).creditP-newTransaction.valueP));
        }

        if (!this.accounts.containsKey(newTransaction.toAccount)) {
            this.accounts.put(toAccountName, new Account(toAccountName, newTransaction.valueP));
        } else {
            this.accounts.put(toAccountName, new Account(toAccountName, this.accounts.get(newTransaction.toAccount).creditP+newTransaction.valueP));
        }
    }

    void listAll(){
        for(Map.Entry<String, Account> entry : this.accounts.entrySet()){
            entry.getValue().printStatement();
        }
    }

    void listAccount(String accountName){
        System.out.println("Date | From | To | Narrative | Amount");
        for(Transaction entry : this.transactionHistory){
            if (entry.toAccount.equals(accountName)||entry.fromAccount.equals(accountName)){
                entry.printTransaction();
            }
        }
    }

    void addTransactionFromCSVFile(String fileName){
        try {
            LOGGER.info("Uploading " + fileName);
            BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
            String line = csvReader.readLine();
            for(line = csvReader.readLine(); line != null; line = csvReader.readLine()){
                try {
                    Transaction newTransaction = new Transaction(line);
                    addTransaction(newTransaction);
                } catch (Exception e) {
                    LOGGER.warn("Line not added.");
                }
            }
            csvReader.close();
        }
        catch (FileNotFoundException f){
            LOGGER.error("File" + fileName + "Not Found.");
        }
        catch (Exception e){
            LOGGER.error("Unexpected error.");
        }
    }

    boolean checkAccountExists(String accountName){
        for(Map.Entry<String, Account> entry : this.accounts.entrySet()){
            if(entry.getKey().equalsIgnoreCase(accountName)) return true;
        }
        return false;
    }

    boolean checkConservationOfMoney(){
        int total = 0;
        for(Map.Entry<String, Account> entry : this.accounts.entrySet()){
            total += entry.getValue().creditP;
        }
        if( total != 0){
            System.out.println("The laws of physics have been broken! Total is: " + total);
            return false;
        }
        else {
            return true;
        }
    }

    void bankInterface() {
        System.out.println("The SupportBank has opened.");
        Scanner input = new Scanner(System.in);
        System.out.println("Type \"List All\" to list all the accounts\nType \"List [account name]\" to list the transactions in a single account.");
        String inputString = input.nextLine();
        String[] inputWords = inputString.split("\\s+");
        String accountName = String.join(" ", Arrays.copyOfRange(inputWords, 1, inputWords.length));
        while (!inputWords[0].equalsIgnoreCase("List") || !(inputWords[1].equalsIgnoreCase("All")||checkAccountExists(accountName))) {
            System.out.println("Invalid input. Please type \"List All\" or \"List [account name]\"");
            inputString = input.nextLine();
            inputWords = inputString.split("\\s+");
            accountName = String.join(" ", Arrays.copyOfRange(inputWords, 1, inputWords.length));
        }
        if(inputWords[1].equalsIgnoreCase("All")){
            listAll();
        }
        else{
            listAccount(accountName);
        }
    }
}
