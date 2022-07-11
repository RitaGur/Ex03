package DTO.loan;

public class PaymentsDTO {
    private int paymentNumber;
    private final int paymentTimeUnit;
    private final int fundPayment;
    private final int interestPayment;
    private final int paymentSum;
    private boolean wasItPaid;

    public PaymentsDTO(int paymentTimeUnit, double fundAtPayment, double interestAtPayment, int paymentSum, boolean wasItPaid, int paymentNumber) {
        this.paymentNumber = paymentNumber;
        this.paymentTimeUnit = paymentTimeUnit;
        this.fundPayment = (int)Math.round(fundAtPayment);
        this.interestPayment = (int)Math.round(interestAtPayment);
        this.paymentSum = paymentSum;
        this.wasItPaid = wasItPaid;
    }

    public int getPaymentTimeUnit() {
        return paymentTimeUnit;
    }

    public double getFundPayment() {
        return fundPayment;
    }

    public double getInterestPayment() {
        return interestPayment;
    }

    public int getPaymentSum() {
        return paymentSum;
    }

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public boolean isWasItPaid() {
        return wasItPaid;
    }
}
