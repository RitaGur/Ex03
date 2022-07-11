package DTO.loan;

public class PartInLoanDTO {
    private final int lenderNumber;
    private final String lenderName;
    private final int amountOfLoan;

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
}
