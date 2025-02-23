package Bank;

import java.io.IOException;

abstract class Account{
    protected long accountnumber;
    protected double balance;
    protected User owner;

    public Account(long accountNumber, User owner) {
        this.accountnumber = accountNumber;
        this.owner = owner;
        this.balance = 0;
    }

    public abstract void deposit(double amount) throws IOException;
    public abstract boolean withdraw(double amount) throws IOException;
    public double getBalance(){
        return this.balance;
    }

    public User getOwner() {
        return this.owner;
    }
}

