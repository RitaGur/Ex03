package DTO.lists;

import DTO.client.ClientInformationDTO;
import java.util.List;

public class CustomersListDTO {
    private List<ClientInformationDTO> customerList;

    public List<ClientInformationDTO> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<ClientInformationDTO> customerList) {
        this.customerList = customerList;
    }
}
