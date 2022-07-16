package mainApp.customer.scrambleExceptions;

public class MoneyToInvestException extends Exception{
    private final String moneyToInvestMessage;

    public MoneyToInvestException(String moneyToInvestMessage) {
        this.moneyToInvestMessage = moneyToInvestMessage;
    }

    public String getMoneyToInvestMessage() {
        return moneyToInvestMessage;
    }
}
