package Bank;

import Bank.User;

import java.io.IOException;

public class CurrentAccount extends Account{

    private static final double INTEREST_RATE = 0.04;

    public CurrentAccount(long accountNumber, User owner) {
        super(accountNumber, owner);
    }

    @Override
    public void deposit(double amount) throws IOException {
        this.balance += amount;
        owner.saveuser("UserDetails.txt", owner.getName(), owner.getPassword(),owner.getAccountnumber(),this.balance);
        System.out.println("Amount Deposited");
    }

    @Override
    public boolean withdraw(double amount) throws IOException {
        if (this.balance < amount){
            System.out.println("Insufficient Balance");
            System.out.println("Money could not be withdrawn");
            return false;
        } else {
            this.balance -= amount;
            System.out.println("money withdrawn successfully");
            System.out.println("Current balance : "+this.balance);
            owner.saveuser("UserDetails.txt", owner.getName(), owner.getPassword(),owner.getAccountnumber(),this.balance);
            return true;
        }
    }

    public void addIntrest(){
        this.balance += this.balance * INTEREST_RATE;
    }
}



