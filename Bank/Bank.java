package Bank;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

class UserFileHandeler {
    private String username;
    private File userfolder;

    UserFileHandeler(String name) {
        this.username = name;
        this.userfolder = new File("BankUsers2/" + this.username);
        if (userfolder.mkdirs()) {
            System.out.println("File created successfully");
        } else {
            System.out.println("The File already exists");
        }
    }
}

class User {
    private String name;
    private BigInteger password;
    private long accountnumber;
    Account account;

    User(String name, BigInteger password, long accountnumber) {
        this.name = name;
        this.accountnumber = accountnumber;
        this.account = new CurrentAccount(this.accountnumber, this);
        loadBalance();
        this.password = password;
        savePassword(); // No encryption, save directly
    }

    public void saveuser(String filename, String name, BigInteger password, long accountnumber, double balance) throws IOException {
        UserFileHandeler file = new UserFileHandeler(this.name);

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File("BankUsers2/" + this.name + "/" + filename)), StandardCharsets.UTF_8))) {
            writer.println("Username: " + name);
            writer.println("Account Number: " + this.accountnumber);
            writer.println("Password: " + this.password);
            writer.println("Balance: " + account.getBalance());

            System.out.println("Saved data to : " + filename);
        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    public void savePassword() {
        File userFolder = new File("BankUsers2/" + this.name);
        if (!userFolder.exists()) {
            userFolder.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(userFolder, "UserDetails.txt")), StandardCharsets.UTF_8))) {
            writer.println("Username: " + name);
            writer.println("Account Number: " + this.accountnumber);
            writer.println("Password: " + this.password);
            writer.println("Balance: " + account.getBalance());
            System.out.println("User details saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    public static BigInteger loadPasswordFromFile(String username) {
        File userFile = new File("BankUsers2/" + username + "/UserDetails.txt");

        if (userFile.exists()) {
            try (Scanner scanner = new Scanner(userFile, StandardCharsets.UTF_8)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();

                    if (line.startsWith("Password:")) {
                        String passwordStr = line.split(":")[1].trim();

                        try {
                            return new BigInteger(passwordStr);
                        } catch (NumberFormatException e) {
                            System.out.println("NumberFormatException: " + e.getMessage());
                            return null;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading password: " + e.getMessage());
            }
        }
        return null;
    }

    private void loadBalance() {
        File userFile = new File("BankUsers2/" + this.name + "/UserDetails.txt");
        if (userFile.exists()) {
            try (Scanner scanner = new Scanner(userFile, StandardCharsets.UTF_8)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("Balance:")) {
                        this.account.balance = Double.parseDouble(line.split(":")[1].trim());
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading user balance: " + e.getMessage());
            }
        }
    }

    public static long loadAccountNumberFromFile(String username) {
        File userFile = new File("BankUsers2/" + username + "/UserDetails.txt");
        if (userFile.exists()) {
            try (Scanner scanner = new Scanner(userFile, StandardCharsets.UTF_8)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("Account Number:")) {
                        return Long.parseLong(line.split(":")[1].trim());
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading user file: " + e.getMessage());
            }
        }
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getPassword() {
        return password;
    }

    public void setPassword(BigInteger password) {
        this.password = password;
        savePassword();
    }

    public long getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(long accountnumber) {
        this.accountnumber = accountnumber;
    }

    public boolean verifyPassword(BigInteger enteredPassword) {
        return enteredPassword.equals(this.password);
    }

    public boolean isPrivileged() {
        double threshhold = 1000000;
        return account.getBalance() >= threshhold;
    }

    public void saveLoanDetails(Loan loan) {
        File loanFolder = new File("BankUsers2/" + this.name + "/Loans");
        if (!loanFolder.exists()) {
            loanFolder.mkdirs();
        }
           long id = new Random().nextLong();
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(loanFolder, "Loan_" + id + ".txt"), true))) {
            writer.println("Loan Amount: " + loan.getPrincipalAmount());
            writer.println("Interest Rate: " + loan.intrestrate);
            writer.println("Tenure (Months): " + loan.getTenureMonth());
            writer.println("EMI: " + loan.getEmi());
            writer.println("----------");
            System.out.println("Loan details saved.");
        } catch (IOException e) {
            System.out.println("Error saving loan details: " + e.getMessage());
        }
    }

    public void displayLoanDetails() {
        File loanFolder = new File("BankUsers2/" + this.name + "/Loans");
        File[] loanFiles = loanFolder.listFiles();

        if (loanFiles != null && loanFiles.length > 0) {
            for (File file : loanFiles) {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        System.out.println(scanner.nextLine());
                    }
                } catch (IOException e) {
                    System.out.println("Error reading loan file: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No loan details found for user: " + this.name);
        }
    }

    public void applyforloan(double amount, int duration) {
        if (amount <= 0 || duration <= 0) {
            System.out.println("Enter the loan amount or duration");
            return;
        }

        double interestrate = isPrivileged() ? 0.05 : 0.08;

        Loan active = new Loan(amount, interestrate, duration);

        System.out.println("Loan Approved! Emi is : " + active.getEmi());
        saveLoanDetails(active);
    }

    public void DisplayUserDetails() {
        System.out.println("UserName : " + this.name);
        System.out.println("Account Number : " + this.accountnumber);
        System.out.println("Password : " + this.password);
        System.out.println("Balanced : " + account.getBalance());
    }
}

public class Bank {

    public static CurrentAccount getUserAccount(long accountNumber) {
        for (File userFolder : new File("BankUsers2").listFiles()) {
            if (userFolder.isDirectory()) {
                try (Scanner scanner = new Scanner(new File(userFolder, "UserDetails.txt"), StandardCharsets.UTF_8)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (line.startsWith("Account Number: " + accountNumber)) {
                            String username = userFolder.getName();
                            BigInteger password = User.loadPasswordFromFile(username);
                            long accNumber = User.loadAccountNumberFromFile(username);
                            User user = new User(username, password, accNumber);
                            return (CurrentAccount) user.account;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading user details: " + e.getMessage());
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Bank System!");

        while (true) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1. Create a new user");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\nEnter your name:");
                    String newName = sc.nextLine();

                    File userFolderNew = new File("BankUsers/" + newName);
                    if (userFolderNew.exists()) {
                        System.out.println("User already exists! Try logging in.");
                        break;
                    }

                    System.out.println("Enter your password:");
                    BigInteger newPassword = sc.nextBigInteger();
                    sc.nextLine();

                    System.out.println("Enter your account number:");
                    long newAccountNumber = sc.nextLong();
                    sc.nextLine();

                    User newUser = new User(newName, newPassword, newAccountNumber);

                    newUser.saveuser("UserDetails.txt", newName, newUser.getPassword(), newAccountNumber, newUser.account.getBalance());

                    System.out.println("Account created successfully! You can now log in.");
                    break;

                case 2:
                    System.out.println("\nEnter your name:");
                    String loginName = sc.nextLine();

                    File userFolder = new File("BankUsers2/" + loginName);
                    if (!userFolder.exists()) {
                        System.out.println("User does not exist! Please create a new user.");
                        break;
                    }

                    BigInteger storedpass = User.loadPasswordFromFile(loginName);

                    if (storedpass == null) {
                        System.out.println("User details not found or corrupted.");
                        break;
                    }

                    System.out.println("Enter your password:");
                    BigInteger loginPassword = sc.nextBigInteger();
                    sc.nextLine();

                    long accountNumberFromFile = User.loadAccountNumberFromFile(loginName);

                    User loggedInUser = new User(loginName, storedpass, accountNumberFromFile);

                    if (!loggedInUser.verifyPassword(loginPassword)) {
                        System.out.println("Invalid password. Try again.");
                        break;
                    }

                    System.out.println("Login successful! Welcome, " + loginName);

                    while (true) {
                        System.out.println("\nChoose an option:");
                        System.out.println("1. Display User Details");
                        System.out.println("2. Apply for a loan");
                        System.out.println("3. Perform a transaction");
                        System.out.println("4. Display Transaction History");
                        System.out.println("5. Change password");
                        System.out.println("6. Deposit money");
                        System.out.println("7. Withdraw money");
                        System.out.println("8. Logout");

                        int userChoice = sc.nextInt();
                        sc.nextLine();

                        switch (userChoice) {
                            case 1:
                                loggedInUser.DisplayUserDetails();
                                break;

                            case 2:
                                System.out.println("Enter loan amount:");
                                double amount = sc.nextDouble();
                                sc.nextLine();

                                System.out.println("Enter loan duration (months):");
                                int duration = sc.nextInt();
                                sc.nextLine();

                                loggedInUser.applyforloan(amount, duration);
                                break;

                            case 3:
                                System.out.println("Enter recipient's account number:");
                                long recipientAccountNumber = sc.nextLong();
                                sc.nextLine();

                                System.out.println("Enter amount to transfer:");
                                double transferAmount = sc.nextDouble();
                                sc.nextLine();

                                CurrentAccount senderAccount = (CurrentAccount) loggedInUser.account;
                                CurrentAccount recipientAccount = getUserAccount(recipientAccountNumber);

                                new Transaction(senderAccount, recipientAccount, transferAmount);
                                break;

                            case 4:
                                Transaction.showTransactionHistory(loginName);
                                break;

                            case 5:
                                loggedInUser.setPassword(new BigInteger(sc.nextLine()));
                                break;

                            case 6:
                                System.out.println("Enter amount to deposit:");
                                double depositAmount = sc.nextDouble();
                                sc.nextLine();
                                loggedInUser.account.deposit(depositAmount);
                                break;

                            case 7:
                                System.out.println("Enter amount to withdraw:");
                                double withdrawAmount = sc.nextDouble();
                                sc.nextLine();
                                loggedInUser.account.withdraw(withdrawAmount);
                                break;

                            case 8:
                                System.out.println("Logging out...");
                                break;

                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }

                        if (userChoice == 8) {
                            break;
                        }
                    }
                    break;

                case 3:
                    System.out.println("Thank you for using the bank system! Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}