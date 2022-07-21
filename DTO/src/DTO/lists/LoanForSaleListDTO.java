package DTO.lists;

import DTO.loan.LoanForSaleDTO;
import DTO.loan.LoanInformationDTO;

import java.util.List;

public class LoanForSaleListDTO {
    private List<LoanForSaleDTO> loanForSaleDTOList;
    private List<LoanInformationDTO> loanForSaleListInformationDTO;

    public List<LoanForSaleDTO> getLoanForSaleDTOList() {
        return loanForSaleDTOList;
    }

    public void setLoanForSaleDTOList(List<LoanForSaleDTO> loanForSaleDTOList) {
        this.loanForSaleDTOList = loanForSaleDTOList;
    }

    public List<LoanInformationDTO> getLoanForSaleListInformationDTO() {
        return loanForSaleListInformationDTO;
    }

    public void setLoanForSaleListInformationDTO(List<LoanInformationDTO> loanForSaleListInformationDTO) {
        this.loanForSaleListInformationDTO = loanForSaleListInformationDTO;
    }
}
