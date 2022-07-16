package DTO.lists;

import DTO.loan.LoanInformationDTO;

import java.util.List;

public class LoanListDTO {
    private List<LoanInformationDTO> loanList;

    public List<LoanInformationDTO> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<LoanInformationDTO> loanList) {
        this.loanList = loanList;
    }
}
