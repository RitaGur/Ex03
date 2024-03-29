package mainApp;

import DTO.client.PaymentsNotificationsDTO;
import DTO.loan.LoanInformationDTO;
import client.util.api.HttpStatusUpdate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import mainApp.admin.adminController.AdminController;
import mainApp.admin.LoanInfoController;
import mainApp.admin.loan.active.ActiveInfoController;
import mainApp.admin.loan.finished.FinishedInfoController;
import mainApp.admin.loan.pending.PendingInfoController;
import mainApp.admin.loan.risk.RiskInfoController;
import mainApp.header.HeaderController;
import mainApp.login.LoginController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminAppController implements Initializable, HttpStatusUpdate {
    @FXML private GridPane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private GridPane adminComponent;
    @FXML private AdminController adminComponentController;
    @FXML private GridPane pendingInfoComponent;
    @FXML private PendingInfoController pendingInfoComponentController;
    @FXML private GridPane activeInfoComponent;
    @FXML private ActiveInfoController activeInfoComponentController;
    @FXML private GridPane riskInfoComponent;
    @FXML private RiskInfoController riskInfoComponentController;
    @FXML private GridPane finishedInfoComponent;
    @FXML private FinishedInfoController finishedInfoComponentController;
    @FXML private TableView<LoanInformationDTO> loanInfoComponent;
    @FXML private LoanInfoController loanInfoComponentController;

    @FXML
    private BorderPane mainBorderpane;
    @FXML private GridPane loginComponent;
    @FXML private LoginController loginComponentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (headerComponentController != null) {
            headerComponentController.setMainController(this);
        }
        if (adminComponentController != null) {
            adminComponentController.setMainController(this);
        }
        if (pendingInfoComponentController != null) {
            pendingInfoComponentController.setMainController(this);
        }
        if (activeInfoComponentController != null) {
            activeInfoComponentController.setMainController(this);
        }
        if (riskInfoComponentController != null) {
            riskInfoComponentController.setMainController(this);
        }
        if (finishedInfoComponentController != null) {
            finishedInfoComponentController.setMainController(this);
        }
       /* if (customerComponentController != null) {
           // customerComponentController.setMainController(this);
        }*/
        if (loanInfoComponentController != null) {
            loanInfoComponentController.setMainController(this);
        }
    }

    public void setAdminController(AdminController adminController) {
        this.adminComponentController = adminController;
        adminController.setMainController(this);
    }

    public void setLoginController(LoginController loginController) {
        this.loginComponentController = loginController;
        loginController.setMainController(this);
    }

    public void setHeaderController(HeaderController headerController) {
        this.headerComponentController = headerController;
        headerController.setMainController(this);
    }

    public void setPendingInfoController(PendingInfoController pendingInfoController) {
        this.pendingInfoComponentController = pendingInfoController;
        pendingInfoController.setMainController(this);
    }

    public void setActiveInfoController(ActiveInfoController activeInfoController) {
        this.activeInfoComponentController = activeInfoController;
        activeInfoController.setMainController(this);
    }

    public void setRiskInfoController(RiskInfoController riskInfoController) {
        this.riskInfoComponentController = riskInfoController;
        riskInfoController.setMainController(this);
    }

    public void setFinishedInfoController(FinishedInfoController finishedInfoController) {
        this.finishedInfoComponentController = finishedInfoController;
        finishedInfoController.setMainController(this);
    }

  /*  public void setCustomerController(CustomerController customerController) {
        this.customerComponentController = customerController;
        customerController.setMainController(this);
    }*/

    public void insertAdminView(Parent adminView) {
        mainBorderpane.setCenter(adminComponentController.getAdminView());
    }

    public void putCustomerView(String customer, ScrollPane customerScrollPane) throws Exception {
        adminComponentController.makeIncreaseYazAble();
        mainBorderpane.setCenter(customerScrollPane);
        //loadCustomerInformation(customer, adminComponentController.getCategoriesList(), adminComponentController.getPaymentNotificationList(customer));
    }

    public void loadCustomerInformation(String customer, List<String> categoriesList, List<PaymentsNotificationsDTO> paymentsNotificationsDTOList) throws Exception {
        //customerComponentController.loadCustomerInformation(customer, categoriesList, paymentsNotificationsDTOList);
    }

    public void setAdminComponent(ScrollPane adminComponent) {
         adminComponentController.setAdminView(adminComponent);
    }

    public void showPendingInfo(LoanInformationDTO currentLoan) {
        pendingInfoComponentController.showPendingInfo(currentLoan);
    }

   /* public void addCustomersToComboBox(List<ClientInformationDTO> clientsInformationList) {
        headerComponentController.addCustomersToComboBox(clientsInformationList);
    }*/

    public void showLendersTable(LoanInformationDTO currentLoan) throws IOException {
        adminComponentController.showLendersTable(currentLoan);
    }

    public void setLendersTable(TableView lenders, String loanStatus) {
        if (loanStatus.equals("PENDING")) {
            pendingInfoComponentController.setLendersTable(lenders);
        }
        else if (loanStatus.equals("ACTIVE")) {
            activeInfoComponentController.setLendersTable(lenders);
        }
        else if (loanStatus.equals("RISK")) {
            riskInfoComponentController.setLendersTable(lenders);
        }
        else if (loanStatus.equals("FINISHED")) {
            finishedInfoComponentController.setLendersTable(lenders);
        }
    }

    public void showActiveInfo(LoanInformationDTO currentLoan) {
        activeInfoComponentController.showActiveInfo(currentLoan);
    }

    public void showPaymentsTable(LoanInformationDTO currentLoan) throws IOException {
        adminComponentController.showPaymentsTable(currentLoan);
    }

    public void setPaymentsTable(TableView payments, String loanStatus) {
        if (loanStatus.equals("ACTIVE")) {
            activeInfoComponentController.setPaymentsTable(payments);
        }
        else if (loanStatus.equals("RISK")) {
            riskInfoComponentController.setPaymentsTable(payments);
        }
        else if (loanStatus.equals("FINISHED")) {
            finishedInfoComponentController.setPaymentsTable(payments);
        }
    }

    public void showRiskInfo(LoanInformationDTO currentLoan) {
        riskInfoComponentController.showRiskInfo(currentLoan);
    }

    public void showFinishedInfo(LoanInformationDTO currentLoan) {
        finishedInfoComponentController.showFinishedInfo(currentLoan);
    }

    /*public ClientInformationDTO getCustomerByName(String customerName) {
        return adminComponentController.getCustomerByName(customerName);
    }*/

    public void setLoanInfoController(LoanInfoController loanInfoController) {
        this.loanInfoComponentController = loanInfoController;
        loanInfoController.setMainController(this);
    }

    public void showLoanInfo(List<LoanInformationDTO> loanOptions, Boolean isOwnerVisible, Boolean isNumberLoanVisible, ScrollPane infoScrollPane, String kindOfTableView) {
        loanInfoComponentController.setTableColumns(loanOptions, isOwnerVisible, isNumberLoanVisible, infoScrollPane, kindOfTableView);
    }

  /*  public List<LoanInformationDTO> getLoansList() {
        return adminComponentController.getLoansList();
    }*/

    public void insertByStatus(LoanInformationDTO currentLoan, ScrollPane infoScrollPane) throws IOException {
        loanInfoComponentController.insertByStatus(currentLoan, infoScrollPane);
    }

    public void setCustomerViewParameter(ScrollPane customerView) {
        headerComponentController.setCustomerViewParameter(customerView);
    }

    /*public List<LoanInformationDTO> getLoanOptions(String customerName, int moneyAmount, List<String> selectedCategories, int minInterest, int minTotalYaz, int maxOpenLoans) throws Exception {
        return adminComponentController.getLoanOptions(customerName, moneyAmount, selectedCategories, minInterest, minTotalYaz, maxOpenLoans);
    }*/

    /*public void loansDistribution(List<LoanInformationDTO> chosenLoans, int maxOwnershipPercentage, int moneyToInvest, String customerName) throws Exception {
        adminComponentController.loansDistribution(chosenLoans, maxOwnershipPercentage, moneyToInvest, customerName);
    }*/

/*
    public void setAllTables(String customerName) throws Exception {

    }
    public void withdrawMoney(String customerName, int moneyToWithdraw) throws Exception {
        adminComponentController.withdrawMoney(customerName, moneyToWithdraw);
    }

    public void addMoney(String customerName, int moneyToWithdraw) throws Exception {
        adminComponentController.addMoney(customerName, moneyToWithdraw);
    }

    public List<LoanInformationDTO> getCustomerOpenLoansToPay(String customerName) throws Exception {
        return adminComponentController.getCustomerOpenLoansToPay(customerName);
    }

    public void addPaymentToActiveLoan(LoanInformationDTO selectedLoan) throws Exception {
        adminComponentController.addPaymentToActiveLoan(selectedLoan);
    }

    public void addPaymentToRiskLoan(LoanInformationDTO selectedLoan, int amountToPay) throws Exception {
        adminComponentController.addPaymentToRiskLoan(selectedLoan, amountToPay);
    }

    public void closeLoan(LoanInformationDTO selectedLoan) throws Exception {
        adminComponentController.closeLoan(selectedLoan);
    }

    public boolean isNewPaymentNotificationExist(String customerName, String loanNameID) throws Exception {
        return adminComponentController.isNewPaymentNotificationExist(customerName, loanNameID);
    }

    */

    public void setScrollPaneDisability(String loanStatus) {
        adminComponentController.setScrollPaneDisability(loanStatus);
    }

    public void setAllStylesheets(String value) {
        if (adminComponentController != null) {
           // adminComponentController.setAdminStyleSheet(value);
        }
        if (headerComponentController != null) {
            headerComponentController.setHeaderStyleSheet(value);
        }
        /*if (customerComponentController != null) {
           // customerComponentController.setCustomerStyleSheet(value);
        }*/
        if (pendingInfoComponentController != null) {
            pendingInfoComponentController.setPendingInfoStyleSheet(value);
        }
        if (activeInfoComponentController != null) {
            activeInfoComponentController.setActiveInfoStyleSheet(value);
        }
        if (riskInfoComponentController != null) {
            riskInfoComponentController.setRiskInfoStyleSheet(value);
        }
        if (finishedInfoComponentController != null) {
            finishedInfoComponentController.setFinishedInfoStyleSheet(value);
        }
    }

    public void setHeaderComponent(GridPane headerComponent) {
        headerComponentController.setHeaderComponent(headerComponent);
    }

    public void setCustomerComponent(ScrollPane customerView) {
        //customerComponentController.setCustomerComponent(customerView);
    }

    public void setPendingInfoComponent(GridPane pendingInfo) {
        pendingInfoComponentController.setPendingInfoComponent(pendingInfo);
    }

    public void setActiveInfoComponent(GridPane activeInfo) {
        activeInfoComponentController.setActiveInfoComponent(activeInfo);
    }

    public void setRiskInfoComponent(GridPane riskInfo) {
        riskInfoComponentController.setRiskInfoComponent(riskInfo);
    }

    public void setFinishedInfoComponent(GridPane finishedInfo) {
        finishedInfoComponentController.setFinishedInfoComponent(finishedInfo);
    }

    public void setCustomerStyleSheet() {
       // customerComponentController.setCustomerStyleSheet(headerComponentController.getValueOfSkinComboBox());
    }

 /*   public void setPendingInfoStyleSheet() {
        pendingInfoComponentController.setPendingInfoStyleSheet(headerComponentController.getValueOfSkinComboBox());
    }

    public void setActiveInfoStyleSheet() {
        activeInfoComponentController.setActiveInfoStyleSheet(headerComponentController.getValueOfSkinComboBox());
    }

    public void setRiskInfoStyleSheet() {
        riskInfoComponentController.setRiskInfoStyleSheet(headerComponentController.getValueOfSkinComboBox());
    }

    public void setFinishedInfoStyleSheet() {
        finishedInfoComponentController.setFinishedInfoStyleSheet(headerComponentController.getValueOfSkinComboBox());
    }

  */

    public void updateUserName(String userName) throws IOException {
        headerComponentController.updateUsernameLabel(userName);
        loadAdminViewAfterLoginSucceeded();
    }

    private void loadAdminViewAfterLoginSucceeded() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("admin/adminController/admin.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane adminView = fxmlLoader.load(url.openStream());
        AdminController adminController = fxmlLoader.getController();

        setAdminController(adminController);
        setAdminComponent(adminView);
        mainBorderpane.setCenter(adminView);
    }

    public void afterLogin(String userName) throws IOException {
        headerComponentController.updateUsernameLabel(userName); // update user label
        loadAdminViewAfterLoginSucceeded(); // admin fxml loading
        adminComponentController.loadLoanTableFromFXML(); //loans fxml loading
        adminComponentController.fillLoanTablesInformation();
        adminComponentController.fillCustomerTableInformation();
        adminComponentController.makeIncreaseYazButtonAble();
        updateCurrentYaz();
        setActive();
    }

    public void increaseYaz() throws Exception {
        adminComponentController.increaseYaz();
    }

    public void updateCurrentYaz() {
        headerComponentController.updateCurrentYaz();
    }

    public void updateCurrentYazByNumber(String currentYaz) {
        headerComponentController.updateCurrentYazByNumber(currentYaz);
    }

    public void setActive() {
        adminComponentController.startListRefresher();
    }

    @Override
    public void updateHttpLine(String line) {

    }

    public int getSavedCurrentYaz() {
        return headerComponentController.getSavedYaz();
    }

    public void setSavedCurrentYaz(int newCurrentYaz) {
        headerComponentController.setSavedYaz(newCurrentYaz);
    }

    public void addYazToComboBox() {
        headerComponentController.addYazToComboBox();
    }

    public void updateRefresherYaz(int yazChosen) {
        adminComponentController.updateRefresherYaz(yazChosen);
    }
}