package bankingSystem;

import DTO.client.ClientInformationDTO;
import DTO.client.PaymentsNotificationsDTO;
import DTO.loan.LoanInformationDTO;
import bankingSystem.generated.AbsCustomer;
import bankingSystem.generated.AbsDescriptor;
import bankingSystem.generated.AbsLoan;
import bankingSystem.timeline.TimeUnit;
import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.bankClient.BankClient;
import bankingSystem.timeline.bankClient.PaymentNotification;
import bankingSystem.timeline.loan.Loan;
import bankingSystem.timeline.loan.LoanStatus;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class BankingSystem implements LogicInterface {
    private List<BankClient> m_BankAccountList;
    private List<Loan> m_LoanList;
    private TimeUnit m_CurrentTimeUnit;
    private List<String> m_LoanCategoryList;

    public BankingSystem() {
        m_BankAccountList = new ArrayList<>();
        m_LoanList = new ArrayList<>();
        m_CurrentTimeUnit = new TimeUnit();
        m_LoanCategoryList = new ArrayList<>();
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

    public void addLoan(String i_LoanID, String i_BorrowerName, int i_LoanStartSum, int i_SumOfTimeUnit, int i_HowOftenToPay, double i_Interest, String i_LoanCategory) throws Exception {
        BankAccount borrowerAccount = findBankAccountByName(i_BorrowerName);
        if (checkIfCategoryExists(i_LoanCategory)) {
            Loan loanToAdd = new Loan(i_LoanID, borrowerAccount, i_LoanStartSum, i_SumOfTimeUnit, i_HowOftenToPay, i_Interest, i_LoanCategory);
            m_LoanList.add(loanToAdd);
            borrowerAccount.addAsLoanOwner(loanToAdd);
        } else {
            throw new Exception("Loan Category does not exist");
        }
    }

    @Override
    public void promoteTimeline() throws Exception {
        m_CurrentTimeUnit.addOneToTimeUnit();

        // loans in risk
        for (Loan loan : m_LoanList) {
            if (loan.getLoanStatus().toString().equals("RISK") && loan.isItPaymentTime(m_CurrentTimeUnit.getCurrentTimeUnit())) {
                loan.loanInRisk(m_CurrentTimeUnit.getCurrentTimeUnit());
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
    public void readFromFile(File file) throws Exception {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AbsDescriptor descriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(file);

        // Categories:
        List<String> categoriesList = descriptor.getAbsCategories().getAbsCategory();
        fromListToSetCategories(categoriesList); //filling m_LoanCategoryList

        // BankClients:
        fillBankClientsList(descriptor.getAbsCustomers().getAbsCustomer());

        // Loans:
        fillLoanList(descriptor.getAbsLoans().getAbsLoan());

        // Timeunit:
        m_CurrentTimeUnit.setCurrentTimeUnit();
    }

    private void fillLoanList(List<AbsLoan> absLoan) throws Exception {
        m_LoanList = new ArrayList<>();
        for (AbsLoan i_Loan : absLoan) {
            if (i_Loan.getAbsTotalYazTime() % i_Loan.getAbsPaysEveryYaz() != 0) {
                throw new Exception("The division between totalYazNumber to paysEveryYaz is not an integer.");
            }
            if ( i_Loan.getAbsTotalYazTime() < i_Loan.getAbsPaysEveryYaz()) {
               throw new Exception("paysEveryYaz is bigger than totalYazTime.");
            }
            if(checkIfLoanExist(m_LoanList, i_Loan.getId())) {
                throw new Exception("The loan: " + i_Loan.getId() + " already exist.");
            }
            addLoan(i_Loan.getId(), i_Loan.getAbsOwner(), i_Loan.getAbsCapital(), i_Loan.getAbsTotalYazTime(), i_Loan.getAbsPaysEveryYaz(),
                    i_Loan.getAbsIntristPerPayment(), i_Loan.getAbsCategory());
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
    //private boolean checkIfLoanOwnerExist()

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
        m_LoanCategoryList = new ArrayList<>();

        for (String i_CategoryInList : i_ListToConvert) {
            if (!i_ListToConvert.contains(i_CategoryInList)) {
                throw new Exception("This category does not exist.");
            }
            m_LoanCategoryList.add(i_CategoryInList);
        }
    }

    @Override
    public List<LoanInformationDTO> showLoansInformation() {
        List<LoanInformationDTO> loanListToReturn = new ArrayList<>();
        int counter = 1;

        for (Loan i_Loan : m_LoanList) {
            loanListToReturn.add(new LoanInformationDTO(i_Loan, counter++));
        }

        return loanListToReturn;
    }

    @Override
    public List<ClientInformationDTO> showClientsInformation() {
        List<ClientInformationDTO> clientsListToReturn = new ArrayList<>();
        int counter = 1;

        for (BankClient bankClient : m_BankAccountList) {
            clientsListToReturn.add(new ClientInformationDTO(bankClient, counter++));
        }

        return clientsListToReturn;
    }

    public List<PaymentsNotificationsDTO> getPaymentsNotificationInDTO(String customerName) throws Exception {
        BankAccount customer = findBankAccountByName(customerName);
        List<PaymentsNotificationsDTO> paymentsNotificationsDTOList = new ArrayList<>();
        int counter = 1;
        List<PaymentNotification> paymentsNotificationList = customer.getPaymentsNotificationList();

        for (PaymentNotification paymentNotification : paymentsNotificationList) {
            paymentsNotificationsDTOList.add(new PaymentsNotificationsDTO(paymentNotification, counter++));
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
                //check the client is not the loan owner
                if (!loanOfBankingSystem.getLoanOwner().getClientName().equals(clientName) &&
                        (loanOfBankingSystem.getLoanStatus() == LoanStatus.NEW ||
                                loanOfBankingSystem.getLoanStatus()== LoanStatus.PENDING)) {
                    if (chosenCategories.contains(loanOfBankingSystem.getLoanCategory()) || chosenCategories.size() == 0) { //what if null?
                        if (interest <= (loanOfBankingSystem.getInterest() * 100) || interest == 0) {
                            if (maxOpenLoans >= openLoansOfCustomer || maxOpenLoans == 0) {
                                if (minimumTotalTimeunits <= loanOfBankingSystem.getSumOfTimeUnit()) {
                                    optionLoansSet.add(new LoanInformationDTO(loanOfBankingSystem, counter++));
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
                return new ClientInformationDTO(bankClient, -1);
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
                customerOpenLoansToPay.add(new LoanInformationDTO(loan, -1));
            }
        }

        return customerOpenLoansToPay;
    }

    public void addPaymentToActiveLoan(LoanInformationDTO selectedLoan) throws Exception {
        Loan loan = findLoanById(selectedLoan.getLoanNameID());

        loan.addPaymentToActiveLoan(m_CurrentTimeUnit.getCurrentTimeUnit());
    }

    public void addPaymentToRiskLoan(LoanInformationDTO selectedLoan, int amountToPay) throws Exception {
        Loan loan = findLoanById(selectedLoan.getLoanNameID());

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
}
