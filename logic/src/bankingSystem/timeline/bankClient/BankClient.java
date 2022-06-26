package bankingSystem.timeline.bankClient;

import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.bankAccount.RecentTransaction;
import bankingSystem.timeline.loan.Loan;

import java.util.ArrayList;
import java.util.List;

public class BankClient implements BankAccount {
    private final String f_ClientName;
    private double m_AccountBalance;
    private List<Loan> m_ClientAsLenderSet;
    private List<Loan> m_ClientAsBorrowerSet;
    private List<RecentTransaction> m_RecentTransactionList;
    private List<PaymentNotification> paymentsNotificationList;

    public BankClient(int i_AccountBalance, String i_ClientName) {
        f_ClientName = i_ClientName;
        m_AccountBalance = i_AccountBalance;
        m_ClientAsBorrowerSet = new ArrayList<>();
        m_ClientAsLenderSet = new ArrayList<>();
        m_RecentTransactionList = new ArrayList<>();
        paymentsNotificationList = new ArrayList<>();
    }

    @Override
    public double getAccountBalance() {
        return m_AccountBalance;
    }

    @Override
    public void putMoneyInInvest() {
        // m_LenderSet
    }

    @Override
    public void addMoneyToAccount(double i_AmountToAdd, int i_TransactionTimeUnit) {
        m_RecentTransactionList.add(new RecentTransaction(i_AmountToAdd, m_AccountBalance, i_TransactionTimeUnit));
        m_AccountBalance += i_AmountToAdd;
    }

    @Override
    public void withdrawMoneyFromAccount(double i_AmountToWithdraw, int i_TransactionTimeUnit) throws Exception {
        if (i_AmountToWithdraw > m_AccountBalance) {
            throw new Exception("The client does not have enough money in the account.");
        }
        m_RecentTransactionList.add(new RecentTransaction(i_AmountToWithdraw * (-1), m_AccountBalance, i_TransactionTimeUnit));
        m_AccountBalance -= i_AmountToWithdraw;
    }

    @Override
    public List<RecentTransaction> getLastTransactions() {
        return m_RecentTransactionList;
    }

    @Override
    public void addLastTransaction(int i_AmountOfTransaction, int i_TransactionTimeUnit) {
        m_RecentTransactionList.add(new RecentTransaction(i_AmountOfTransaction, m_AccountBalance, i_TransactionTimeUnit));
    }

    @Override
    public String getClientName() {
        return f_ClientName;
    }

    @Override
    public void addAsLoanOwner(Loan loanToAdd) { //as borrower
        m_ClientAsBorrowerSet.add(loanToAdd);
    }

    public void addAsLender(Loan loanToAdd) {
        m_ClientAsLenderSet.add(loanToAdd);
    }

    @Override
    public List<Loan> getClientAsLenderSet() {
        return m_ClientAsLenderSet;
    }

    @Override
    public List<Loan> getClientAsBorrowerSet() {
        return m_ClientAsBorrowerSet;
    }

    public int howManyInBorrower(String loanStatus) {
        int counter = 0;
        for (Loan loan : m_ClientAsBorrowerSet) {
            if (loan.getLoanStatus().toString().equals(loanStatus)) {
                counter++;
            }
        }

        return counter;
    }

    public int howManyInLender(String loanStatus) {
        int counter = 0;
        for (Loan loan : m_ClientAsLenderSet) {
            if (loan.getLoanStatus().toString().equals(loanStatus)) {
                counter++;
            }
        }

        return counter;
    }

    @Override
    public void addPaymentNotification(String loanID, int paymentYaz, int sum) {
        paymentsNotificationList.add(new PaymentNotification(loanID, paymentYaz, sum));
    }

    public void removePaymentNotification(String loanID, int howManyToRemove) {
        int counter = 0;

        if ( howManyToRemove != 0) { // if the loaner pays less than one payment
            for (PaymentNotification paymentNotification : paymentsNotificationList) {
                if (paymentNotification.getLoanID().equals(loanID)) {
                    paymentsNotificationList.remove(paymentNotification);
                    counter++;
                    if (howManyToRemove == counter) {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public List<PaymentNotification> getPaymentsNotificationList() {
        return paymentsNotificationList;
    }

    @Override
    public boolean isNewPaymentNotificationExist(String selectedLoanID) {
        for (PaymentNotification paymentNotification : paymentsNotificationList) {
            if (paymentNotification.getLoanID().equals(selectedLoanID) && paymentNotification.getNewNotification()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getAmountOfNewNotifications() {
        int numberOfNewNotifications = 0;

        for (PaymentNotification paymentNotification : paymentsNotificationList) {
            if (paymentNotification.getNewNotification()) {
                numberOfNewNotifications++;
            }
        }

        return numberOfNewNotifications;
    }

    @Override
    public int getIndexOfFirstNewNotification(int firstUnPaidTimeunit) {
        int index = 0;
        for (PaymentNotification paymentNotification : paymentsNotificationList) {
            if (paymentNotification.getNewNotification() && (paymentNotification.getPaymentYaz() == firstUnPaidTimeunit)) {
                return index;
            }
            index++;
        }

        return -1;
    }

    @Override
    public int timeunitOfFirstUnPaidPayment() {
        for (PaymentNotification paymentNotification : paymentsNotificationList) {
            if (paymentNotification.getNewNotification()) {
                return paymentNotification.getPaymentYaz();
            }
        }

        return 0;
    }

}