package bankingSystem.timeline.loan;

import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.bankClient.BankClient;
import bankingSystem.timeline.bankClient.PaymentNotification;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class Loan {
    private final String f_LoanNameID;
    private final BankAccount f_LoanOwner; //The Borrower
    private final int f_LoanStartSum;
    private final int f_SumOfTimeUnit; // how many yaz - loan duration
    private final int f_TimeUnitsBetweenPayment; // how often (by yaz) you pay
    private final double f_Interest; //ribit - decimal number (0, 100]
    private Payment m_Payment;
    private List<PartInLoan> m_LendersAndAmountsSet;
    private LoanStatus m_LoanStatus;
    private int m_PendingMoney = 0; // raised money before activation
    private int m_BeginningTimeUnit;
    private int m_EndingTimeunit;
    private int m_LastPaidTimeUnit = 0;
    private final String f_LoanCategory;
    private List<PaymentInfo> m_PaymentInfoList;
    private int timeunitOfFirstUnPaidPayment;

    public Loan(String i_LoanNameID, BankAccount i_LoanOwner, int i_LoanStartSum, int i_SumOfTimeUnit,
                int i_HowOftenToPay, double i_Interest, String i_LoanCategory) {
        f_LoanOwner = i_LoanOwner;
        f_LoanNameID = i_LoanNameID;
        f_LoanStartSum = i_LoanStartSum;
        f_SumOfTimeUnit = i_SumOfTimeUnit;
        f_TimeUnitsBetweenPayment = i_HowOftenToPay;
        f_Interest = 0.01 * i_Interest;
        m_LendersAndAmountsSet = new ArrayList<>();
        m_Payment = new Payment(f_LoanStartSum, f_Interest, f_SumOfTimeUnit, f_TimeUnitsBetweenPayment);
        m_LoanStatus = LoanStatus.NEW;
        m_BeginningTimeUnit = -1;
        f_LoanCategory = i_LoanCategory;
        m_PaymentInfoList = new ArrayList<>();
        m_EndingTimeunit = m_BeginningTimeUnit + f_SumOfTimeUnit - 1;
        timeunitOfFirstUnPaidPayment = m_BeginningTimeUnit;
    }


    public int getLastPaidTimeUnit() {
        return m_LastPaidTimeUnit;
    }

    public void updateEndingTimeunit(int i_CurrentTimeunit) {
        m_EndingTimeunit = i_CurrentTimeunit;
    }

    public int howManyTimeUnitsLeftForLoan(int i_CurrentTimeUnit) {
        return f_SumOfTimeUnit - i_CurrentTimeUnit;
    }

    public void addLender(BankClient i_NewLender, int i_AmountOfLoan) {
        PartInLoan existingPartOfLender = findPartInLoanByLender(i_NewLender);

        if (existingPartOfLender == null) {
            m_LendersAndAmountsSet.add(new PartInLoan(i_NewLender, i_AmountOfLoan, f_LoanStartSum, m_Payment.getSumLeftToPay(), f_SumOfTimeUnit, f_TimeUnitsBetweenPayment));
            i_NewLender.addAsLender(this);
        } else { //this lender already exist
            int newAmountOfLoan = i_AmountOfLoan + existingPartOfLender.getAmountOfLoan();
            m_LendersAndAmountsSet.remove(existingPartOfLender);
            m_LendersAndAmountsSet.add(new PartInLoan(i_NewLender, newAmountOfLoan, f_LoanStartSum, m_Payment.getSumLeftToPay(), f_SumOfTimeUnit, f_TimeUnitsBetweenPayment));
        }
    }

    private PartInLoan findPartInLoanByLender(BankClient i_newLender) {
        PartInLoan partToReturn = null;

        for (PartInLoan part : m_LendersAndAmountsSet) {
            if (part.getLender().getClientName().equals(i_newLender.getClientName())) {
                partToReturn = part;
                break;
            }
        }

        return partToReturn;
    }

    public void addPaymentToPaymentInfoListInRisk(int paymentTimeUnit, boolean wasItPaid, int howManyPaymentsToPay) {
        int wasChangedCounter = 0;

        if (howManyPaymentsToPay != 0) { // if the loaner pays less than one payment

            for (PaymentInfo singlePayment : m_PaymentInfoList) {
                if (!singlePayment.isWasItPaid()) {
                    singlePayment.setWasItPaid(true);
                    //m_PaymentInfoList.remove(singlePayment);
                    wasChangedCounter++;
                    if (wasChangedCounter == howManyPaymentsToPay) {
                        break;
                    }
                }
            }
           // m_PaymentInfoList.add(new PaymentInfo(paymentTimeUnit, m_Payment.getFundToPayEveryTimeUnit() * howManyPaymentsToPay, m_Payment.getInterestToPayEveryTimeUnit() * howManyPaymentsToPay, (int) m_Payment.getSumToPayEveryTimeUnit() * howManyPaymentsToPay, wasItPaid));
        }
    }

    public void addUnPaidPaymentToInfoListLoanInRisk(int paymentYaz) {
        m_PaymentInfoList.add(new PaymentInfo(paymentYaz, m_Payment.getFundToPayEveryTimeUnit(), m_Payment.getInterestToPayEveryTimeUnit(), (int) m_Payment.getSumToPayEveryTimeUnit(), false));

    }

    public void addPaymentToPaymentInfoListActive(int paymentTimeUnit, boolean wasItPaid) {
        m_PaymentInfoList.removeIf(singlePaymentInfo -> singlePaymentInfo.isWasItPaid() == false);
        m_PaymentInfoList.add(new PaymentInfo(paymentTimeUnit, m_Payment.getFundToPayEveryTimeUnit(), m_Payment.getInterestToPayEveryTimeUnit(), (int) m_Payment.getSumToPayEveryTimeUnit(), wasItPaid));
    }

    public void addPaymentToPaymentInfoListFromActiveRisk(int paymentTimeUnit, boolean wasItPaid) {
        m_PaymentInfoList.add(new PaymentInfo(paymentTimeUnit, m_Payment.getFundToPayEveryTimeUnit(), m_Payment.getInterestToPayEveryTimeUnit(), (int) m_Payment.getSumToPayEveryTimeUnit(), wasItPaid));
    }

    public double loanSumLeftToPay() {
        return m_Payment.getSumLeftToPay();
    }

    public double loanFundLeftToPay() {
        return m_Payment.getFundLeftToPay();
    }

    public double loanInterestLeftToPay() {
        return m_Payment.getInterestLeftToPay();
    }

    public double loanPaidFund() {
        return m_Payment.paidFundAmount();
    }

    public double loanPaidInterest() {
        return m_Payment.paidInterestAmount();
    }

    public double interestLoanToPayAmount() {
        return m_Payment.getInitialInterest();
    }

    public int getPendingMoney() {
        return m_PendingMoney;
    }

    public int getSumOfTimeUnit() {
        return f_SumOfTimeUnit;
    }

    public int getBeginningTimeUnit() {
        return m_BeginningTimeUnit;
    }

    public String getLoanNameID() {
        return f_LoanNameID;
    }

    public BankAccount getLoanOwner() {
        return f_LoanOwner;
    }

    public List<PartInLoan> getLendersSet() {
        return m_LendersAndAmountsSet;
    }

    public LoanStatus getLoanStatus() {
        return m_LoanStatus;
    }

    public String getLoanCategory() {
        return f_LoanCategory;
    }

    public int getLoanStartSum() {
        return f_LoanStartSum;
    }

    public double getInterest() {
        return f_Interest;
    }

    public double fundOfNextPayment() {
        return m_Payment.getFundToPayNextPayment();
    }

    public double interestOfNextPayment() {
        return m_Payment.getInterestToPayNextPayment();
    }

    public double amountOfNextPayment() {
        return m_Payment.getAmountToPayNextPayment();
    }

    public int getTimeUnitsBetweenPayment() {
        return f_TimeUnitsBetweenPayment;
    }

    public List<PaymentInfo> getPaymentInfoList() {
        return m_PaymentInfoList;
    }

    public void changeLoanStatus(LoanStatus i_LoanStatus) {
        m_LoanStatus = i_LoanStatus;
    }

    public void addToPendingMoney(int i_AmountToAdd, int i_BeginningTimeUnit) throws Exception {
        if (m_PendingMoney == 0 && m_LoanStatus == LoanStatus.NEW) {
            m_LoanStatus = LoanStatus.PENDING;
        }
        if (m_LoanStatus == LoanStatus.PENDING) {
            m_PendingMoney += i_AmountToAdd;
            updateLoanStatusToActive(i_BeginningTimeUnit);
        } else {
            throw new Exception("Loan status is not pending or new");
        }
    }

    private void updateLoanStatusToActive(int i_CurrentTimeUnit) throws Exception {
        if (m_PendingMoney == f_LoanStartSum) {
            m_LoanStatus = LoanStatus.ACTIVE;
            f_LoanOwner.addMoneyToAccount(m_PendingMoney, i_CurrentTimeUnit); //pay to borrower
            m_PendingMoney = 0;
            m_BeginningTimeUnit = i_CurrentTimeUnit;
            //TODO: update ending timeunit
            timeunitOfFirstUnPaidPayment = i_CurrentTimeUnit;
            checkIfPaymentNeededAndAddPaymentNotification(i_CurrentTimeUnit);
        }
    }

    public void updateLoanStatusToPending() {
        m_LoanStatus = LoanStatus.PENDING;
    }

    public boolean isItPaymentTime(int i_CurrentTimeunit) {
        return (((i_CurrentTimeunit - (m_BeginningTimeUnit - 1)) % f_TimeUnitsBetweenPayment) == 0 &&
                (m_LoanStatus == LoanStatus.ACTIVE || m_LoanStatus == LoanStatus.RISK));
    }

    public void checkIfPaymentNeededAndAddPaymentNotification(int i_CurrentTimeUnit) {
        if (isItPaymentTime(i_CurrentTimeUnit) && (m_LoanStatus == LoanStatus.ACTIVE || m_LoanStatus == LoanStatus.RISK)) {
            f_LoanOwner.addPaymentNotification(f_LoanNameID, i_CurrentTimeUnit, (int) m_Payment.getSumToPayEveryTimeUnit());
        }
    }

    public void unpaidPayment(int currentTimeunit) {
        m_LoanStatus = LoanStatus.RISK;
        addPaymentToPaymentInfoListFromActiveRisk(currentTimeunit, false);
        m_Payment.updateNextPaymentRisk();
        updateNextPaymentsOfLendersRisk();
    }

    private void checkIfLoanIsFinished(int i_CurrentTimeUnit) {
        if (m_Payment.getSumLeftToPay() < 1) {
            m_LoanStatus = LoanStatus.FINISHED;
            updateEndingTimeunit(i_CurrentTimeUnit);
        }
    }

    private void updateNextPaymentsOfLendersRisk() {
        for (PartInLoan lenderPart : m_LendersAndAmountsSet) {
            lenderPart.updateAmountToReceiveNextPaymentRisk();
        }
    }

    private void updateNextPaymentsOfLendersActiveAgain() {
        for (PartInLoan lenderPart : m_LendersAndAmountsSet) {
            lenderPart.updateAmountToReceiveNextPaymentActiveAgain();
        }
    }

    private void updateNextPaymentsOfLendersNotAllDebtPaid(int numberOfPaidPayments) {
        for (PartInLoan lenderPart : m_LendersAndAmountsSet) {
            lenderPart.updateAmountToReceiveNextPaymentNotAllAmountWasPaid(numberOfPaidPayments);
        }
    }

    private void updatePaymentsNotificationsAndDebtAmount(int amountToPay) {
        int numberOfCompletePaidPayments = amountToPay / (int) sumAmountToPayEveryTimeUnit();
        int amountOfPartlyPaidPayment = (int) (amountToPay % sumAmountToPayEveryTimeUnit());
        int i;

        for (i = 0; i < numberOfCompletePaidPayments; i++) {
            f_LoanOwner.getPaymentsNotificationList().remove(i);
        }
        if (amountOfPartlyPaidPayment > 0) {
            m_Payment.takeDownFromDebt(amountOfPartlyPaidPayment);
            int paymentNotificationListSize = f_LoanOwner.getPaymentsNotificationList().size();
            int notificationAmount = f_LoanOwner.getPaymentsNotificationList().get(paymentNotificationListSize-1).getSum();
            f_LoanOwner.getPaymentsNotificationList().get(paymentNotificationListSize-1).setSum(notificationAmount - amountOfPartlyPaidPayment);
        }
    }

    private void takePaymentsFromBorrowerToLenders(int i_currentTimeUnit, int amountToPay) throws Exception {
        for (PartInLoan lenderPart : m_LendersAndAmountsSet) {
            lenderPart.getLender().addMoneyToAccount(lenderPart.getAmountPercentageOfLoan() * amountToPay, i_currentTimeUnit);
            f_LoanOwner.withdrawMoneyFromAccount(lenderPart.getAmountPercentageOfLoan() * amountToPay, i_currentTimeUnit);
        }

        //what if he does not have enough money - we come here after we chose to pay this loan with knowing he has the money
    }

    private void takePaymentsFromBorrowerToLendersCloseLoan(int i_currentTimeUnit, int amountToPay) throws Exception {
        for (PartInLoan lenderPart : m_LendersAndAmountsSet) {
            lenderPart.getLender().addMoneyToAccount(lenderPart.getAmountPercentageOfLoan() * amountToPay, i_currentTimeUnit);
            f_LoanOwner.withdrawMoneyFromAccount(lenderPart.getAmountPercentageOfLoan() * amountToPay, i_currentTimeUnit);
        }
    }

    private void updatePaymentsNotificationCloseLoan() {
        f_LoanOwner.getPaymentsNotificationList().clear();
    }

    public double sumAmountToPayEveryTimeUnit() {
        return m_Payment.getSumToPayEveryTimeUnit();
    }

    public int howManyUnpaidPayments() {
        int counter = 0;

        for (PaymentInfo singlePayment : m_PaymentInfoList) {
            if (!singlePayment.isWasItPaid()) {
                counter++;
            }
        }

        return counter;
    }

    public void addPaymentToActiveLoan(int currentTimeUnit) throws Exception {
        int indexOfFirstNewNotification = f_LoanOwner.getIndexOfFirstNewNotification(timeunitOfFirstUnPaidPayment);
        List<PaymentNotification> loanOwnerNotificationList = f_LoanOwner.getPaymentsNotificationList();
        loanOwnerNotificationList.get(indexOfFirstNewNotification).setNewNotification(false);
        m_Payment.addPayment(1);
        m_PaymentInfoList.add(new PaymentInfo(currentTimeUnit, m_Payment.getFundToPayEveryTimeUnit(), m_Payment.getInterestToPayEveryTimeUnit(), (int) m_Payment.getSumToPayEveryTimeUnit(), true));
        m_LastPaidTimeUnit = currentTimeUnit;
        takePaymentsFromBorrowerToLenders(currentTimeUnit, (int) sumAmountToPayEveryTimeUnit());

        timeunitOfFirstUnPaidPayment += f_TimeUnitsBetweenPayment;
    }

    public void checkIfPaymentNeededAndPay(int i_CurrentTimeUnit) throws Exception {
        if (isItPaymentTime(i_CurrentTimeUnit)) {
            if (f_LoanOwner.getAccountBalance() >= m_Payment.getSumToPayEveryTimeUnit()) {
                if (m_LoanStatus == LoanStatus.RISK) { // loan is in RISK mode
                    int howManyPaymentsCanTheBorrowerPayInRisk = (int) (f_LoanOwner.getAccountBalance() / m_Payment.getSumToPayEveryTimeUnit());
                    // al least == 1
                    if (howManyPaymentsCanTheBorrowerPayInRisk > howManyUnpaidPayments() + 1) { // +1 for this timeunit payment
                        howManyPaymentsCanTheBorrowerPayInRisk = howManyUnpaidPayments() + 1;
                    }
                    if (howManyPaymentsCanTheBorrowerPayInRisk == howManyUnpaidPayments() + 1) {
                        m_LoanStatus = LoanStatus.ACTIVE;
                        m_Payment.updateNextPaymentActiveAgain();
                        updateNextPaymentsOfLendersActiveAgain();
                    }
                    m_Payment.addPayment(howManyPaymentsCanTheBorrowerPayInRisk);
                    addPaymentToPaymentInfoListInRisk(i_CurrentTimeUnit, true, howManyPaymentsCanTheBorrowerPayInRisk);
                    m_LastPaidTimeUnit = i_CurrentTimeUnit;
                    takePaymentsFromBorrowerToLenders(i_CurrentTimeUnit, howManyPaymentsCanTheBorrowerPayInRisk);
                } else { // active stays in active
                    m_Payment.addPayment(1);
                    addPaymentToPaymentInfoListActive(i_CurrentTimeUnit, true);
                    m_LastPaidTimeUnit = i_CurrentTimeUnit;
                    takePaymentsFromBorrowerToLenders(i_CurrentTimeUnit, 1);
                }
                checkIfLoanIsFinished(i_CurrentTimeUnit); // can be finished even in RISK
            } else { //from active to risk
                m_LoanStatus = LoanStatus.RISK;
                addPaymentToPaymentInfoListFromActiveRisk(i_CurrentTimeUnit, false);
                m_Payment.updateNextPaymentRisk();
                updateNextPaymentsOfLendersRisk();
            }
        }
    }

    public void addPaymentToRiskLoan(int currentTimeUnit, int amountToPayOnLoan) throws Exception {
        int currentAmountToPay = amountToPayOnLoan;
        List<PaymentNotification> loanOwnerNotificationList = f_LoanOwner.getPaymentsNotificationList();

        while (currentAmountToPay > 0) {
            int indexOfFirstNewNotification = f_LoanOwner.getIndexOfFirstNewNotification(timeunitOfFirstUnPaidPayment);
            PaymentNotification paymentNotification  = loanOwnerNotificationList.get(indexOfFirstNewNotification);

            if (paymentNotification.getSum() <= currentAmountToPay) {
                paymentNotification.setNewNotification(false);
                m_Payment.updateNextLeftFundAndInterestNotAllDebtPaid(f_Interest, paymentNotification.getSum());
                m_Payment.updateNextPaymentNotAllDebtPaid(1);
                updateNextPaymentsOfLendersNotAllDebtPaid(1);
                addPaymentToPaymentInfoListInRisk(currentTimeUnit, true, 1);
                m_Payment.takeDownFromDebt(paymentNotification.getSum());
                timeunitOfFirstUnPaidPayment += f_TimeUnitsBetweenPayment;
            }
            else {
                addNewPaymentNotificationSmallerAmount(paymentNotification.getSum() - currentAmountToPay, indexOfFirstNewNotification);
                m_Payment.takeDownFromDebt((int) (currentAmountToPay));
                m_Payment.updateNextLeftFundAndInterestNotAllDebtPaid(f_Interest, currentAmountToPay);
                break;
            }

            currentAmountToPay -= paymentNotification.getSum();
        }

        m_LastPaidTimeUnit = currentTimeUnit;
        takePaymentsFromBorrowerToLenders(currentTimeUnit, amountToPayOnLoan);

        if (isItPaymentTime(currentTimeUnit) && f_LoanOwner.getAmountOfNewNotifications() == 1) {
            loanIsActiveAgain();
        }
        else if (f_LoanOwner.getAmountOfNewNotifications() == 0) {
            loanIsActiveAgain();
        }
    }

    private void loanIsActiveAgain() {
        m_LoanStatus = LoanStatus.ACTIVE;
        m_Payment.updateNextPaymentActiveAgain();
        updateNextPaymentsOfLendersActiveAgain();
    }

    private void updatePaymentsNotificationsAmount(int newAmount, int index) {
        f_LoanOwner.getPaymentsNotificationList().get(index).setSum(newAmount);
    }

    private void addNewPaymentNotificationSmallerAmount(int newAmount, int index) {
        int loanPaymentYaz = f_LoanOwner.getPaymentsNotificationList().get(index).getPaymentYaz();
        f_LoanOwner.getPaymentsNotificationList().get(index).setNewNotification(false);
        f_LoanOwner.getPaymentsNotificationList().add(new PaymentNotification(f_LoanNameID, loanPaymentYaz, newAmount));
    }

    public void closeLoan(Loan selectedLoan, int currentTimeUnit) throws Exception {
        if (f_LoanOwner.getAccountBalance() < m_Payment.getSumLeftToPay()) {
            throw new Exception("Sorry, there is not enough money in the account.");
        }
        int unPaidPayments = howManyUnpaidPayments();

        if (selectedLoan.m_LoanStatus.toString().equals("RISK")) {
            addPaymentToRiskLoan(currentTimeUnit, getDebt());
            //addPaymentToPaymentInfoListInRisk(currentTimeUnit, true, unPaidPayments);
            //m_Payment.addPayment(unPaidPayments);
        }

        m_PaymentInfoList.add(new PaymentInfo(currentTimeUnit, m_Payment.getFundLeftToPay(), m_Payment.getInterestLeftToPay(), (int) m_Payment.getSumLeftToPay(), true));
        takePaymentsFromBorrowerToLendersCloseLoan(currentTimeUnit, (int) m_Payment.getSumLeftToPay());
        //updatePaymentsNotificationCloseLoan();
        m_Payment.addPaymentToCloseLoan();
        m_Payment.updateNextPaymentCloseLoan();
        //m_Payment.takeDownFromDebt((int) m_Payment.getSumLeftToPay());
        m_LastPaidTimeUnit = currentTimeUnit;
        m_LoanStatus = LoanStatus.FINISHED;
        m_EndingTimeunit = currentTimeUnit;
    }

    public void changeLoanToRisk(int currentTimeUnit) {
        m_LoanStatus = LoanStatus.RISK;
        addUnPaidPaymentToInfoListLoanInRisk(currentTimeUnit);
        m_Payment.updateNextPaymentRisk();
        m_Payment.addToDebt((int) m_Payment.getSumToPayEveryTimeUnit());
        updateNextPaymentsOfLendersRisk();
    }

    public void loanInRisk(int currentTimeUnit) {
        addUnPaidPaymentToInfoListLoanInRisk(currentTimeUnit);
        m_Payment.updateNextPaymentRisk();
        m_Payment.addToDebt((int) m_Payment.getSumToPayEveryTimeUnit());
        updateNextPaymentsOfLendersRisk();
    }

    public int getDebt() {
        return m_Payment.getDebt();
    }
}