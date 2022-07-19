package DTO.lists;

import DTO.client.PaymentsNotificationsDTO;
import java.util.List;

public class PaymentNotificationsListDTO {
    private List<PaymentsNotificationsDTO> paymentsNotificationsDTOList;

    public List<PaymentsNotificationsDTO> getPaymentsNotificationsDTOList() {
        return paymentsNotificationsDTOList;
    }

    public void setPaymentsNotificationsDTOList(List<PaymentsNotificationsDTO> paymentsNotificationsDTOList) {
        this.paymentsNotificationsDTOList = paymentsNotificationsDTOList;
    }
}