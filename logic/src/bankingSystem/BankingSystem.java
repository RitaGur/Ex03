package bankingSystem;

import DTO.client.ClientInformationDTO;
import DTO.client.PaymentsNotificationsDTO;
import DTO.client.RecentTransactionDTO;
import DTO.lists.LoanForSaleListDTO;
import DTO.loan.LoanForSaleDTO;
import DTO.loan.LoanInformationDTO;
import DTO.loan.PartInLoanDTO;
import DTO.loan.PaymentsDTO;
import DTO.refresher.ForAdminRefresherDTO;
import DTO.refresher.ForCustomerRefresherDTO;
import bankingSystem.generated.AbsCustomer;
import bankingSystem.generated.AbsDescriptor;
import bankingSystem.generated.AbsLoan;
import bankingSystem.timeline.TimeUnit;
import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.bankAccount.RecentTransaction;
import bankingSystem.timeline.bankClient.BankClient;
import bankingSystem.timeline.bankClient.PaymentNotification;
import bankingSystem.timeline.loan.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class BankingSystem implements LogicInterface {
    private List<BankClient> m_BankAccountList;
    private List<Loan> m_LoanList;
    private TimeUnit m_CurrentTimeUnit;
    private List<String> m_LoanCategoryList;
    private List<LoanForSale> loansForSaleList;
    private List<ForAdminRefresherDTO> adminRefresherListByYaz;
    private List<ForCustomerRefresherDTO> customerRefresherListByYaz;
    private Boolean isRewind;
    private int yazOfRefresher;

    public BankingSystem() {
        m_BankAccountList = new ArrayList<>();
        m_LoanList = new ArrayList<>();
        m_CurrentTimeUnit = new TimeUnit();
        m_LoanCategoryList = new ArrayList<>();
        loansForSaleList = new ArrayList<>();
        adminRefresherListByYaz = new ArrayList<>();
        customerRefresherListByYaz = new ArrayList<>();
        isRewind = false;
        yazOfRefresher = 1;
    }

    @Override
    public void addBankClient(int i_AccountBalance, String i_ClientName) throws Exception {
        BankAccount account = null;

        for (BankClient i_BankClient : m_BankAccountList) {
            if (i_BankClient.getClientName() == i_ClientName) {
                account = i_BankClient;
            }
        }

        if (account != null) {
            throw new Exception("This bank client already exists.");
        }

        m_BankAccountList.add(new BankClient(i_AccountBalance, i_ClientName));
    }

    public void addBankClientByClient(BankClient newBankClient) {
        m_BankAccountList.add(newBankClient);
    }

    public void addLoan(String i_LoanID, String i_BorrowerName, int i_LoanStartSum, int i_SumOfTimeUnit, int i_HowOftenToPay, double i_Interest, String i_LoanCategory) throws Exception {
        checkParamsRange(i_LoanStartSum, i_SumOfTimeUnit, i_HowOftenToPay, i_Interest);
        checkLoanParams(i_SumOfTimeUnit, i_HowOftenToPay, i_LoanID);

        BankAccount borrowerAccount = findBankAccountByName(i_BorrowerName);

        Loan loanToAdd = new Loan(i_LoanID, borrowerAccount, i_LoanStartSum, i_SumOfTimeUnit, i_HowOftenToPay, i_Interest, i_LoanCategory);
        m_LoanList.add(loanToAdd);
        borrowerAccount.addAsLoanOwner(loanToAdd);

        if (!checkIfCategoryExists(i_LoanCategory)) {
            m_LoanCategoryList.add(i_LoanCategory);
        }
    }

    private void checkParamsRange(int loanStartSum, int sumOfTimeUnit, int howOftenToPay, double interest) throws Exception {
        if (loanStartSum <= 0  ) {
            throw new Exception("Loan amount needs to be a number bigger than 0.");
        }
        if (sumOfTimeUnit <= 0) {
            throw new Exception("Total yaz needs to be a number bigger than 0.");
        }
        if (howOftenToPay <= 0) {
            throw new Exception("Yaz between payments needs to be a number bigger than 0.");
        }
        if (interest <= 0 || interest > 100) {
            throw new Exception("loan interest needs to be numbers only");
        }
    }

    @Override
    public void promoteTimeline() throws Exception {
        m_CurrentTimeUnit.addOneToTimeUnit();
        if (!isRewind) {
            yazOfRefresher++;
        }

        // loans in risk
        for (Loan loan : m_LoanList) {
            if (loan.getLoanStatus().toString().equals("RISK") && loan.isItPaymentTime(m_CurrentTimeUnit.getCurrentTimeUnit())) {
                if (!(loan.getDebt() == (int) loan.loanSumLeftToPay())) {
                    loan.loanInRisk(m_CurrentTimeUnit.getCurrentTimeUnit());
                }
            }
        }

        for (BankClient client : m_BankAccountList) {
            ArrayList<Loan> loansNeedToBePaidThisTimeunitList = whichLoansNeedToPayListByBorrower(client);

            for (Loan loan : loansNeedToBePaidThisTimeunitList) {
                loan.checkIfPaymentNeededAndAddPaymentNotification(m_CurrentTimeUnit.getCurrentTimeUnit());
            }

            for (PaymentNotification paymentNotification : client.getPaymentsNotificationList()) {
                Loan loan = findLoanById(paymentNotification.getLoanID());
                if (paymentNotification.getNewNotification() && paymentNotification.getPaymentYaz() < m_CurrentTimeUnit.getCurrentTimeUnit()
                && loan.getLoanStatus().toString().equals("ACTIVE")) {
                    loan.changeLoanToRisk(m_CurrentTimeUnit.getCurrentTimeUnit());
                }
            }
        }
    }

    private ArrayList<Loan> whichLoansNeedToPayListByBorrower(BankClient client) {
        ArrayList<Loan> listToReturn = new ArrayList<>();

        for (Loan loan : client.getClientAsBorrowerSet()) {
            if (loan.isItPaymentTime(m_CurrentTimeUnit.getCurrentTimeUnit())) {
                listToReturn.add(loan);
            }
        }

        return listToReturn;
    }

    private ArrayList<Loan> whichLoansNeedToPayList() {
        ArrayList<Loan> listToReturn = new ArrayList<>();

        for (Loan loan : m_LoanList) {
            if (loan.isItPaymentTime(m_CurrentTimeUnit.getCurrentTimeUnit())) {
                listToReturn.add(loan);
            }
        }

        return listToReturn;
    }

    @Override
    public void readFromFile(String contentType, InputStream inputStream, String customerName) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AbsDescriptor descriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(inputStream);

        // Categories:
        List<String> categoriesList = descriptor.getAbsCategories().getAbsCategory();
        fromListToSetCategories(categoriesList); //filling m_LoanCategoryList

        // Loans:
        fillLoanList(descriptor.getAbsLoans().getAbsLoan(), customerName);
    }

    private void fillLoanList(List<AbsLoan> absLoan, String customerName) throws Exception {
        for (AbsLoan i_Loan : absLoan) {
            //checkLoanParams(i_Loan.getAbsTotalYazTime(), i_Loan.getAbsPaysEveryYaz(), i_Loan.getId());
            addLoan(i_Loan.getId(), customerName, i_Loan.getAbsCapital(), i_Loan.getAbsTotalYazTime(), i_Loan.getAbsPaysEveryYaz(),
                    i_Loan.getAbsIntristPerPayment(), i_Loan.getAbsCategory());
        }
    }

    private void checkLoanParams(int loanTotalYazTime, int loanPaysEveryYaz, String loanID) throws Exception {
        if (loanTotalYazTime % loanPaysEveryYaz != 0) {
            throw new Exception("The division between totalYazNumber to paysEveryYaz is not an integer.");
        }
        if (loanTotalYazTime < loanPaysEveryYaz) {
            throw new Exception("paysEveryYaz is bigger than totalYazTime.");
        }
        if(checkIfLoanExist(m_LoanList, loanID)) {
            throw new Exception("The loan: " + loanID + " already exist.");
        }
    }

    private void fillBankClientsList(List<AbsCustomer> absCustomerList) throws Exception {
        m_BankAccountList = new ArrayList<>();

        for (AbsCustomer i_Customer : absCustomerList) {
            if (checkIfClientExist(m_BankAccountList, i_Customer.getName())) {
                throw new Exception("This client " + i_Customer.getName() + " already exist.");
            }
            m_BankAccountList.add(new BankClient(i_Customer.getAbsBalance(), i_Customer.getName()));
        }
    }

    private boolean checkIfLoanExist(List<Loan> loanList, String loanID) {
        for (Loan loan : loanList) {
            if(loan.getLoanNameID().equals(loanID)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfClientExist(List<BankClient> BankAccountList, String name) {
        for (BankAccount account : BankAccountList) {
            if(account.getClientName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void fromListToSetCategories(List<String> i_ListToConvert) throws Exception {
        for (String i_CategoryInList : i_ListToConvert) {
            m_LoanCategoryList.add(i_CategoryInList);
        }
    }

    @Override
    public List<LoanInformationDTO> showLoansInformation() {
        List<LoanInformationDTO> loanListToReturn = new ArrayList<>();

        int counter = 1;

        for (Loan i_Loan : m_LoanList) {
            loanListToReturn.add(buildLoanDTO(i_Loan, counter++));
        }

        return loanListToReturn;
    }

    private LoanInformationDTO buildLoanDTO(Loan loan, int loanNumber) {
        LoanInformationDTO loanDTO = new LoanInformationDTO();

        loanDTO.setLoanNumber(loanNumber);
        loanDTO.setLoanNameID(loan.getLoanNameID());
        loanDTO.setBorrowerName(loan.getLoanOwner().getClientName());
        loanDTO.setLoanCategory(loan.getLoanCategory());
        loanDTO.setLoanStartSum(loan.getLoanStartSum());
        loanDTO.setLoanSumOfTimeUnit(loan.getSumOfTimeUnit());
        loanDTO.setLoanInterest((int)(loan.getInterest() * 100));
        loanDTO.setTimeUnitsBetweenPayments(loan.getTimeUnitsBetweenPayment());
        loanDTO.setStatus(loan.getLoanStatus().toString());
        loanDTO.setLenderSetAndAmounts(lenderSetAndAmountInDTO(loan.getLendersSet()));
        loanDTO.setBeginningTimeUnit(loan.getBeginningTimeUnit());
        loanDTO.setEndingTimeUnit(loan.getEndingTimeunit());
        loanDTO.setFundAmount(loan.getLoanStartSum());
        loanDTO.setInterestAmount((int) Math.round(loan.interestLoanToPayAmount()));
        loanDTO.setSumAmount(loanDTO.getFundAmount() + loanDTO.getInterestAmount());
        loanDTO.setPaidFund((int)Math.round(loan.loanPaidFund()));
        loanDTO.setPaidInterest((int)Math.round(loan.loanPaidInterest()));
        loanDTO.setFundLeftToPay((int)Math.round(loan.loanFundLeftToPay()));
        loanDTO.setInterestLeftToPay((int)Math.round(loan.loanInterestLeftToPay()));
        loanDTO.setPendingMoney(loan.getPendingMoney());
        loanDTO.setMissingMoneyToActive((loanDTO.getFundAmount() - loanDTO.getPendingMoney()));
        loanDTO.setNextPaymentTimeUnit(calculateNextPaymentOfLoan(loan));
        loanDTO.setSumAmountToPayEveryTimeUnit((int)Math.round(loan.sumAmountToPayEveryTimeUnit()));
        loanDTO.setPaymentsListInDTO(paymentsListInDTO(loan.getPaymentInfoList()));
        loanDTO.setLastPaymentTimeunit(loan.getLastPaidTimeUnit());
        loanDTO.setAmountToPayNextPayment((int)Math.round(loan.amountOfNextPayment()));
        loanDTO.setFundToPayNextPayment((int)Math.round(loan.fundOfNextPayment()));
        loanDTO.setInterestToPayNextPayment((int)Math.round(loan.interestOfNextPayment()));
        loanDTO.setNumberOfUnpaidPayments(loan.howManyUnpaidPayments());
        loanDTO.setDebt(loan.getDebt());

        return loanDTO;
    }

    private int calculateNextPaymentOfLoan(Loan loan) {
        int nextPaymentYaz = loan.getBeginningTimeUnit() + loan.getTimeUnitsBetweenPayment() - 1;
        int endYaz = loan.getBeginningTimeUnit() + loan.getSumOfTimeUnit() - 1;

        while (nextPaymentYaz < m_CurrentTimeUnit.getCurrentTimeUnit() && nextPaymentYaz < endYaz) {
            nextPaymentYaz += loan.getTimeUnitsBetweenPayment();

            if (nextPaymentYaz >= m_CurrentTimeUnit.getCurrentTimeUnit() || nextPaymentYaz == endYaz) {
                return nextPaymentYaz;
            }
        }

        return nextPaymentYaz;
    }

    private List<PartInLoanDTO> lenderSetAndAmountInDTO(List<PartInLoan> i_LenderSetAndAmount) {
        List<PartInLoanDTO> setToReturn = new ArrayList<>();
        int count = 1;

        for (PartInLoan i_PartInLoan : i_LenderSetAndAmount) {
            setToReturn.add(new PartInLoanDTO(i_PartInLoan.getLender().getClientName(), i_PartInLoan.getAmountOfLoan(), count++));
        }

        return setToReturn;
    }

    private List<PaymentsDTO> paymentsListInDTO(List<PaymentInfo> i_paymentsInfoSet) {
        List<PaymentsDTO> listToReturn = new ArrayList<>();
        int count = 1;

        for (PaymentInfo i_PaymentInfo : i_paymentsInfoSet) {
            listToReturn.add(new PaymentsDTO(i_PaymentInfo.getPaymentTimeUnit(), i_PaymentInfo.getFundPayment(),
                    i_PaymentInfo.getInterestPayment(), i_PaymentInfo.getPaymentSum(), i_PaymentInfo.isWasItPaid(), count++));
        }

        return listToReturn;
    }

    @Override
    public List<ClientInformationDTO> showClientsInformation() {
        List<ClientInformationDTO> clientsListToReturn = new ArrayList<>();
        int counter = 1;

        for (BankClient bankClient : m_BankAccountList) {
            clientsListToReturn.add(buildClientDTO(bankClient, counter++));
        }

        return clientsListToReturn;
    }

    private ClientInformationDTO buildClientDTO(BankClient bankClient, int clientNumber) {
        ClientInformationDTO clientDTO = new ClientInformationDTO();

        clientDTO.setClientNumber(clientNumber);
        clientDTO.setClientName(bankClient.getClientName());
        clientDTO.setRecentTransactionList(recentTransactionListDTO(bankClient.getLastTransactions()));
        clientDTO.setClientAsBorrowerLoanList(clientLoanListDTO(bankClient.getClientAsBorrowerSet()));
        clientDTO.setClientAsLenderLoanList(clientLoanListDTO(bankClient.getClientAsLenderSet()));
        clientDTO.setPaymentsNotificationsList(paymentsNotificationListDTO(bankClient.getPaymentsNotificationList()));
        clientDTO.setClientBalance((int)Math.round(bankClient.getAccountBalance()));
        clientDTO.setNewBorrower(bankClient.howManyInBorrower("NEW"));
        clientDTO.setPendingBorrower(bankClient.howManyInBorrower("PENDING"));
        clientDTO.setActiveBorrower(bankClient.howManyInBorrower("ACTIVE"));
        clientDTO.setRiskBorrower(bankClient.howManyInBorrower("RISK"));
        clientDTO.setFinishedBorrower(bankClient.howManyInBorrower("FINISHED"));
        clientDTO.setNewLender(bankClient.howManyInLender("NEW"));
        clientDTO.setPendingLender(bankClient.howManyInLender("PENDING"));
        clientDTO.setActiveLender(bankClient.howManyInLender("ACTIVE"));
        clientDTO.setRiskLender(bankClient.howManyInLender("RISK"));
        clientDTO.setFinishedLender(bankClient.howManyInLender("FINISHED"));

        return clientDTO;
    }

    private List<PaymentsNotificationsDTO> paymentsNotificationListDTO(List<PaymentNotification> paymentsNotificationList) {
        List<PaymentsNotificationsDTO> paymentsNotificationListInDTO = new ArrayList<>();
        int counter = 1;

        for (PaymentNotification paymentNotification : paymentsNotificationList) {
            paymentsNotificationListInDTO.add(buildPayment(paymentNotification, counter++));
        }

        return paymentsNotificationListInDTO;
    }

    private PaymentsNotificationsDTO buildPayment(PaymentNotification paymentNotification, int paymentNotificationNumber) {
        PaymentsNotificationsDTO paymentsNotificationsDTO = new PaymentsNotificationsDTO();

        paymentsNotificationsDTO.setPaymentNotificationNumber(paymentNotificationNumber);
        paymentsNotificationsDTO.setLoanID(paymentNotification.getLoanID());
        paymentsNotificationsDTO.setPaymentYaz(paymentNotification.getPaymentYaz());
        paymentsNotificationsDTO.setSum(paymentNotification.getSum());
        paymentsNotificationsDTO.setNewNotification(paymentNotification.getNewNotification());

        return paymentsNotificationsDTO;
    }

    private List<LoanInformationDTO> clientLoanListDTO(List<Loan> clientSet) {
        List<LoanInformationDTO> setToReturn = new ArrayList<>();
        int counter = 1;

        for (Loan loan : clientSet) {
            setToReturn.add(buildLoanDTO(loan, counter++));
        }

        return setToReturn;
    }

    private List<RecentTransactionDTO> recentTransactionListDTO(List<RecentTransaction> lastTransactions) {
        List<RecentTransactionDTO> setToReturn = new ArrayList<>();
        int count = 1;

        for (RecentTransaction recentTransaction : lastTransactions) {
            setToReturn.add(new RecentTransactionDTO(recentTransaction.getAmountOfTransaction(),recentTransaction.getBalanceBeforeTransaction(),
                    recentTransaction.getBalanceAfterTransaction(), recentTransaction.getTransactionTimeUnit(), recentTransaction.getKindOfTransaction(), count++));
        }

        return setToReturn;
    }

    public List<PaymentsNotificationsDTO> getPaymentsNotificationInDTO(String customerName) throws Exception {
        BankAccount customer = findBankAccountByName(customerName);
        List<PaymentsNotificationsDTO> paymentsNotificationsDTOList = new ArrayList<>();
        int counter = 1;
        List<PaymentNotification> paymentsNotificationList = customer.getPaymentsNotificationList();

        for (PaymentNotification paymentNotification : paymentsNotificationList) {
            paymentsNotificationsDTOList.add(buildPayment(paymentNotification, counter++));
        }

        return paymentsNotificationsDTOList;
    }

    @Override
    public void addMoneyToAccount(String i_ClientAccount, int i_AmountToAdd) throws Exception {
        BankAccount accountToAddMoney = findBankAccountByName(i_ClientAccount);
        accountToAddMoney.addMoneyToAccount(i_AmountToAdd, m_CurrentTimeUnit.getCurrentTimeUnit());
    }

    @Override
    public void withdrawMoneyFromAccount(String i_ClientAccount, int i_AmountToReduce) throws Exception {
        BankAccount accountToReduceMoney = findBankAccountByName(i_ClientAccount);
        accountToReduceMoney.withdrawMoneyFromAccount(i_AmountToReduce, m_CurrentTimeUnit.getCurrentTimeUnit());
    }

    @Override
    public void loansDistribution(List<LoanInformationDTO> chosenLoans, int amountOfMoneyToInvest, String lenderName, int maxOwnershipPercentage) throws Exception {
        int smallerPartAmount, biggerPartAmount, bigParts, arrIndex = 0, chosenLoanSize = chosenLoans.size(), partInPercentage, amountToInvestInLoan;
        BankClient lender = findBankAccountByName(lenderName);

        while (amountOfMoneyToInvest > 0 && chosenLoanSize > 0) {
            smallerPartAmount = (amountOfMoneyToInvest / chosenLoanSize);
            biggerPartAmount = smallerPartAmount + 1;
            bigParts = amountOfMoneyToInvest % (chosenLoanSize--);
            Object[] chosenLoansArr = chosenLoans.toArray();

            if (maxOwnershipPercentage != 0) {
                partInPercentage = calculateAmountByPercentage((LoanInformationDTO) chosenLoansArr[arrIndex], maxOwnershipPercentage);
                amountToInvestInLoan = bigParts > 0 ? Math.min(biggerPartAmount, partInPercentage) : Math.min(smallerPartAmount, partInPercentage);
                checkLenderPartForLoan((LoanInformationDTO) chosenLoansArr[arrIndex++], amountToInvestInLoan, lender);
            }
            else {
                amountToInvestInLoan = bigParts > 0 ? biggerPartAmount : smallerPartAmount;
                checkLenderPartForLoan((LoanInformationDTO) chosenLoansArr[arrIndex++], amountToInvestInLoan, lender);
            }

            amountOfMoneyToInvest -= amountToInvestInLoan;
        }
    }

    private int calculateAmountByPercentage(LoanInformationDTO loanInformationDTO, int maxOwnershipPercentage) {
        int loanStartSum = loanInformationDTO.getLoanStartSum();
        return (loanStartSum * maxOwnershipPercentage) / 100;
    }

    private void checkLenderPartForLoan(LoanInformationDTO chosenLoan, int lenderAmount, BankClient lender) throws Exception {
        Loan loan = findLoanById(chosenLoan.getLoanNameID());
        int finalAmountForLoan = 0;
        if (loan == null) {
            throw new Exception("There wasn't any loan found by this ID, check chosen loan array.");
        }
        if (loan.getLoanStatus() == LoanStatus.NEW) {
            finalAmountForLoan = (loan.getLoanStartSum() <= lenderAmount) ? loan.getLoanStartSum() : lenderAmount;
        }
        else if (loan.getLoanStatus() == LoanStatus.PENDING) {
            int moneyLeftToRaise = loan.getLoanStartSum() - loan.getPendingMoney();
            finalAmountForLoan = moneyLeftToRaise >= lenderAmount ? lenderAmount : moneyLeftToRaise;
        }

        loan.addLender(lender, finalAmountForLoan);
        lender.withdrawMoneyFromAccount(finalAmountForLoan, m_CurrentTimeUnit.getCurrentTimeUnit()); // take the money from the lender
        loan.addToPendingMoney(finalAmountForLoan, m_CurrentTimeUnit.getCurrentTimeUnit());
    }

    private Loan findLoanById(String loanNameID) {
        Loan loanToReturn = null;
        for (Loan loan : m_LoanList) {
            if (loan.getLoanNameID().equals(loanNameID)) {
                loanToReturn = loan;
                break;
            }
        }

        return loanToReturn;
    }

    @Override
    public void updateLoansCategories(List<String> i_LoansCategories) {
        for (String i_Category : i_LoansCategories) {
            m_LoanCategoryList.add(i_Category);
        }
    }

    private BankClient findBankAccountByName(String i_ClientName) throws Exception {
        BankClient account = null;

        for (BankClient i_BankClient : m_BankAccountList) {
            if (i_BankClient.getClientName().equals(i_ClientName)) {
                account = i_BankClient;
            }
        }

        if (account == null) {
            throw new Exception("This bank client does not exist, please add the bank client first.");
        }

        return account;
    }

    public LoanInformationDTO getLoanDTOByLoanID(String loanID, int numberOfLoanDTO) {
        LoanInformationDTO loanInformationDTO = null;

        for (Loan loan : m_LoanList) {
            if (loan.getLoanNameID().equals(loanID)) {
                loanInformationDTO = buildLoanDTO(loan, numberOfLoanDTO);
            }
        }

        return loanInformationDTO;
    }

    private boolean checkIfCategoryExists(String i_CategoryToCheck) {
        boolean categoryExist = false;

        for (String i_Category : m_LoanCategoryList) {
            if (i_Category.equals(i_CategoryToCheck)) {
                categoryExist = true;
            }
        }

        return categoryExist;
    }

    public List<String> getLoanCategoryList() {
        return m_LoanCategoryList;
    }

    public List<LoanInformationDTO> optionsForLoans(String clientName, List<String> chosenCategories, int amountOfMoneyToInvest,
                                                   int interest, int minimumTotalTimeunits, int maxOpenLoans) throws Exception {
        List<LoanInformationDTO> optionLoansSet = new ArrayList<>();
        BankAccount investClient = findBankAccountByName(clientName);
        int openLoansOfCustomer, counter = 1;

        try {
            if (amountOfMoneyToInvest > investClient.getAccountBalance()) {
                throw new Exception("Sorry, you do not have enough money in your account to invest this amount of money.");
            }
            for (Loan loanOfBankingSystem : m_LoanList) {
                openLoansOfCustomer = howManyOpenLoans(loanOfBankingSystem.getLoanOwner());
                if (loanOfBankingSystem.getLoanOwner().getClientName().equals(clientName)) {
                    throw new Exception("You can't invest in your own loans.");
                }
                //check the client is not the loan owner
                if (!loanOfBankingSystem.getLoanOwner().getClientName().equals(clientName) &&
                        (loanOfBankingSystem.getLoanStatus() == LoanStatus.NEW ||
                                loanOfBankingSystem.getLoanStatus()== LoanStatus.PENDING)) {
                    if (chosenCategories.contains(loanOfBankingSystem.getLoanCategory()) || chosenCategories.size() == 0) { //what if null?
                        if (interest <= (loanOfBankingSystem.getInterest() * 100) || interest == 0) {
                            if (maxOpenLoans >= openLoansOfCustomer || maxOpenLoans == 0) {
                                if (minimumTotalTimeunits <= loanOfBankingSystem.getSumOfTimeUnit()) {
                                    optionLoansSet.add(buildLoanDTO(loanOfBankingSystem, counter++));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return optionLoansSet;
    }

    private int howManyOpenLoans(BankAccount loanOwner) {
        int openLoansNumber = 0;
        List<Loan> customerLoans = loanOwner.getClientAsBorrowerSet();

        for (Loan customerLoan : customerLoans) {
            if (!customerLoan.getLoanStatus().toString().equals("FINISHED")) {
                openLoansNumber++;
            }
        }

        return openLoansNumber;
    }

    public void fillChosenCategoriesList(List<String> chosenCategories, String categories) throws Exception {
        if (!categories.equals("0")) {
            StringTokenizer numbers = new StringTokenizer(categories);
            while (numbers.hasMoreTokens()) {
                chosenCategories.add(findCategoryByNumberString(numbers.nextToken()));
            }
        }
    }

    private String findCategoryByNumberString(String numberAsString) throws Exception {
        int numberInInt = Integer.parseInt(numberAsString);
        if (numberInInt > m_LoanCategoryList.size() || numberInInt < 0) {
            throw new Exception("There are no categories for the number: "+ numberInInt);
        }
        Iterator<String> categoryIterator = m_LoanCategoryList.iterator();
        for (int i = 0; i < numberInInt - 1; i++) {
            categoryIterator.next();
        }
        return categoryIterator.next();
    }


    public TimeUnit getCurrentTimeUnit() {
        return m_CurrentTimeUnit;
    }

    public ClientInformationDTO getClientInformationByName(String customerName) {
        for (BankClient bankClient : m_BankAccountList) {
            if (bankClient.getClientName().equals(customerName)) {
                return buildClientDTO(bankClient, -1);
            }
        }

        return null;
    }

    public List<LoanInformationDTO> getCustomerOpenLoansToPay(String customerName) throws Exception {
        List<LoanInformationDTO> customerOpenLoansToPay = new ArrayList<>();
        BankAccount customer = findBankAccountByName(customerName);

        for (Loan loan : customer.getClientAsBorrowerSet()) {
            if (loan.getLoanStatus().toString().equals("ACTIVE") ||
                    loan.getLoanStatus().toString().equals("RISK")) {
                customerOpenLoansToPay.add(buildLoanDTO(loan, -1));
            }
        }

        return customerOpenLoansToPay;
    }

    public void addPayment(LoanInformationDTO selectedLoan, int amountToPay) throws Exception {
        Loan loan = findLoanById(selectedLoan.getLoanNameID());

        if (loan.getLoanStatus().toString().equals("RISK")) {
            addPaymentToRiskLoan(loan, amountToPay);
        }
        else {
            addPaymentToActiveLoan(loan);
        }
    }

    public void addPaymentToActiveLoan(Loan loan) throws Exception {
        loan.addPaymentToActiveLoan(m_CurrentTimeUnit.getCurrentTimeUnit());
    }

    public void addPaymentToRiskLoan(Loan loan, int amountToPay) throws Exception {
        loan.addPaymentToRiskLoan(m_CurrentTimeUnit.getCurrentTimeUnit(), amountToPay);
    }

    public void closeLoan(LoanInformationDTO selectedLoan) throws Exception {
        Loan loan = findLoanById(selectedLoan.getLoanNameID());

        loan.closeLoan(loan, m_CurrentTimeUnit.getCurrentTimeUnit());
    }

    public boolean isNewPaymentNotificationExist(String customerName, String selectedLoanID) throws Exception {
        BankAccount customer = findBankAccountByName(customerName);

        return customer.isNewPaymentNotificationExist(selectedLoanID);
    }

    public int getCustomerBalanceByName(String customerName) throws Exception {
        BankAccount customer = findBankAccountByName(customerName);

        return (int)customer.getAccountBalance();
    }

    public List<RecentTransactionDTO> getCustomerRecentTransactionByName(String customerFromSession) throws Exception {
        BankAccount customer = findBankAccountByName(customerFromSession);

        return recentTransactionListDTO(customer.getLastTransactions());
    }

    public List<LoanInformationDTO> getCustomerLenderLoans(String customerFromSession) throws Exception {
        BankAccount customer = findBankAccountByName(customerFromSession);

        return clientLoanListDTO(customer.getClientAsLenderSet());
    }

    public List<LoanInformationDTO> getCustomerLoanerLoans(String customerFromSession) throws Exception {
        BankAccount customer = findBankAccountByName(customerFromSession);

        return clientLoanListDTO(customer.getClientAsBorrowerSet());
    }

    public void addToSellLoanList(String loanID, String lenderName) throws Exception {
        Loan newLoanForSale = findLoanById(loanID);

        if (!newLoanForSale.getLoanStatus().toString().equals("ACTIVE")) {
            throw new Exception("The loan is not in ACTIVE status.");
        }

        BankClient loanSeller = findBankAccountByName(lenderName);
        int loanPrice = newLoanForSale.amountOfPartInLoanOfLender(loanSeller);

        LoanForSale newLoanForSaleToAddToList = new LoanForSale(newLoanForSale, loanSeller, loanPrice);
        for (LoanForSale loan : loansForSaleList) {
            if (loan.getLoanSeller().getClientName().equals(lenderName)) {
                throw new Exception("The client has already put this loan to sale.");
            }
        }
        loansForSaleList.add(newLoanForSaleToAddToList);
    }

    public List<LoanForSaleDTO> getLoansToSellInDTO() {
        List<LoanForSaleDTO> loanListToReturn = new ArrayList<>();

        for (LoanForSale loanForSale : loansForSaleList) {
            LoanForSaleDTO loanToAddDTO = new LoanForSaleDTO();
            loanToAddDTO.setLoanForSaleID(loanForSale.getLoanForSale().getLoanNameID());
            loanToAddDTO.setLoanSellerName(loanForSale.getLoanSeller().getClientName());
            loanToAddDTO.setLoanPrice(loanForSale.getLoanPrice());
            loanListToReturn.add(loanToAddDTO);
        }

        return loanListToReturn;
    }

    public List<LoanForSaleDTO> getLoansToSellInDTOByCustomer(String customer) {
        List<LoanForSaleDTO> loanListToReturn = new ArrayList<>();
        int counter = 1;

        for (LoanForSale loanForSale : loansForSaleList) {
            if (loanForSale.getLoanForSale().getLoanStatus().toString().equals("ACTIVE")) {
                if (!(loanForSale.getLoanForSale().getLoanOwner().getClientName().equals(customer))) {
                    LoanForSaleDTO loanToAddDTO = new LoanForSaleDTO();
                    loanToAddDTO.setLoanForSaleID(loanForSale.getLoanForSale().getLoanNameID());
                    loanToAddDTO.setLoanSellerName(loanForSale.getLoanSeller().getClientName());
                    loanToAddDTO.setLoanPrice(loanForSale.getLoanPrice());
                    loanToAddDTO.setLoanNumber(counter++);

                    loanListToReturn.add(loanToAddDTO);
                }
            }
        }

        return loanListToReturn;
    }

    public LoanForSaleListDTO getLoanForSaleForRefresher(String customer) {
        LoanForSaleListDTO listDTOToReturn = new LoanForSaleListDTO();

        List<LoanForSaleDTO> listLoanForSale = getLoansToSellInDTOByCustomer(customer);
        listDTOToReturn.setLoanForSaleListInformationDTO(loanInformationForSale(listLoanForSale));
        listDTOToReturn.setLoanForSaleDTOList(listLoanForSale);

        return listDTOToReturn;
    }

    public List<LoanInformationDTO> loanInformationForSale(List<LoanForSaleDTO> loanForSaleDTOList) {
        List<LoanInformationDTO> listToReturn = new ArrayList<>();

        for (LoanForSaleDTO loan : loanForSaleDTOList) {
            Loan loanToAdd = findLoanById(loan.getLoanForSaleID());
            if (loanToAdd.getLoanStatus().toString().equals("ACTIVE")) {
                LoanInformationDTO loanToAddDTO = buildLoanDTO(loanToAdd, loan.getLoanNumber());
                listToReturn.add(loanToAddDTO);
            }
        }

        return listToReturn;
    }

    public List<LoanInformationDTO> allLoansForSaleByCustomer(String customerName) {
        List<LoanInformationDTO> listToReturn = new ArrayList<>();

        for (LoanForSale loanForSale : loansForSaleList) {
            if (!(loanForSale.getLoanForSale().getLoanOwner().getClientName().equals(customerName))) {
                LoanInformationDTO loanToAdd = new LoanInformationDTO();
                loanToAdd.setLoanNameID(loanForSale.getLoanForSale().getLoanNameID());

                listToReturn.add(loanToAdd);
            }
        }
        return listToReturn;
    }


    public void buyLoan(String loanForSaleID, String sellerName, String newLenderName) throws Exception {
        if (newLenderName.equals(sellerName)) {
            throw new Exception("You can't buy a loan you put on sale");
        }

        Loan loanToSwitchLenders = findLoanById(loanForSaleID);
        BankClient seller = findBankAccountByName(sellerName);
        BankClient newLender = findBankAccountByName(newLenderName);
        LoanForSale loanInSale = findLoanForSaleByID(loanForSaleID);

        if (loanInSale == null) {
            throw new Exception("Could not find loan");
        }
        else if (newLender.getAccountBalance() < loanInSale.getLoanPrice()) {
            throw new Exception("There is not enough money in the account");
        }

        loanToSwitchLenders.switchLenders(seller, newLender);
        transferMoneyOfLenders(seller, newLender, loanForSaleID);
        loansForSaleList.removeIf(Loan ->(Loan.getLoanForSale().getLoanNameID().equals(loanForSaleID)));
    }

    private LoanForSale findLoanForSaleByID(String loanForSaleID) {
        for (LoanForSale loanforSale : loansForSaleList) {
            if (loanforSale.getLoanForSale().getLoanNameID().equals(loanForSaleID)) {
                return loanforSale;
            }
        }

        return null;
    }

    private void transferMoneyOfLenders(BankClient seller, BankClient newLender, String loanForSaleID) throws Exception {
        int amountToTransfer = 0;

        for (LoanForSale loanforSale : loansForSaleList) {
            if (loanforSale.getLoanForSale().getLoanNameID().equals(loanForSaleID)) {
                amountToTransfer = loanforSale.getLoanPrice();
            }
        }

        seller.addMoneyToAccount(amountToTransfer, m_CurrentTimeUnit.getCurrentTimeUnit());
        newLender.withdrawMoneyFromAccount(amountToTransfer, m_CurrentTimeUnit.getCurrentTimeUnit());
    }

    public void addNewAdminRefresher() {
        ForAdminRefresherDTO newAdminRefresher = new ForAdminRefresherDTO();
        setAdminRefresherParams(newAdminRefresher);

        adminRefresherListByYaz.add(newAdminRefresher);
    }

    public void addNewCustomerRefresher(String customerFromSession) throws Exception {
        ForCustomerRefresherDTO newCustomerRefresher = new ForCustomerRefresherDTO();
        setCustomerRefresherParams(newCustomerRefresher, customerFromSession);

        customerRefresherListByYaz.add(newCustomerRefresher);
    }

    private void setAdminRefresherParams(ForAdminRefresherDTO adminRefresherDTO) {
        adminRefresherDTO.setAdminLoanList(showLoansInformation());
        adminRefresherDTO.setAdminCustomerList(showClientsInformation());
        adminRefresherDTO.setCurrentYaz(getCurrentTimeUnit().getCurrentTimeUnit());
    }

    private void setCustomerRefresherParams(ForCustomerRefresherDTO customerRefresherDTO, String customerFromSession) throws Exception {
        customerRefresherDTO.setCustomerName(customerFromSession);
        customerRefresherDTO.setCustomerBalance(getCustomerBalanceByName(customerFromSession));
        customerRefresherDTO.setCurrentYaz(getCurrentYaz());
        customerRefresherDTO.setCustomerPaymentNotificationsList(getPaymentsNotificationInDTO(customerFromSession));
        customerRefresherDTO.setCustomerRecentTransactionsList(getCustomerRecentTransactionByName(customerFromSession));
        customerRefresherDTO.setCustomerLenderLoansList(getCustomerLenderLoans(customerFromSession));
        customerRefresherDTO.setCustomerLonerLoansList(getCustomerLoanerLoans(customerFromSession));
        customerRefresherDTO.setCustomerPaymentLoanerLoansList(getCustomerOpenLoansToPay(customerFromSession));
        customerRefresherDTO.setLoanCategoryList(getLoanCategoryList());
        customerRefresherDTO.setLoansForSaleList(getLoanForSaleForRefresher(customerFromSession));
    }

    public ForCustomerRefresherDTO getCustomerRefresherFromList(int yaz, String customerName) throws Exception {
        if (yaz == m_CurrentTimeUnit.getCurrentTimeUnit()) {
            ForCustomerRefresherDTO customerRefresherDTO = new ForCustomerRefresherDTO();
            setCustomerRefresherParams(customerRefresherDTO, customerName);

            return customerRefresherDTO;
        }
        else {
            return findCustomerRefresherFromListByNameAndYaz(yaz, customerName);
        }
    }

    private ForCustomerRefresherDTO findCustomerRefresherFromListByNameAndYaz(int yaz, String customerName) {
        for (ForCustomerRefresherDTO customerRefresher : customerRefresherListByYaz) {
            if (customerRefresher.getCustomerName().equals(customerName)) {
                if (customerRefresher.getCurrentYaz() == yaz) {
                    return customerRefresher;
                }
            }
        }

        return null;
    }

    public ForAdminRefresherDTO getAdminRefresherFromList(int yaz) {
        if (yaz == m_CurrentTimeUnit.getCurrentTimeUnit()) {
            ForAdminRefresherDTO adminRefresherDTO = new ForAdminRefresherDTO();
            setAdminRefresherParams(adminRefresherDTO);

            return adminRefresherDTO;
        }
        else {
            return adminRefresherListByYaz.get(yaz - 1);
        }
    }

    public void updateBankWeInRewind(int yazOfRefresher) {
        isRewind = true;
        this.yazOfRefresher = yazOfRefresher;
    }

    public int getCurrentYaz() {
        if (isRewind) {
            return yazOfRefresher;
        }
        return m_CurrentTimeUnit.getCurrentTimeUnit();
    }

    public void updateBackToRealTime() {
        isRewind = false;
        yazOfRefresher = m_CurrentTimeUnit.getCurrentTimeUnit();
    }

    public int getYazOfRefresher() {
        return yazOfRefresher;
    }

    public Boolean getRewind() {
        return isRewind;
    }

    public ForCustomerRefresherDTO getEmptyRefresher() {
        ForCustomerRefresherDTO customerRefresherDTO = new ForCustomerRefresherDTO();

        customerRefresherDTO.setCustomerBalance(0);
        customerRefresherDTO.setCurrentYaz(getCurrentYaz());
        customerRefresherDTO.setCustomerPaymentNotificationsList(new ArrayList<>());
        customerRefresherDTO.setCustomerRecentTransactionsList(new ArrayList<>());
        customerRefresherDTO.setCustomerLenderLoansList(new ArrayList<>());
        customerRefresherDTO.setCustomerLonerLoansList(new ArrayList<>());
        customerRefresherDTO.setCustomerPaymentLoanerLoansList(new ArrayList<>());
        customerRefresherDTO.setLoanCategoryList(new ArrayList<>());
        LoanForSaleListDTO loanForSaleListDTO = new LoanForSaleListDTO();
        loanForSaleListDTO.setLoanForSaleDTOList(new ArrayList<>());
        loanForSaleListDTO.setLoanForSaleListInformationDTO(new ArrayList<>());
        customerRefresherDTO.setLoansForSaleList(loanForSaleListDTO);

        return customerRefresherDTO;
    }

    public boolean isLoanIDExists(String loanNameID) {
        for (Loan loan : m_LoanList) {
            if (loan.getLoanNameID().equals(loanNameID)) {
                return true;
            }
        }

        return false;
    }

    public boolean isCategoryExist(String categoryToCheck) {
        for (String category : m_LoanCategoryList) {
            if (category.equals(categoryToCheck)) {
                return true;
            }
        }

        return false;
    }
}
