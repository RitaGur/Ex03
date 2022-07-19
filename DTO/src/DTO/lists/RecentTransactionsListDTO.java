package DTO.lists;

import DTO.client.RecentTransactionDTO;
import java.util.List;

public class RecentTransactionsListDTO {
    private List<RecentTransactionDTO> recentTransactionsListDTO;

    public List<RecentTransactionDTO> getRecentTransactionsListDTO() {
        return recentTransactionsListDTO;
    }

    public void setRecentTransactionsListDTO(List<RecentTransactionDTO> recentTransactionsListDTO) {
        this.recentTransactionsListDTO = recentTransactionsListDTO;
    }
}