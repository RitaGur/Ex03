package DTO.loan.scramble;

import java.util.List;

public class InvestmentLoanInformationDTO {
    private String customerOfInvestmentName;
    private int amountOfMoneyToInvest;
    private List<String> chosenCategories;
    private int interest;
    private int minimumTotalTimeunits;
    private int maxOpenLoans;
    private int maxOwnershipPercentage;

    public InvestmentLoanInformationDTO() {

    }

    public int getMaxOwnershipPercentage() {
        return maxOwnershipPercentage;
    }

    public String getCustomerOfInvestmentName() {
        return customerOfInvestmentName;
    }

    public int getAmountOfMoneyToInvest() {
        return amountOfMoneyToInvest;
    }

    public List<String> getChosenCategories() {
        return chosenCategories;
    }

    public int getInterest() {
        return interest;
    }

    public int getMinimumTotalTimeunits() {
        return minimumTotalTimeunits;
    }

    public int getMaxOpenLoans() {
        return maxOpenLoans;
    }

    public void setCustomerOfInvestmentName(String customerOfInvestmentName) {
        this.customerOfInvestmentName = customerOfInvestmentName;
    }

    public void setAmountOfMoneyToInvest(int amountOfMoneyToInvest) {
        this.amountOfMoneyToInvest = amountOfMoneyToInvest;
    }

    public void setChosenCategories(List<String> chosenCategories) {
        this.chosenCategories = chosenCategories;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public void setMinimumTotalTimeunits(int minimumTotalTimeunits) {
        this.minimumTotalTimeunits = minimumTotalTimeunits;
    }

    public void setMaxOpenLoans(int maxOpenLoans) {
        this.maxOpenLoans = maxOpenLoans;
    }

    public void setMaxOwnershipPercentage(int maxOwnershipPercentage) {
        this.maxOwnershipPercentage = maxOwnershipPercentage;
    }
}
