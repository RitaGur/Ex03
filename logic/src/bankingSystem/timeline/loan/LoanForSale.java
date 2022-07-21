package bankingSystem.timeline.loan;

import bankingSystem.timeline.bankClient.BankClient;

public class LoanForSale {
    Loan loanForSale;
    BankClient loanSeller;
    int loanPrice;

    public LoanForSale(Loan loanForSale, BankClient loanSeller, int loanPrice) {
        this.loanForSale = loanForSale;
        this.loanSeller = loanSeller;
        this.loanPrice = loanPrice;
    }

    public Loan getLoanForSale() {
        return loanForSale;
    }

    public BankClient getLoanSeller() {
        return loanSeller;
    }

    public int getLoanPrice() {
        return loanPrice;
    }

    public void setLoanForSale(Loan loanForSale) {
        this.loanForSale = loanForSale;
    }
}
