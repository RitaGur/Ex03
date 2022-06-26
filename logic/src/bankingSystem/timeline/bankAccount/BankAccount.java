package bankingSystem.timeline.bankAccount;

import bankingSystem.timeline.bankClient.PaymentNotification;
import bankingSystem.timeline.loan.Loan;

import java.util.List;

public interface BankAccount {
    public void putMoneyInInvest();

    public void addMoneyToAccount(double i_AmountToAdd, int i_TransactionTimeUnit);

    public void withdrawMoneyFromAccount(double i_AmountToWithdraw, int i_TransactionTimeUnit) throws Exception;

    public List<RecentTransaction> getLastTransactions();

    public void addLastTransaction(int i_AmountOfTransaction, int i_TransactionTimeUnit);

    public double getAccountBalance();

    public String getClientName();

    void addAsLoanOwner(Loan loanToAdd);

    public List<Loan> getClientAsLenderSet();

    public List<Loan> getClientAsBorrowerSet();

    public void addPaymentNotification(String loanID, int paymentYaz, int sum);

    public List<PaymentNotification> getPaymentsNotificationList();

    public void removePaymentNotification(String loanID, int howManyToRemove);

    public boolean isNewPaymentNotificationExist(String selectedLoanID);

    public int getAmountOfNewNotifications();

    public int getIndexOfFirstNewNotification(int lastPaidTimeUnit);

    public int timeunitOfFirstUnPaidPayment();
}