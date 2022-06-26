package bankingSystem.timeline.bankClient;

public class PaymentNotification {
    private String loanID;
    private int paymentYaz;
    private int sum;
    private Boolean newNotification;

    public PaymentNotification(String loanID, int paymentYaz, int sum) {
        this.loanID = loanID;
        this.paymentYaz = paymentYaz;
        this.sum = sum;
        this.newNotification = true;
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

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void setNewNotification(Boolean old) {
        this.newNotification = old;
    }

    public Boolean getNewNotification() {
        return newNotification;
    }
}
