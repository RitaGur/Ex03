package DTO.loan;

import bankingSystem.timeline.loan.Loan;
import bankingSystem.timeline.loan.PartInLoan;
import bankingSystem.timeline.loan.PaymentInfo;

import java.util.ArrayList;
import java.util.List;

public class LoanInformationDTO {
    private final int loanNumber;
    private final String loanNameID;
    private final String borrowerName;
    private final String loanCategory;
    private final int loanStartSum;
    private final int loanSumOfTimeUnit;
    private final int loanInterest;
    private final int timeUnitsBetweenPayments;
    private String loanStatus;
    private List<PartInLoanDTO> lenderSetAndAmounts;
    private int pendingMoney;
    private int missingMoneyToActive;
    private final int beginningTimeUnit;
    private final int endingTimeUnit;
    private final int fundAmount;
    private final int interestAmount;
    private final int sumAmount;
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

    public LoanInformationDTO(Loan loan, int loanNumberI) {
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
    }

    private List<PaymentsDTO> paymentsListInDTO(List<PaymentInfo> i_paymentsInfoSet) {
        List<PaymentsDTO> listToReturn = new ArrayList<>();
        int count = 1;

        for (PaymentInfo i_PaymentInfo : i_paymentsInfoSet) {
            listToReturn.add(new PaymentsDTO(i_PaymentInfo.getPaymentTimeUnit(), i_PaymentInfo.getFundPayment(),
                    i_PaymentInfo.getInterestPayment(), i_PaymentInfo.getPaymentSum(), i_PaymentInfo.isWasItPaid(), count++));
        }

        return listToReturn;
    }

    private List<PartInLoanDTO> lenderSetAndAmountInDTO(List<PartInLoan> i_LenderSetAndAmount) {
        List<PartInLoanDTO> setToReturn = new ArrayList<>();
        int count = 1;

        for (PartInLoan i_PartInLoan : i_LenderSetAndAmount) {
            setToReturn.add(new PartInLoanDTO(i_PartInLoan.getLender().getClientName(), i_PartInLoan.getAmountOfLoan(), count++));
        }

        return setToReturn;
    }

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

    public double getInterestAmount() {
        return interestAmount;
    }

    public double getSumAmount() {
        return sumAmount;
    }

    public double getPaidFund() {
        return paidFund;
    }

    public double getPaidInterest() {
        return paidInterest;
    }

    public double getFundLeftToPay() {
        return fundLeftToPay;
    }

    public double getInterestLeftToPay() {
        return interestLeftToPay;
    }

    public int getNextPaymentTimeUnit() {
        return nextPaymentTimeUnit;
    }

    public double getSumAmountToPayEveryTimeUnit() {
        return sumAmountToPayEveryTimeUnit;
    }

    public List<PaymentsDTO> getPaymentsList() {
        return paymentsListInDTO;
    }

    public double getAmountToPayNextPayment() {
        return amountToPayNextPayment;
    }

    public double getFundToPayNextPayment() {
        return fundToPayNextPayment;
    }

    public double getInterestToPayNextPayment() {
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

    public double amountOfUnPaidPayments() {
        int sum = 0;

        for (PaymentsDTO singlePayment : paymentsListInDTO) {
            if (singlePayment.isWasItPaid() == false) {
                sum += singlePayment.getPaymentSum();
            }
        }

        return sum;
    }

    public void setStatus(String pending) {
        loanStatus = "PENDING";
    }

    public int getDebt() {
        return debt;
    }
}
