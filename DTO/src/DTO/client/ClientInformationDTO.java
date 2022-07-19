package DTO.client;

import DTO.loan.LoanInformationDTO;

import java.util.ArrayList;
import java.util.List;

public class ClientInformationDTO {
    private int clientNumber;
    private String clientName;
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

    public ClientInformationDTO() {

    }

 /*   public ClientInformationDTO(BankClient bankClient, int clientNumberI) {
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
    }*/

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

    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setRecentTransactionList(List<RecentTransactionDTO> recentTransactionList) {
        this.recentTransactionList = recentTransactionList;
    }

    public void setClientAsBorrowerLoanList(List<LoanInformationDTO> clientAsBorrowerLoanList) {
        this.clientAsBorrowerLoanList = clientAsBorrowerLoanList;
    }

    public void setPaymentsNotificationsList(List<PaymentsNotificationsDTO> paymentsNotificationsList) {
        this.paymentsNotificationsList = paymentsNotificationsList;
    }

    public void setNewBorrower(int newBorrower) {
        this.newBorrower = newBorrower;
    }

    public void setPendingBorrower(int pendingBorrower) {
        this.pendingBorrower = pendingBorrower;
    }

    public void setActiveBorrower(int activeBorrower) {
        this.activeBorrower = activeBorrower;
    }

    public void setRiskBorrower(int riskBorrower) {
        this.riskBorrower = riskBorrower;
    }

    public void setFinishedBorrower(int finishedBorrower) {
        this.finishedBorrower = finishedBorrower;
    }

    public void setClientAsLenderLoanList(List<LoanInformationDTO> clientAsLenderLoanList) {
        this.clientAsLenderLoanList = clientAsLenderLoanList;
    }

    public void setNewLender(int newLender) {
        this.newLender = newLender;
    }

    public void setPendingLender(int pendingLender) {
        this.pendingLender = pendingLender;
    }

    public void setActiveLender(int activeLender) {
        this.activeLender = activeLender;
    }

    public void setRiskLender(int riskLender) {
        this.riskLender = riskLender;
    }

    public void setFinishedLender(int finishedLender) {
        this.finishedLender = finishedLender;
    }

    public void setClientBalance(int clientBalance) {
        this.clientBalance = clientBalance;
    }
}
