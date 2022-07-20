package mainApp.admin.adminController;

import DTO.client.ClientInformationDTO;
import DTO.lists.CustomersListDTO;
import DTO.lists.LoanListDTO;
import DTO.loan.LoanInformationDTO;
import DTO.loan.PaymentsDTO;
import DTO.refresher.ForAdminRefresherDTO;
import client.util.api.HttpStatusUpdate;
import client.util.http.HttpClientUtil;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import mainApp.AdminAppController;
import mainApp.admin.LendersController;
import mainApp.admin.LoanInfoController;
import mainApp.admin.PaymentsController;
import mainApp.admin.loan.pending.PendingInfoController;
import mainApp.customer.CustomerController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static client.util.Constants.*;
import static client.util.popup.AlertPopUp.alertPopUp;

public class AdminController implements Initializable {
    @FXML private TableView lendersComponent;
    @FXML private LendersController lendersComponentController;
    @FXML private TableView paymentsComponent;
    @FXML private PaymentsController paymentsComponentController;

    @FXML private PendingInfoController pendingController;

    private List<ClientInformationDTO> customersList;

    private AdminAppController mainController;
    private ScrollPane adminView;

    private Timer timer;
    private TimerTask listRefresher;
    private HttpStatusUpdate httpStatusUpdate;
    private TableView adminLoansTableView;

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


    public void setHttpStatusUpdate(HttpStatusUpdate httpStatusUpdate) {
        this.httpStatusUpdate = httpStatusUpdate;

    }

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
        mainController.increaseYaz();
    }

    private void setAdminScrollPanesVisibility(boolean visibility) {
        customersInformationScrollPane.setVisible(visibility);
        loansScrollPane.setVisible(visibility);
    }

    private void loadCustomerView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/mainApp/customer/customer.fxml");
        fxmlLoader.setLocation(url);
        assert url != null;
        ScrollPane customerView = fxmlLoader.load(url.openStream());
        CustomerController customerController = fxmlLoader.getController();
        mainController.setCustomerController(customerController);

        mainController.setCustomerComponent(customerView);
        mainController.setCustomerStyleSheet();
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

    public void setMainController(AdminAppController mainController) {
        this.mainController = mainController;
    }

    public void makeIncreaseYazAble() {
        increaseYazButton.setDisable(false);
    }

    public void loadLoanTableFromFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/mainApp/admin/loanInfo.fxml");
        fxmlLoader.setLocation(url);
        TableView loansTable = fxmlLoader.load(url.openStream());
        LoanInfoController loanInfoController = fxmlLoader.getController();
        mainController.setLoanInfoController(loanInfoController);
        loansScrollPane.setContent(loansTable);
        loansScrollPane.setVisible(true);
        adminLoansTableView = loansTable;
    }

    //todo: delete?
    /*public void insertAdminView() throws IOException {
        //todo func insertAdmin

        // update loans table
        loadLoanTableFromFXML();

        //TODO: get loans list
        //mainController.showLoanInfo(engine.showLoansInformation(), true, true, topInfoScrollPane);

       setCustomerTableColumns();

        //customersInfoTableView.setItems(FXCollections.observableArrayList(engine.showClientsInformation()));
    }*/

    private void setCustomerTableColumns() {
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
    }

    public void setAdminView(ScrollPane adminComponent) {
        adminView = adminComponent;
    }

    public Parent getAdminView() {
        return adminView;
    }

    public void showLendersTable(LoanInformationDTO currentLoan) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/mainApp/admin/lendersTable.fxml");
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
        URL url = getClass().getResource("/mainApp/admin/paymentsTable.fxml");
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

   /*

    public void closeLoan(LoanInformationDTO selectedLoan) throws Exception {
        engine.closeLoan(selectedLoan);
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

    public void setScrollPaneDisability(String loanStatus) {
        if (!(loanStatus.equals("NEW"))) {
            topInfoScrollPane.setVisible(true);
        }
    }

    private void checkForChangesInLoans(ObservableList<LoanInformationDTO> oldLoansList, List<LoanInformationDTO> newLoanList) {
        int i = 0;
        for (LoanInformationDTO oldLoanDTO : oldLoansList) {
            LoanInformationDTO currentNewLoan = newLoanList.get(i++);
            if (oldLoanDTO.getLoanNameID().equals(currentNewLoan.getLoanNameID())) {
                oldLoanDTO.setStatus(currentNewLoan.getLoanStatus());
                oldLoanDTO.setFundAmount(currentNewLoan.getFundAmount());
                oldLoanDTO.setLoanSumOfTimeUnit(currentNewLoan.getLoanSumOfTimeUnit());
                oldLoanDTO.setLoanInterest((int)currentNewLoan.getLoanInterest());
                oldLoanDTO.setSumAmount(currentNewLoan.getSumAmount());
                oldLoanDTO.setTimeUnitsBetweenPayments(currentNewLoan.getTimeUnitsBetweenPayments());

                oldLoanDTO.setLenderSetAndAmounts(currentNewLoan.getLenderSetAndAmounts());
                oldLoanDTO.setPaymentsListInDTO(currentNewLoan.getPaymentsList());

                oldLoanDTO.setPendingMoney(currentNewLoan.getPendingMoney());
                oldLoanDTO.setMissingMoneyToActive(currentNewLoan.getMissingMoneyToActive());
                oldLoanDTO.setBeginningTimeUnit(currentNewLoan.getBeginningTimeUnit());
                oldLoanDTO.setNextPaymentTimeUnit(currentNewLoan.getNextPaymentTimeUnit());
                oldLoanDTO.setPaidFund(currentNewLoan.getPaidFund());
                oldLoanDTO.setPaidInterest(currentNewLoan.getPaidInterest());
                oldLoanDTO.setFundLeftToPay(currentNewLoan.getFundLeftToPay());
                oldLoanDTO.setInterestLeftToPay(currentNewLoan.getInterestLeftToPay());
                oldLoanDTO.setNumberOfUnpaidPayments(currentNewLoan.getNumberOfUnpaidPayments());
                oldLoanDTO.setSumAmountToPayEveryTimeUnit(currentNewLoan.getSumAmountToPayEveryTimeUnit());
                oldLoanDTO.setEndingTimeUnit(currentNewLoan.getEndingTimeUnit());
            }
        }
        while (newLoanList.size() > i) {
            oldLoansList.add(newLoanList.get(i++));
        }
   }

    private void checkForChangesInCustomers(ObservableList<ClientInformationDTO> oldCustomersList, List<ClientInformationDTO> newCustomersList) {
        int i = 0;
        for (ClientInformationDTO oldClientDTO : oldCustomersList) {
            ClientInformationDTO currentNewCustomer = newCustomersList.get(i++);
            if (oldClientDTO.getClientName().equals(currentNewCustomer.getClientName())) {
                oldClientDTO.setRiskLender(currentNewCustomer.getRiskLender());
                oldClientDTO.setNewLender(currentNewCustomer.getNewLender());
                oldClientDTO.setFinishedLender(currentNewCustomer.getFinishedLender());
                oldClientDTO.setActiveLender(currentNewCustomer.getActiveLender());
                oldClientDTO.setPendingLender(currentNewCustomer.getPendingLender());
                oldClientDTO.setNewBorrower(currentNewCustomer.getNewBorrower());
                oldClientDTO.setFinishedBorrower(currentNewCustomer.getFinishedBorrower());
                oldClientDTO.setRiskBorrower(currentNewCustomer.getRiskBorrower());
                oldClientDTO.setPendingBorrower(currentNewCustomer.getPendingBorrower());
                oldClientDTO.setActiveBorrower(currentNewCustomer.getActiveBorrower());
                oldClientDTO.setClientBalance((int)currentNewCustomer.getClientBalance());
            }
        }
        while (newCustomersList.size() > i) {
            oldCustomersList.add(newCustomersList.get(i++));
        }
    }

    private void updateCustomersList(ForAdminRefresherDTO adminRefresherDTO) {
        Platform.runLater(() -> {
            try {
                // Loans update
                ObservableList<ClientInformationDTO> customersFromTableView = customersInfoTableView.getItems();
                checkForChangesInCustomers(customersFromTableView, adminRefresherDTO.getAdminCustomerList());
                customersInfoTableView.refresh();

                // Customers update
                ObservableList<LoanInformationDTO> loansFromTableView = adminLoansTableView.getItems();
                checkForChangesInLoans(loansFromTableView, adminRefresherDTO.getAdminLoanList());
                adminLoansTableView.refresh();

                // Current Yaz Update
                mainController.updateCurrentYazByNumber(String.valueOf(adminRefresherDTO.getCurrentYaz()));
                if (adminRefresherDTO.getCurrentYaz() > mainController.getSavedCurrentYaz()) {
                    setCustomerTableViewVisibilityAndUnselected();
                    mainController.setSavedCurrentYaz(adminRefresherDTO.getCurrentYaz());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setCustomerTableViewVisibilityAndUnselected() {
        topInfoScrollPane.setVisible(false);
        adminLoansTableView.getSelectionModel().clearSelection();
    }

    public void startListRefresher() {
        listRefresher = new AdminRefresher(
                this::updateCustomersList
               );
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void fillLoanTablesInformation() throws IOException {
        String finalUrl = HttpUrl
                .parse(ADMIN_SHOW_LOANS)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                       alertPopUp("Loans information error", "Could not load information", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Loans information error", "Could not load information", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            loadLoanTableFromFXML();
                            rawBody = response.body().string();
                            LoanListDTO loanListDTO = GSON_INSTANCE.fromJson(rawBody, LoanListDTO.class);
                            setAdminScrollPanesVisibility(true);
                            mainController.showLoanInfo(loanListDTO.getLoanList(), true, true, topInfoScrollPane, "Admin Loans");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    public void fillCustomerTableInformation() {
        String finalUrl = HttpUrl
                .parse(ADMIN_SHOW_CUSTOMERS)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Customers information error", "Could not load information", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Customers information error", "Could not load information", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            //loadLoanTableFromFXML();
                            rawBody = response.body().string();
                            CustomersListDTO customersListDTO = GSON_INSTANCE.fromJson(rawBody, CustomersListDTO.class);
                            setCustomerTableColumns();
                            customersInfoTableView.setItems(FXCollections.observableArrayList(customersListDTO.getCustomerList()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    public void makeIncreaseYazButtonAble() {
        increaseYazButton.setDisable(false);
    }

    public void increaseYaz() throws Exception {
        String finalUrl = HttpUrl
                .parse(ADMIN_INCREASE_YAZ)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Increase Yaz error", "Could not increase yaz", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Increase Yaz error", "Could not increase yaz", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            mainController.updateCurrentYazByNumber(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}
