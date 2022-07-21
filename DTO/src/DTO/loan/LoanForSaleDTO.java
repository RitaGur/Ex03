package DTO.loan;

public class LoanForSaleDTO {
    private int loanNumber;
    private String loanForSaleID;
    private String loanSellerName;
    private int loanPrice;

    public int getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(int loanNumber) {
        this.loanNumber = loanNumber;
    }

    public String getLoanForSaleID() {
        return loanForSaleID;
    }

    public String getLoanSellerName() {
        return loanSellerName;
    }

    public int getLoanPrice() {
        return loanPrice;
    }

    public void setLoanForSaleID(String loanForSaleID) {
        this.loanForSaleID = loanForSaleID;
    }

    public void setLoanSellerName(String loanSellerName) {
        this.loanSellerName = loanSellerName;
    }

    public void setLoanPrice(int loanPrice) {
        this.loanPrice = loanPrice;
    }
}
