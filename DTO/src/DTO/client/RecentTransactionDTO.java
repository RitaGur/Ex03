package DTO.client;

public class RecentTransactionDTO {
    private int transActionNumber;
    private int amountOfTransaction;
    private int balanceBeforeTransaction;
    private int balanceAfterTransaction;
    private int transactionTimeUnit;
    private char kindOfTransaction;

    public RecentTransactionDTO(double f_AmountOfTransaction, double i_BalanceBeforeTransaction, double i_BalanceAfterTransaction, int i_TransactionTimeUnit, char i_KindOfTransaction, int transactionNumber) {
        this.transActionNumber = transactionNumber;
        this.amountOfTransaction = (int)Math.round(f_AmountOfTransaction);
        this.balanceBeforeTransaction = (int)Math.round(i_BalanceBeforeTransaction);
        this.balanceAfterTransaction = (int)Math.round(i_BalanceAfterTransaction);
        this.transactionTimeUnit = i_TransactionTimeUnit;
        this.kindOfTransaction = i_KindOfTransaction;
    }

    public double getAmountOfTransaction() {
        return amountOfTransaction;
    }

    public double getBalanceBeforeTransaction() {
        return balanceBeforeTransaction;
    }

    public double getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public int getTransactionTimeUnit() {
        return transactionTimeUnit;
    }

    public char getKindOfTransaction() {
        return kindOfTransaction;
    }

    public int getTransActionNumber() {
        return transActionNumber;
    }

    public void setTransActionNumber(int transActionNumber) {
        this.transActionNumber = transActionNumber;
    }

    public void setAmountOfTransaction(int amountOfTransaction) {
        this.amountOfTransaction = amountOfTransaction;
    }

    public void setBalanceBeforeTransaction(int balanceBeforeTransaction) {
        this.balanceBeforeTransaction = balanceBeforeTransaction;
    }

    public void setBalanceAfterTransaction(int balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public void setTransactionTimeUnit(int transactionTimeUnit) {
        this.transactionTimeUnit = transactionTimeUnit;
    }

    public void setKindOfTransaction(char kindOfTransaction) {
        this.kindOfTransaction = kindOfTransaction;
    }
}
