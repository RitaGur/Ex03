package DTO.loan;

public class PartInLoanDTO {
    private int lenderNumber;
    private String lenderName;
    private int amountOfLoan;

    public PartInLoanDTO(String i_LenderName, int i_AmountOfLoan, int lenderNumber) {
        this.lenderNumber = lenderNumber;
        this.lenderName = i_LenderName;
        this.amountOfLoan = i_AmountOfLoan;
    }

    public String getLenderName() {
        return lenderName;
    }

    public int getAmountOfLoan() {
        return amountOfLoan;
    }

    public int getLenderNumber() {
        return lenderNumber;
    }

    public void setLenderNumber(int lenderNumber) {
        this.lenderNumber = lenderNumber;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public void setAmountOfLoan(int amountOfLoan) {
        this.amountOfLoan = amountOfLoan;
    }
}
