package mainApp.customer.scrambleExceptions;

public class MaxOpenLoansException extends Exception{
    private final String maxOpenLoansMessage;

    public MaxOpenLoansException(String maxOpenLoansMessage) {
        this.maxOpenLoansMessage = maxOpenLoansMessage;
    }

    public String getMaxOpenLoansMessage() {
        return maxOpenLoansMessage;
    }
}
