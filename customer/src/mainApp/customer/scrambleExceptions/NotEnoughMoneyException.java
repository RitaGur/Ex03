package mainApp.customer.scrambleExceptions;

public class NotEnoughMoneyException extends Exception{
    private String notEnoughMoneyMessage;

    public NotEnoughMoneyException(String notEnoughMoneyMessage) {
        this.notEnoughMoneyMessage = notEnoughMoneyMessage;
    }

    public String getNotEnoughMoneyMessage() {
        return notEnoughMoneyMessage;
    }
}
