package DTO.refresher;

import DTO.client.ClientInformationDTO;
import DTO.loan.LoanInformationDTO;

import java.util.List;

public class ForAdminRefresherDTO {
    private List<LoanInformationDTO> adminLoanList;
    private List<ClientInformationDTO> adminCustomerList;
    private int currentYaz;

    public List<LoanInformationDTO> getAdminLoanList() {
        return adminLoanList;
    }

    public List<ClientInformationDTO> getAdminCustomerList() {
        return adminCustomerList;
    }

    public int getCurrentYaz() {
        return currentYaz;
    }

    public void setAdminLoanList(List<LoanInformationDTO> adminLoanList) {
        this.adminLoanList = adminLoanList;
    }

    public void setAdminCustomerList(List<ClientInformationDTO> adminCustomerList) {
        this.adminCustomerList = adminCustomerList;
    }

    public void setCurrentYaz(int currentYaz) {
        this.currentYaz = currentYaz;
    }
}
