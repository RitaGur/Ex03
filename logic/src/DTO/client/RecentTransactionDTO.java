package DTO.client;

public class RecentTransactionDTO {
    private final int transActionNumber;
    private final int amountOfTransaction;
    private final int balanceBeforeTransaction;
    private final int balanceAfterTransaction;
    private final int transactionTimeUnit;
    private final char kindOfTransaction;

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
}
