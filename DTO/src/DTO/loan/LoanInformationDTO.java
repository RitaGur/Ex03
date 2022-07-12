package DTO.loan;

import java.util.List;

public class LoanInformationDTO {
    private int loanNumber;
    private String loanNameID;
    private String borrowerName;
    private String loanCategory;
    private int loanStartSum;
    private int loanSumOfTimeUnit;
    private int loanInterest;
    private int timeUnitsBetweenPayments;
    private String loanStatus;
    private List<PartInLoanDTO> lenderSetAndAmounts;
    private int pendingMoney;
    private int missingMoneyToActive;
    private int beginningTimeUnit;
    private int endingTimeUnit;
    private int fundAmount;
    private int interestAmount;
    private int sumAmount;
    private int paidFund;
    private int paidInterest;
    private int fundLeftToPay;
    private int interestLeftToPay;
    private int nextPaymentTimeUnit;
    private int sumAmountToPayEveryTimeUnit;
    private List<PaymentsDTO> paymentsListInDTO;
    private int lastPaymentTimeunit;
    private int amountToPayNextPayment;
    private int fundToPayNextPayment;
    private int interestToPayNextPayment;
    private int numberOfUnpaidPayments;
    private int debt;

    public LoanInformationDTO() {

    }

   /* public LoanInformationDTO(Loan loan, int loanNumberI) {
        loanNumber = loanNumberI;
        loanNameID = loan.getLoanNameID();
        borrowerName = loan.getLoanOwner().getClientName();
        loanCategory = loan.getLoanCategory();
        loanStartSum = loan.getLoanStartSum();
        loanSumOfTimeUnit = loan.getSumOfTimeUnit();
        loanInterest = (int)(loan.getInterest() * 100);
        timeUnitsBetweenPayments = loan.getTimeUnitsBetweenPayment();
        loanStatus = loan.getLoanStatus().toString();
        lenderSetAndAmounts = lenderSetAndAmountInDTO(loan.getLendersSet());
        beginningTimeUnit = loan.getBeginningTimeUnit();
        endingTimeUnit = beginningTimeUnit + loanSumOfTimeUnit - 1;
        fundAmount = loan.getLoanStartSum();
        interestAmount = (int) Math.round(loan.interestLoanToPayAmount());
        sumAmount = fundAmount + interestAmount;
        paidFund = (int)Math.round(loan.loanPaidFund());
        paidInterest = (int)Math.round(loan.loanPaidInterest());
        fundLeftToPay = (int)Math.round(loan.loanFundLeftToPay());
        interestLeftToPay = (int)Math.round(loan.loanInterestLeftToPay());
        pendingMoney = loan.getPendingMoney();
        missingMoneyToActive = (int)(fundAmount - pendingMoney);
        nextPaymentTimeUnit = loan.getLastPaidTimeUnit() + timeUnitsBetweenPayments;
        sumAmountToPayEveryTimeUnit = (int)Math.round(loan.sumAmountToPayEveryTimeUnit());
        paymentsListInDTO = paymentsListInDTO(loan.getPaymentInfoList());
        lastPaymentTimeunit = loan.getLastPaidTimeUnit();
        amountToPayNextPayment = (int)Math.round(loan.amountOfNextPayment());
        fundToPayNextPayment = (int)Math.round(loan.fundOfNextPayment());
        interestToPayNextPayment = (int)Math.round(loan.interestOfNextPayment());
        numberOfUnpaidPayments = loan.howManyUnpaidPayments();
        debt = loan.getDebt();
    }*/

    public int getLastPaymentTimeunit() {
        return lastPaymentTimeunit;
    }

    public String getLoanNameID() {
        return loanNameID;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public String getLoanCategory() {
        return loanCategory;
    }

    public int getLoanStartSum() {
        return loanStartSum;
    }

    public int getLoanSumOfTimeUnit() {
        return loanSumOfTimeUnit;
    }

    public double getLoanInterest() {
        return loanInterest;
    }

    public int getTimeUnitsBetweenPayments() {
        return timeUnitsBetweenPayments;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public List<PartInLoanDTO> getLenderSetAndAmounts() {
        return lenderSetAndAmounts;
    }

    public int getPendingMoney() {
        return pendingMoney;
    }

    public int getBeginningTimeUnit() {
        return beginningTimeUnit;
    }

    public int getEndingTimeUnit() {
        return endingTimeUnit;
    }

    public int getFundAmount() {
        return fundAmount;
    }

    public int getInterestAmount() {
        return interestAmount;
    }

    public int getSumAmount() {
        return sumAmount;
    }

    public int getPaidFund() {
        return paidFund;
    }

    public int getPaidInterest() {
        return paidInterest;
    }

    public int getFundLeftToPay() {
        return fundLeftToPay;
    }

    public int getInterestLeftToPay() {
        return interestLeftToPay;
    }

    public int getNextPaymentTimeUnit() {
        return nextPaymentTimeUnit;
    }

    public int getSumAmountToPayEveryTimeUnit() {
        return sumAmountToPayEveryTimeUnit;
    }

    public List<PaymentsDTO> getPaymentsList() {
        return paymentsListInDTO;
    }

    public int getAmountToPayNextPayment() {
        return amountToPayNextPayment;
    }

    public int getFundToPayNextPayment() {
        return fundToPayNextPayment;
    }

    public int getInterestToPayNextPayment() {
        return interestToPayNextPayment;
    }

    public int getNumberOfUnpaidPayments() {
        return numberOfUnpaidPayments;
    }

    public int getMissingMoneyToActive() {
        return missingMoneyToActive;
    }

    public int getLoanNumber() {
        return loanNumber;
    }

    public int amountOfUnPaidPayments() {
        int sum = 0;

        for (PaymentsDTO singlePayment : paymentsListInDTO) {
            if (singlePayment.isWasItPaid() == false) {
                sum += singlePayment.getPaymentSum();
            }
        }

        return sum;
    }

    public void setStatus(String status) {
        loanStatus = status;
    }

    public int getDebt() {
        return debt;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public void setLenderSetAndAmounts(List<PartInLoanDTO> lenderSetAndAmounts) {
        this.lenderSetAndAmounts = lenderSetAndAmounts;
    }

    public void setPendingMoney(int pendingMoney) {
        this.pendingMoney = pendingMoney;
    }

    public void setMissingMoneyToActive(int missingMoneyToActive) {
        this.missingMoneyToActive = missingMoneyToActive;
    }

    public void setPaidFund(int paidFund) {
        this.paidFund = paidFund;
    }

    public void setPaidInterest(int paidInterest) {
        this.paidInterest = paidInterest;
    }

    public void setFundLeftToPay(int fundLeftToPay) {
        this.fundLeftToPay = fundLeftToPay;
    }

    public void setInterestLeftToPay(int interestLeftToPay) {
        this.interestLeftToPay = interestLeftToPay;
    }

    public void setNextPaymentTimeUnit(int nextPaymentTimeUnit) {
        this.nextPaymentTimeUnit = nextPaymentTimeUnit;
    }

    public void setSumAmountToPayEveryTimeUnit(int sumAmountToPayEveryTimeUnit) {
        this.sumAmountToPayEveryTimeUnit = sumAmountToPayEveryTimeUnit;
    }

    public void setPaymentsListInDTO(List<PaymentsDTO> paymentsListInDTO) {
        this.paymentsListInDTO = paymentsListInDTO;
    }

    public void setLastPaymentTimeunit(int lastPaymentTimeunit) {
        this.lastPaymentTimeunit = lastPaymentTimeunit;
    }

    public void setAmountToPayNextPayment(int amountToPayNextPayment) {
        this.amountToPayNextPayment = amountToPayNextPayment;
    }

    public void setFundToPayNextPayment(int fundToPayNextPayment) {
        this.fundToPayNextPayment = fundToPayNextPayment;
    }

    public void setInterestToPayNextPayment(int interestToPayNextPayment) {
        this.interestToPayNextPayment = interestToPayNextPayment;
    }

    public void setNumberOfUnpaidPayments(int numberOfUnpaidPayments) {
        this.numberOfUnpaidPayments = numberOfUnpaidPayments;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public void setLoanNumber(int loanNumber) {
        this.loanNumber = loanNumber;
    }

    public void setLoanNameID(String loanNameID) {
        this.loanNameID = loanNameID;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public void setLoanCategory(String loanCategory) {
        this.loanCategory = loanCategory;
    }

    public void setLoanStartSum(int loanStartSum) {
        this.loanStartSum = loanStartSum;
    }

    public void setLoanSumOfTimeUnit(int loanSumOfTimeUnit) {
        this.loanSumOfTimeUnit = loanSumOfTimeUnit;
    }

    public void setLoanInterest(int loanInterest) {
        this.loanInterest = loanInterest;
    }

    public void setTimeUnitsBetweenPayments(int timeUnitsBetweenPayments) {
        this.timeUnitsBetweenPayments = timeUnitsBetweenPayments;
    }

    public void setBeginningTimeUnit(int beginningTimeUnit) {
        this.beginningTimeUnit = beginningTimeUnit;
    }

    public void setEndingTimeUnit(int endingTimeUnit) {
        this.endingTimeUnit = endingTimeUnit;
    }

    public void setFundAmount(int fundAmount) {
        this.fundAmount = fundAmount;
    }

    public void setInterestAmount(int interestAmount) {
        this.interestAmount = interestAmount;
    }

    public void setSumAmount(int sumAmount) {
        this.sumAmount = sumAmount;
    }
}