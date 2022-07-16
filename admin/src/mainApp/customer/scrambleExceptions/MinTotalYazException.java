package mainApp.customer.scrambleExceptions;

public class MinTotalYazException extends Exception{
    private final String minTotalYazMessage;

    public MinTotalYazException(String minTotalYazMessage) {
        this.minTotalYazMessage = minTotalYazMessage;
    }

    public String getMinTotalYazMessage() {
        return minTotalYazMessage;
    }
}
