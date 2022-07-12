package mainApp.admin;

import DTO.client.ClientInformationDTO;
import DTO.client.PaymentsNotificationsDTO;
import DTO.loan.LoanInformationDTO;
import DTO.loan.PaymentsDTO;
//import bankingSystem.BankingSystem;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import mainApp.AppController;
import mainApp.admin.loan.pending.PendingInfoController;
import mainApp.customer.CustomerController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML private TableView lendersComponent;
    @FXML private LendersController lendersComponentController;
    @FXML private TableView paymentsComponent;
    @FXML private PaymentsController paymentsComponentController;

    @FXML private PendingInfoController pendingController;

    private List<ClientInformationDTO> customersList;

    //private BankingSystem engine;
    private File chosenFile;
    private AppController mainController;
    private ScrollPane adminView;

    @FXML
    private ScrollPane adminScrollPane;

    @FXML
    private GridPane adminBottomGridPane;

    @FXML
    private TableView<ClientInformationDTO> customersInfoTableView;

    @FXML
    private TableColumn<ClientInformationDTO, Integer> CINumberCol;

    @FXML
    private TableColumn<ClientInformationDTO, String> CINameCol;

    @FXML
    private TableColumn<ClientInformationDTO, Integer> CIBalanceCol;

    @FXML
    private TableColumn<ClientInformationDTO, Integer> CINewBorrowerCol;

    @FXML
    private TableColumn<ClientInformationDTO, Integer> CIPendingActiveBorrowerCol;

    @FXML
    private TableColumn<ClientInformationDTO, Integer> CIActiveBorrowerCol;

    @FXML
    private TableColumn<ClientInformationDTO, Integer> CIFinishedBorrowerCol;

    @FXML
    private TableColumn<ClientInformationDTO, Integer> CINewLenderCol;

    @FXML
    private TableColumn<ClientInformationDTO, Integer> CIPendingLenderCol;

    @FXML
    private TableColumn<ClientInformationDTO, Integer> CIActiveLenderCol;

    @FXML
    private TableColumn<ClientInformationDTO, Integer> CIFinishedLenderCol;

    @FXML
    private TableColumn<ClientInformationDTO, Integer> CIRiskBorrower;

    @FXML
    private TableColumn<ClientInformationDTO, Integer> CIRiskLender;

    @FXML
    private TableView<LoanInformationDTO> loanTableView1;

    @FXML
    private TableView<?> loanTableView11;

    @FXML
    private Button increaseYazButton;

    @FXML
    private Button loadFileButton;

    @FXML
    private ScrollPane topInfoScrollPane;

    @FXML
    private ScrollPane bottomInfoScrollPane;

    @FXML
    private GridPane pendingInfoGridPane;

    @FXML
    private Label pendingLoanNumberLabel;

    @FXML
    private Label pendingRaisedAmountLabel;

    @FXML
    private Label pendingRemainingAmountLabel;

    @FXML
    private Button pendingLendersButton;

    @FXML
    private ScrollPane loansScrollPane;

    @FXML
    private ScrollPane customersInformationScrollPane;

    @FXML
    private Button clickMeButton;

    @FXML
    private AnchorPane animationAnchorPane;

    @FXML
    void clickMeOnActionListener(ActionEvent event) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Fill Transitions");
        stage.setResizable(false);

        Group root = new Group();
        Node square = new ImageView("/mainApp/admin/st,small,507x507-pad,600x600,f8f8f8.jpg");

        final Rotate rotationTransform = new Rotate(0, 50, 50);
        square.getTransforms().add(rotationTransform);

        // rotate a square using timeline attached to the rotation transform's angle property.
        final Timeline rotationAnimation = new Timeline();
        rotationAnimation.getKeyFrames()
                .add(
                        new KeyFrame(
                                Duration.seconds(5),
                                new KeyValue(
                                        rotationTransform.angleProperty(),
                                        360
                                )
                        )
                );
        rotationAnimation.setCycleCount(1);
        rotationAnimation.play();

        animationAnchorPane.getChildren().add(square);

        rotationAnimation.setOnFinished((event1) -> {
            FadeTransition fadeout = new FadeTransition(Duration.seconds(2), animationAnchorPane);
            fadeout.setFromValue(1.0);
            fadeout.setToValue(0.0);
            fadeout.play();
            clickMeButton.setVisible(false);
        });
    }

    @FXML
    void increaseYazButtonActionListener(ActionEvent event) throws Exception {
        /*mainController.increaseYaz();

        // load Notification Area again
        List<ClientInformationDTO> customersList = engine.showClientsInformation();
        for (ClientInformationDTO customer : customersList) {
            mainController.loadCustomerInformation(customer.getClientName(), engine.getLoanCategoryList(), engine.getPaymentsNotificationInDTO(customer.getClientName()));
        }*/
    }

    @FXML
    void loadFileButtonActionListener(ActionEvent event) throws Exception {
        Node node = (Node) event.getSource();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML","*.xml")
        );
        fileChooser.setTitle("Load File");
        chosenFile = fileChooser.showOpenDialog(node.getScene().getWindow());

        if (chosenFile != null) {
            loadFile(chosenFile);
        }

        setAdminScrollPanesVisibility(true);
    }

    private void setAdminScrollPanesVisibility(boolean visibility) {
        customersInformationScrollPane.setVisible(visibility);
        //bottomInfoScrollPane.setVisible(visibility);
        loansScrollPane.setVisible(visibility);
    }

    private void loadFile(File chosenFile) throws Exception {
       /* mainController.makeButtonAbleBack();
        engine = new BankingSystem();
        engine.readFromFile(chosenFile);

        mainController.updateHeaderLabels(engine.getCurrentTimeUnit().getCurrentTimeUnit(), chosenFile.getPath());
        insertAdminView();

        // customers Loading
        addCustomersToComboBox();
        loadCustomerView();*/
    }

    private void loadCustomerView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/mainApp/customer/customer.fxml");
        fxmlLoader.setLocation(url);
        assert url != null;
        ScrollPane customerView = fxmlLoader.load(url.openStream());
        CustomerController customerController = fxmlLoader.getController();
        mainController.setCustomerController(customerController);

        //mainController.setCustomerViewParameter(customerView);
        mainController.setCustomerComponent(customerView);
        mainController.setCustomerStyleSheet();

        //TODO: styleCSS
        //customerView.getStylesheets().add(getClass().getResource("customerCSS2.css").toExternalForm());
        //customerView.getStylesheets().add(getClass().getResource("customerCSS.css").toExternalForm());
    }

    private void addCustomersToComboBox() {
        //mainController.addCustomersToComboBox(engine.showClientsInformation());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (lendersComponentController != null) {
            lendersComponentController.setMainController(this);
        }
        if (paymentsComponentController != null) {
            paymentsComponentController.setMainController(this);
        }

        setAdminScrollPanesVisibility(false);
        topInfoScrollPane.setVisible(false);
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void makeIncreaseYazAble() {
        increaseYazButton.setDisable(false);
    }

    public void insertAdminView() throws IOException {
        // update loans table
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/mainApp/admin/loanInfo.fxml");
        fxmlLoader.setLocation(url);
        TableView loansTable = fxmlLoader.load(url.openStream());
        LoanInfoController loanInfoController = fxmlLoader.getController();
        mainController.setLoanInfoController(loanInfoController);

        loansScrollPane.setContent(loansTable);
        //mainController.showLoanInfo(engine.showLoansInformation(), true, true, topInfoScrollPane);

        customersInfoTableView.refresh();
        // customers information table:
        CINumberCol.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, Integer>("clientNumber"));
        CINameCol.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, String>("clientName"));
        CIBalanceCol.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, Integer>("clientBalance"));
        CINewBorrowerCol.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, Integer>("newBorrower"));
        CIPendingActiveBorrowerCol.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, Integer>("pendingBorrower"));
        CIActiveBorrowerCol.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, Integer>("activeBorrower"));
        CIFinishedBorrowerCol.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, Integer>("finishedBorrower"));
        CINewLenderCol.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, Integer>("newLender"));
        CIPendingLenderCol.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, Integer>("pendingLender"));
        CIActiveLenderCol.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, Integer>("activeLender"));
        CIFinishedLenderCol.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, Integer>("finishedLender"));
        CIRiskBorrower.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, Integer>("riskBorrower"));
        CIRiskLender.setCellValueFactory(new PropertyValueFactory<ClientInformationDTO, Integer>("riskLender"));

       // customersInfoTableView.setItems(FXCollections.observableArrayList(engine.showClientsInformation()));
    }

    public void setAdminView(ScrollPane adminComponent) {
        adminView = adminComponent;
    }

    public Parent getAdminView() {
        return adminView;
    }

    public void showLendersTable(LoanInformationDTO currentLoan) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("lendersTable.fxml");
        fxmlLoader.setLocation(url);
        TableView lenders = fxmlLoader.load(url.openStream());
        LendersController lendersController = fxmlLoader.getController();
        this.setLendersController(lendersController);

        mainController.setLendersTable(lenders, currentLoan.getLoanStatus());
        lendersComponentController.showLendersTable(currentLoan.getLenderSetAndAmounts());
    }

    public void setLendersController(LendersController lendersController) {
        this.lendersComponentController = lendersController;
        lendersController.setMainController(this);
    }

    public void showPaymentsTable(LoanInformationDTO currentLoan) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("paymentsTable.fxml");
        fxmlLoader.setLocation(url);
        TableView paymentsTable = fxmlLoader.load(url.openStream());
        PaymentsController paymentsController = fxmlLoader.getController();
        this.setPaymentsController(paymentsController);

        mainController.setPaymentsTable(paymentsTable, currentLoan.getLoanStatus());
        List<PaymentsDTO> listOfPayments = currentLoan.getPaymentsList();
        paymentsComponentController.showPaymentsTable(listOfPayments, currentLoan.getLoanStatus());
    }

    private void setPaymentsController(PaymentsController paymentsController) {
        this.paymentsComponentController = paymentsController;
        paymentsController.setMainController(this);
    }

   /* public ClientInformationDTO getCustomerByName(String customerName) {
        return engine.getClientInformationByName(customerName);
    }

    public List<String> getCategoriesList() {
        return engine.getLoanCategoryList();
    }

    public List<LoanInformationDTO> getLoansList() {
        return engine.showLoansInformation();
    }

    public List<LoanInformationDTO> getLoanOptions(String customerName, int moneyAmount, List<String> selectedCategories, int minInterest, int minTotalYaz, int maxOpenLoans) throws Exception {
        return engine.optionsForLoans(customerName, selectedCategories, moneyAmount, minInterest, minTotalYaz, maxOpenLoans);
    }

    public void loansDistribution(List<LoanInformationDTO> chosenLoans, int maxOwnershipPercentage, int moneyToInvest, String customerName) throws Exception {
        engine.loansDistribution(chosenLoans, moneyToInvest, customerName, maxOwnershipPercentage);
    }

    public void withdrawMoney(String customerName, int moneyToWithdraw) throws Exception {
        engine.withdrawMoneyFromAccount(customerName, moneyToWithdraw);
    }

    public void addMoney(String customerName, int moneyToAdd) throws Exception {
        engine.addMoneyToAccount(customerName, moneyToAdd);
    }

    public List<LoanInformationDTO> getCustomerOpenLoansToPay(String customerName) throws Exception {
        return engine.getCustomerOpenLoansToPay(customerName);
    }

    public List<PaymentsNotificationsDTO> getPaymentNotificationList(String customer) throws Exception {
        return engine.getPaymentsNotificationInDTO(customer);
    }

    public void addPaymentToActiveLoan(LoanInformationDTO selectedLoan) throws Exception {
        engine.addPaymentToActiveLoan(selectedLoan);
    }

    public void addPaymentToRiskLoan(LoanInformationDTO selectedLoan, int amountToPay) throws Exception {
        engine.addPaymentToRiskLoan(selectedLoan, amountToPay);
    }

    public void closeLoan(LoanInformationDTO selectedLoan) throws Exception {
        engine.closeLoan(selectedLoan);
    }

    public int getCurrentYaz() {
        return engine.getCurrentTimeUnit().getCurrentTimeUnit();
    }

    public void increaseYaz() throws Exception {
        engine.promoteTimeline();

        for (ClientInformationDTO clientDTO : engine.showClientsInformation()) {
            mainController.setAllTables(clientDTO.getClientName());
        }
    }

    public boolean isNewPaymentNotificationExist(String customerName, String selectedLoanID) throws Exception {
        return engine.isNewPaymentNotificationExist(customerName, selectedLoanID);
    }

    public void setScrollPaneDisability(String loanStatus) {
        if (!(loanStatus.equals("NEW"))) {
            topInfoScrollPane.setVisible(true);
        }
    }

    public void setAdminStyleSheet(String value) {
        switch (value) {
            case "Default":
                adminView.getStylesheets().clear();
                break;
            case "Skin1":
                adminView.getStylesheets().clear();
                adminView.getStylesheets().add(getClass().getResource("adminCSS.css").toExternalForm());
                break;
            case "Skin2":
                adminView.getStylesheets().clear();
                adminView.getStylesheets().add(getClass().getResource("adminCSS2.css").toExternalForm());
                break;
        }
    }

    public void setCustomerStyleSheet(String value) {

    }*/
}
