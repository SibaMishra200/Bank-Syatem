package Bank;
import Bank.User;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Transaction{
    private long TransactionId;
    private CurrentAccount fromAccount;
    private CurrentAccount toAccount;
    private double amount;

    private LocalDateTime timestamp;

    public static final String userTransactionFolder = "BankUsers2/";

    Transaction(CurrentAccount fromAccount , CurrentAccount toAccount , double amount) throws IOException {

        this.TransactionId = new Random().nextLong();
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        boolean done = fromAccount.withdraw(this.amount);
        fromAccount.owner.saveuser("UserDetails.txt", fromAccount.owner.getName(), fromAccount.owner.getPassword(),fromAccount.owner.getAccountnumber(),fromAccount.getBalance());
        toAccount.deposit(this.amount);
        toAccount.owner.saveuser("UserDetails.txt",toAccount.owner.getName() , toAccount.owner.getPassword() , toAccount.owner.getAccountnumber() , toAccount.getBalance());
        this.saveTransaction();

    }

    public void saveTransaction(){

        String username = fromAccount.getOwner().getName();
        File userFolder = new File(userTransactionFolder + username + "/Transactions");
        if (!userFolder.exists()) {
            userFolder.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(userFolder, "Transaction_" + TransactionId + ".txt"), true))) {
            writer.println("Transaction ID: " + this.TransactionId);
            writer.println("From Account Number: " + fromAccount.accountnumber);
            writer.println("To Account Number: " + toAccount.accountnumber);
            writer.println("Amount Transferred: " + this.amount);
            writer.println("Timestamp: " + this.timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("----------");
            System.out.println("Transaction saved for user: " + username);
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }


    public static void showTransactionHistory(String username) {
        File userFolder = new File(userTransactionFolder + username + "/Transactions");
        File[] transactionFiles = userFolder.listFiles((dir, name) -> name.startsWith("Transaction_"));

        if (transactionFiles != null && transactionFiles.length > 0) {
            System.out.println("Transaction History for " + username + ":");
            for (File file : transactionFiles) {
                System.out.println("Details for transaction file: " + file.getName());
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        System.out.println(scanner.nextLine());
                    }
                    System.out.println("----------");
                } catch (IOException e) {
                    System.out.println("Error reading transaction file: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No transactions found for user: " + username);
        }
    }

}
