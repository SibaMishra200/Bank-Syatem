package Bank;

public class Loan {

    private double principalAmount;
    double intrestrate;
    private int tenureMonth;
    private double emi;

    public Loan(double principalAmount, double intrestrate, int tenureMonth) {
        this.principalAmount = principalAmount;
        this.intrestrate = intrestrate;
        this.tenureMonth = tenureMonth;
        this.emi = calculateEmi();
    }

    private double calculateEmi(){
        double monthIntrest = this.intrestrate/12;

        double ans = (this.principalAmount * monthIntrest * Math.pow((1+monthIntrest) , this.tenureMonth))/(Math.pow((1+monthIntrest) , this.tenureMonth) - 1);
        return ans;
    }

    public double getEmi(){
        return this.emi;
    }

    public int getTenureMonth(){
        return this.tenureMonth;
    }

    public double getPrincipalAmount() {
        return principalAmount;
    }



}
