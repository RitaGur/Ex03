package DTO.lists;

import DTO.loan.LoanInformationDTO;

public class LoanInfoAndPaymentAmountDTO {
    private LoanInformationDTO loanToPay;
    private int amountToPay;

    public LoanInformationDTO getLoanToPay() {
        return loanToPay;
    }

    public int getAmountToPay() {
        return amountToPay;
    }

    public void setLoanToPay(LoanInformationDTO loanToPay) {
        this.loanToPay = loanToPay;
    }

    public void setAmountToPay(int amountToPay) {
        this.amountToPay = amountToPay;
    }
}
