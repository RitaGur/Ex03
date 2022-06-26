package DTO.client;

import bankingSystem.timeline.bankClient.PaymentNotification;

public class PaymentsNotificationsDTO {
    private final int paymentNotificationNumber;
    private final String loanID;
    private final int paymentYaz;
    private final int sum;

    public PaymentsNotificationsDTO(PaymentNotification paymentNotification, int paymentNotificationNumber) {
        this.paymentNotificationNumber = paymentNotificationNumber;
        this.loanID = paymentNotification.getLoanID();
        this.paymentYaz = paymentNotification.getPaymentYaz();
        this.sum = paymentNotification.getSum();
    }

    public int getPaymentNotificationNumber() {
        return paymentNotificationNumber;
    }

    public String getLoanID() {
        return loanID;
    }

    public int getPaymentYaz() {
        return paymentYaz;
    }

    public int getSum() {
        return sum;
    }
}
