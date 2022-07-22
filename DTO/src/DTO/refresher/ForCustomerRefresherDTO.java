package DTO.refresher;

import DTO.client.PaymentsNotificationsDTO;
import DTO.client.RecentTransactionDTO;
import DTO.lists.LoanForSaleListDTO;
import DTO.loan.LoanForSaleDTO;
import DTO.loan.LoanInformationDTO;

import java.util.List;

public class ForCustomerRefresherDTO {
    private String customerName;
    private List<LoanInformationDTO> customerLonerLoansList;
    private List<LoanInformationDTO> customerLenderLoansList;
    private List<RecentTransactionDTO> customerRecentTransactionsList;
    private int customerBalance;
    private List<LoanInformationDTO> customerPaymentLoanerLoansList;
    private List<PaymentsNotificationsDTO> customerPaymentNotificationsList;
    private int currentYaz;
    private List<String> loanCategoryList;
    private LoanForSaleListDTO loansForSaleList;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LoanForSaleListDTO getLoansForSaleList() {
        return loansForSaleList;
    }

    public void setLoansForSaleList(LoanForSaleListDTO loansForSaleList) {
        this.loansForSaleList = loansForSaleList;
    }

    public List<LoanInformationDTO> getCustomerLonerLoansList() {
        return customerLonerLoansList;
    }

    public List<LoanInformationDTO> getCustomerLenderLoansList() {
        return customerLenderLoansList;
    }

    public List<RecentTransactionDTO> getCustomerRecentTransactionsList() {
        return customerRecentTransactionsList;
    }

    public int getCustomerBalance() {
        return customerBalance;
    }

    public List<LoanInformationDTO> getCustomerPaymentLoanerLoansList() {
        return customerPaymentLoanerLoansList;
    }

    public List<PaymentsNotificationsDTO> getCustomerPaymentNotificationsList() {
        return customerPaymentNotificationsList;
    }

    public int getCurrentYaz() {
        return currentYaz;
    }

    public void setCurrentYaz(int currentYaz) {
        this.currentYaz = currentYaz;
    }

    public void setCustomerLonerLoansList(List<LoanInformationDTO> customerLonerLoansList) {
        this.customerLonerLoansList = customerLonerLoansList;
    }

    public void setCustomerLenderLoansList(List<LoanInformationDTO> customerLenderLoansList) {
        this.customerLenderLoansList = customerLenderLoansList;
    }

    public void setCustomerRecentTransactionsList(List<RecentTransactionDTO> customerRecentTransactionsList) {
        this.customerRecentTransactionsList = customerRecentTransactionsList;
    }

    public void setCustomerBalance(int customerBalance) {
        this.customerBalance = customerBalance;
    }

    public void setCustomerPaymentLoanerLoansList(List<LoanInformationDTO> customerPaymentLoanerLoansList) {
        this.customerPaymentLoanerLoansList = customerPaymentLoanerLoansList;
    }

    public void setCustomerPaymentNotificationsList(List<PaymentsNotificationsDTO> customerPaymentNotificationsList) {
        this.customerPaymentNotificationsList = customerPaymentNotificationsList;
    }

    public List<String> getLoanCategoryList() {
        return loanCategoryList;
    }

    public void setLoanCategoryList(List<String> loanCategoryList) {
        this.loanCategoryList = loanCategoryList;
    }
}