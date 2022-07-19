package DTO.client;

public class PaymentsNotificationsDTO {
    private int paymentNotificationNumber;
    private String loanID;
    private int paymentYaz;
    private int sum;
    private Boolean newNotification;

    public PaymentsNotificationsDTO() {

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

    public void setPaymentNotificationNumber(int paymentNotificationNumber) {
        this.paymentNotificationNumber = paymentNotificationNumber;
    }

    public void setLoanID(String loanID) {
        this.loanID = loanID;
    }

    public void setPaymentYaz(int paymentYaz) {
        this.paymentYaz = paymentYaz;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public Boolean getNewNotification() {
        return newNotification;
    }

    public void setNewNotification(Boolean newNotification) {
        this.newNotification = newNotification;
    }
}
