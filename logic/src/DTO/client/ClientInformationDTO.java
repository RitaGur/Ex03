package DTO.client;

import DTO.loan.LoanInformationDTO;
import bankingSystem.timeline.bankAccount.RecentTransaction;
import bankingSystem.timeline.bankClient.BankClient;
import bankingSystem.timeline.bankClient.PaymentNotification;
import bankingSystem.timeline.loan.Loan;

import java.util.ArrayList;
import java.util.List;

public class ClientInformationDTO {
    private final int clientNumber;
    private final String clientName;
    private List<RecentTransactionDTO> recentTransactionList;
    private List<LoanInformationDTO> clientAsBorrowerLoanList;
    private List<PaymentsNotificationsDTO> paymentsNotificationsList;
    private int newBorrower; // number of these loans, of new loans as borrower
    private int pendingBorrower;
    private int activeBorrower;
    private int riskBorrower;
    private int finishedBorrower;
    private List<LoanInformationDTO> clientAsLenderLoanList;
    private int newLender;
    private int pendingLender;
    private int activeLender;
    private int riskLender;
    private int finishedLender;
    private int clientBalance;

    public ClientInformationDTO(BankClient bankClient, int clientNumberI) {
        clientNumber = clientNumberI;
        this.clientName = bankClient.getClientName();
        this.recentTransactionList = recentTransactionListDTO(bankClient.getLastTransactions());
        this.clientAsBorrowerLoanList = clientLoanListDTO(bankClient.getClientAsBorrowerSet());
        this.clientAsLenderLoanList = clientLoanListDTO(bankClient.getClientAsLenderSet());
        this.paymentsNotificationsList = paymentsNotificationListDTO(bankClient.getPaymentsNotificationList());
        this.clientBalance = (int)Math.round(bankClient.getAccountBalance());
        newBorrower = bankClient.howManyInBorrower("NEW");
        pendingBorrower = bankClient.howManyInBorrower("PENDING");
        activeBorrower = bankClient.howManyInBorrower("ACTIVE");
        riskBorrower = bankClient.howManyInBorrower("RISK");
        finishedBorrower = bankClient.howManyInBorrower("FINISHED");
        newLender = bankClient.howManyInLender("NEW");
        pendingLender = bankClient.howManyInLender("PENDING");
        activeLender = bankClient.howManyInLender("ACTIVE");
        riskLender = bankClient.howManyInLender("RISK");
        finishedLender = bankClient.howManyInLender("FINISHED");
    }

    private List<PaymentsNotificationsDTO> paymentsNotificationListDTO(List<PaymentNotification> paymentsNotificationList) {
        List<PaymentsNotificationsDTO> paymentsNotificationListInDTO = new ArrayList<>();
        int counter = 1;

        for (PaymentNotification paymentNotification : paymentsNotificationList) {
            paymentsNotificationListInDTO.add(new PaymentsNotificationsDTO(paymentNotification, counter++));
        }

        return paymentsNotificationListInDTO;
    }

    private List<LoanInformationDTO> clientLoanListDTO(List<Loan> clientSet) {
        List<LoanInformationDTO> setToReturn = new ArrayList<>();
        int counter = 1;

        for (Loan loan : clientSet) {
            setToReturn.add(new LoanInformationDTO(loan, counter++));
        }

        return setToReturn;
    }

    private List<RecentTransactionDTO> recentTransactionListDTO(List<RecentTransaction> lastTransactions) {
        List<RecentTransactionDTO> setToReturn = new ArrayList<>();
        int count = 1;

        for (RecentTransaction recentTransaction : lastTransactions) {
            setToReturn.add(new RecentTransactionDTO(recentTransaction.getAmountOfTransaction(),recentTransaction.getBalanceBeforeTransaction(),
                    recentTransaction.getBalanceAfterTransaction(), recentTransaction.getTransactionTimeUnit(), recentTransaction.getKindOfTransaction(), count++));
        }

        return setToReturn;
    }

    public String getClientName() {
        return clientName;
    }

    public List<RecentTransactionDTO> getRecentTransactionList() {
        return recentTransactionList;
    }

    public List<LoanInformationDTO> getClientAsBorrowerLoanList() {
        return clientAsBorrowerLoanList;
    }

    public List<LoanInformationDTO> getClientAsLenderLoanList() {
        return clientAsLenderLoanList;
    }

    public double getClientBalance() {
        return clientBalance;
    }


    public int getClientNumber() {
        return clientNumber;
    }

    public int getNewBorrower() {
        return newBorrower;
    }

    public int getPendingBorrower() {
        return pendingBorrower;
    }

    public int getActiveBorrower() {
        return activeBorrower;
    }

    public int getRiskBorrower() {
        return riskBorrower;
    }

    public int getFinishedBorrower() {
        return finishedBorrower;
    }

    public int getNewLender() {
        return newLender;
    }

    public int getPendingLender() {
        return pendingLender;
    }

    public int getActiveLender() {
        return activeLender;
    }

    public int getRiskLender() {
        return riskLender;
    }

    public int getFinishedLender() {
        return finishedLender;
    }

    public List<PaymentsNotificationsDTO> getPaymentsNotificationsList() {
        return paymentsNotificationsList;
    }
}
