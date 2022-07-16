package mainApp.customer;

import DTO.client.ClientInformationDTO;
import DTO.client.PaymentsNotificationsDTO;
import DTO.client.RecentTransactionDTO;
import DTO.lists.CustomersListDTO;
import DTO.lists.LoanListDTO;
import DTO.loan.LoanInformationDTO;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import mainApp.AppController;
import mainApp.admin.*;
import mainApp.customer.scrambleExceptions.MaxOpenLoansException;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static client.util.Constants.*;
import static client.util.popup.AlertPopUp.alertPopUp;

public class CustomerController implements Initializable {
    private AppController mainController;
    private String customerName;
    private List<LoanInformationDTO> chosenLoans;
    private int moneyToInvest, minTotalYaz, maxOpenLoans;
    private Parent customerComponent;
    private ClientInformationDTO customer;

    private Task<Boolean> loanOptionsTask;
    List<String> selectedCategories;
    int minInterest;

    private TableView customerOpenLoansToPayTable;
    List<LoanInformationDTO> loanOptions;
    private SimpleIntegerProperty scrambleInterestSliderValue;
    private SimpleIntegerProperty scrambleOwnershipPercentageSliderValue;

    @FXML
    private Label scrambleErrorLabel;

    @FXML
    private Label scrambleInterestRangeLabel;

    @FXML
    private Slider scrambleOwnershipPercentageSlider;

    @FXML
    private Slider scrambleInterestSlider;

    @FXML
    private Label scrambleOwnershipPercentageRangeLabel;

    @FXML
    private TabPane customerTabPane;

    @FXML
    private Button chargeButton;

    @FXML
    private Button withdrawButton;

    @FXML
    private Label scrambleCurrentBalanceLabel;

    @FXML
    private Label currentBalanceLabel;

    @FXML
    private TableView<RecentTransactionDTO> accountTransactionsTableView;

    @FXML
    private TableColumn<RecentTransactionDTO, Integer> lastTransactionNumberCol;

    @FXML
    private TableColumn<RecentTransactionDTO, Integer> transactionYazCol;

    @FXML
    private TableColumn<RecentTransactionDTO, Integer> transactionAmountCol;

    @FXML
    private TableColumn<RecentTransactionDTO, Character> transactionKindCol;

    @FXML
    private TableColumn<RecentTransactionDTO, Integer> transactionBalanceBeforeCol;

    @FXML
    private TableColumn<RecentTransactionDTO, Integer> transactionBalanceAfterCol;

    @FXML
    private Label accountTransactionsLabel;

    @FXML
    private TableView<LoanInformationDTO> loanerLoansTableView;

    @FXML
    private TableColumn<LoanInformationDTO, String> loanerLoanIDCol;

    @FXML
    private TableColumn<LoanInformationDTO, String> loanerCategoryCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> loanerInitialFundCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> loanerYazBetweenPaymentsCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> loanerInterestCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> loanerSumCol;

    @FXML
    private TableColumn<LoanInformationDTO, String> loanerStatusCol;

    @FXML
    private TableView<LoanInformationDTO> lenderLoansTableView;

    @FXML
    private TableColumn<LoanInformationDTO, String> lenderLoanIDCol;

    @FXML
    private TableColumn<LoanInformationDTO, String> lenderCategoryCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> lenderInitialFundCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> lenderYazBetweenPaymentsCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> lenderInterestCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> lenderSumCol;

    @FXML
    private TableColumn<LoanInformationDTO, String> lenderStatusCol;

    @FXML
    private Label loanerLoansLabel;

    @FXML
    private Label lenderLoansLabel;

    @FXML
    private ScrollPane topInfoScrollPane;

    @FXML
    private ScrollPane bottomInfoScrollPane;

    @FXML
    private Button submitButton;

    @FXML
    private Label loanerLoansPaymentLabel;

    @FXML
    private TableView<PaymentsNotificationsDTO> notificationAreaTable;

    @FXML
    private TableColumn<PaymentsNotificationsDTO, Integer> messagePaymentNumberCol;

    @FXML
    private TableColumn<PaymentsNotificationsDTO, String> messageLoanIDCol;

    @FXML
    private TableColumn<PaymentsNotificationsDTO, Integer> messagePaymentYaz;

    @FXML
    private TableColumn<PaymentsNotificationsDTO, Character> messageSumCol;

    @FXML
    private Button paymentCloseLoanButton;

    @FXML
    private Label paymentControlsLabel;

    @FXML
    private Label notificationAreaLabel;

    @FXML
    private ScrollPane paymentInfoScrollPane;

    @FXML
    private ListView<String> scrambleCategoriesListView;

    @FXML
    private ScrollPane loansOptionsScrollPane;

    @FXML
    private ScrollPane statusInfoScrollPane;

    @FXML
    private TextField scrambleMoneyAmountTextField;

    @FXML
    private TextField scrambleMinOfTotalYazTextField;

    @FXML
    private TextField scrambleMaxOpenLoansTextField;

    @FXML
    private Label scrambleLoansOptionsLabel;

    @FXML
    private Button scrambleChooseLoansToInvestButton;

    @FXML
    private Label scrambleSuccessMessageLabel;

    @FXML
    private ScrollPane paymentLoanerLoansScrollPane;

    @FXML
    private Button payPaymentButton;

    @FXML
    private BorderPane customerBorderPane;

    @FXML
    private Tab informationTab;

    @FXML
    private Tab scrambleTab;

    @FXML
    private Tab paymentTab;

    @FXML
    private ProgressBar scrambleProgressBar;

    @FXML
    private Label scrambleProgressMessage;

    @FXML
    private Label scrambleMinTotalYazLabel;

    @FXML
    private Label scrambleMaxOpenLoansLabel;

    @FXML
    private Label scrambleMoneyToInvestLabel;

    @FXML
    private ScrollPane loanerLoansScrollPane;

    @FXML
    private ScrollPane lenderLoansScrollPane;

    @FXML
    private Button clickMe2Button;

    @FXML
    private Button loadFileButton;

    @FXML
    void loadFileButtonActionListener(ActionEvent event) throws Exception {
        Node node = (Node) event.getSource();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        fileChooser.setTitle("Load File");
        File chosenFile = fileChooser.showOpenDialog(node.getScene().getWindow());

        if (chosenFile != null) {
            loadFile(chosenFile.getPath());
        }
    }

    private void loadFile(String chosenFile) throws Exception {
        String finalUrl = HttpUrl
                .parse(Constants.CUSTOMER_LOAD_FILE)
                .newBuilder()
                .build()
                .toString();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file1", chosenFile,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(chosenFile)))
                .build();


        HttpClientUtil.runAsyncPost(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Load File Error");
                            alert.setHeaderText("Could not open your file");
                            alert.setContentText(e.getMessage());
                            alert.showAndWait();
                        }
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            Platform.runLater(() -> {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Load File Error");
                                        alert.setHeaderText("Could not open your file");
                                        alert.setContentText(responseBody);

                                        alert.showAndWait();
                                    }
                            ));
                } else {
                    Platform.runLater(() -> {
                        mainController.updateFilePath(chosenFile);
                        fillCustomerLoansTables(customerName);
                    });
                }
                response.close();
            }
        }, body);
    }

    @FXML
    void secondAnimationClickOnActionListener(ActionEvent event) {
        /*TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(2));
        transition.setNode(clickMe2Button);
        transition.setToY(-100);

        ScaleTransition transition1 = new ScaleTransition(Duration.seconds(2), clickMe2Button);
        transition1.setToX(2);
        transition1.setToZ(2);

        RotateTransition transition2 = new RotateTransition(Duration.seconds(2), clickMe2Button);
        transition2.setByAngle(360);

        ParallelTransition parallelTransition = new ParallelTransition(transition, transition1, transition2);

        parallelTransition.setOnFinished((event1 -> {
            FadeTransition fadeout = new FadeTransition(Duration.seconds(2), clickMe2Button);
            fadeout.setFromValue(1.0);
            fadeout.setToValue(0.0);
            fadeout.play();
        }));

        parallelTransition.play();*/
    }

    @FXML
    void closeLoanOnActionListener(ActionEvent event) throws Exception {
       /* LoanInformationDTO selectedLoan = (LoanInformationDTO) customerOpenLoansToPayTable.getSelectionModel().getSelectedItem();
        try {
            mainController.closeLoan(selectedLoan);
            mainController.setAllTables(customerName);
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Close Loan Error");
            alert.setHeaderText("Could not close loan");
            alert.setContentText(exception.getMessage());

            alert.showAndWait();
        }*/
    }

    @FXML
    void payPaymentOnActionListener(ActionEvent event) throws Exception {
       /* // if selected loan is in risk
        ClientInformationDTO customer = mainController.getCustomerByName(customerName);
        LoanInformationDTO selectedLoan = (LoanInformationDTO) customerOpenLoansToPayTable.getSelectionModel().getSelectedItem();

        if (selectedLoan.getLoanStatus().equals("RISK")) {
            TextInputDialog paymentDialog = new TextInputDialog();
            paymentDialog.setTitle("Loan In Risk");
            paymentDialog.setContentText("Please enter the amount of money you would like to pay:");
            paymentDialog.setHeaderText("Current Balance: " + (int) customer.getClientBalance() + "\nDebt Amount: " + selectedLoan.getDebt());
            paymentDialog.getDialogPane().setStyle(""); // #TODO
            paymentDialog.showAndWait();

            if (paymentDialog.getResult() != null) {
                try {
                    checkAmountOfPaymentToPayRiskLoan(paymentDialog.getResult(), selectedLoan.getDebt());
                    mainController.addPaymentToRiskLoan(selectedLoan, Integer.parseInt(paymentDialog.getResult()));
                } catch (NumberFormatException ex) {
                    showInvalidInputAlertDialog(ex);
                } catch (Exception ex) {
                    showPaymentErrorDialog(ex);
                }
            }
        } else {
            mainController.addPaymentToActiveLoan(selectedLoan);
        }

        mainController.setAllTables(customerName);*/
    }

    private void showInvalidInputAlertDialog(NumberFormatException ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pay Payment Error");
        alert.setHeaderText("Could not add your payment");
        alert.setContentText("Invalid input - should be numbers only. ");

        alert.showAndWait();
    }

    private void showPaymentErrorDialog(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pay Payment Error");
        alert.setHeaderText("Could not add your payment");
        alert.setContentText(ex.getMessage());

        alert.showAndWait();
    }

    private void checkAmountOfPaymentToPayRiskLoan(String result, int debt) throws Exception {
        int paymentAmountByUser = Integer.parseInt(result);

        if (paymentAmountByUser > debt) {
            throw new Exception("You cannot pay more than your debt amount when loan is in RISK.");
        } else if (paymentAmountByUser <= 0) {
            throw new Exception("Please enter a number bigger than 0.");
        }
    }

    @FXML
    void chargeOnActionListener(ActionEvent event) throws Exception {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Charge Money");
        dialog.setContentText("Please enter the amount of money you would like to add");
        dialog.setHeaderText("Current Balance: " + (int) customer.getClientBalance());
        dialog.showAndWait();

        if (dialog.getResult() != null) {
            String finalUrl = HttpUrl
                    .parse(Constants.CUSTOMER_DEPOSIT)
                    .newBuilder()
                    .addQueryParameter("amount", dialog.getResult())
                    .build()
                    .toString();

            HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            alertPopUp("Charge Error", "Could not charge your payment", e.getMessage())
                    );
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();
                        Platform.runLater(() ->
                                alertPopUp("Charge Error", "Could not charge your payment", responseBody)
                        );
                    } else {
                        Platform.runLater(() -> {
                            //todo: set all tables
                            fillAccountTransactionTableAndUpdateBalanceLabel();
                        });
                    }
                }
            });
        }
    }

    @FXML
    void withdrawOnActionListener(ActionEvent event) throws Exception {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Withdraw Money");
        dialog.setContentText("Please enter the amount of money you would like to withdraw");
        dialog.setHeaderText("Current Balance: " + (int) customer.getClientBalance());
        dialog.showAndWait();

        if (dialog.getResult() != null) {
            String finalUrl = HttpUrl
                    .parse(Constants.CUSTOMER_WITHDRAW)
                    .newBuilder()
                    .addQueryParameter("amount", dialog.getResult())
                    .build()
                    .toString();

            HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            alertPopUp("Withdraw Error", "Could not withdraw your payment", e.getMessage())
                    );
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();
                        Platform.runLater(() ->
                                alertPopUp("Withdraw Error", "Could not withdraw your payment", responseBody)
                        );
                    } else {
                        Platform.runLater(() -> {
                            fillAccountTransactionTableAndUpdateBalanceLabel();
                        });
                    }
                }
            });
        }
    }

    @FXML
    void chooseLoansOnActionListener(ActionEvent event) throws Exception {
       /* scrambleProgressMessage.setVisible(false);
        scrambleProgressBar.setVisible(false);

        int maxOwnershipPercentage = scrambleOwnershipPercentageSlider.getValue() == 0 ? 0 : (int) scrambleOwnershipPercentageSlider.getValue();

        mainController.loansDistribution(chosenLoans, maxOwnershipPercentage, moneyToInvest, customerName);
        mainController.setAllTables(customerName);

        scrambleSuccessMessageLabel.setText("Your investment money was distributed successfully!");
        clearAllScrambleFields();*/
    }

    private void clearAllScrambleFields() {
       /* scrambleProgressBar.setVisible(false);
        scrambleProgressMessage.setVisible(false);
        scrambleMoneyAmountTextField.clear();
        scrambleInterestSlider.setValue(0);
        scrambleInterestRangeLabel.setText("");
        scrambleMinOfTotalYazTextField.clear();
        scrambleMaxOpenLoansTextField.clear();
        scrambleOwnershipPercentageSlider.setValue(0);
        scrambleOwnershipPercentageRangeLabel.setText("");
        scrambleSetVisibility(false);*/
    }

    @FXML
    void submitOnActionListener(ActionEvent event) throws Exception {
        /*resetLabels();

        try {
            // calculate loans by parameters
            checkMoneyToInvest(scrambleMoneyAmountTextField.getText());
            checkMinOfTotalYazTextField(scrambleMinOfTotalYazTextField.getText());
            selectedCategories = scrambleCategoriesListView.getSelectionModel().getSelectedItems();
            minInterest = (int) scrambleInterestSlider.getValue();
            checkMaxOpenLoans(scrambleMaxOpenLoansTextField.getText());

            scrambleProgressBar.setVisible(true);
            scrambleProgressMessage.setVisible(true);

            startInvest();



        }
        catch (MoneyToInvestException moneyToInvestException) {
            moneyToInvestCatch(moneyToInvestException.getMoneyToInvestMessage());
        }
        catch (MinTotalYazException minTotalYazException) {
            minTotalYazCatch(minTotalYazException.getMinTotalYazMessage());
        }
        catch (MaxOpenLoansException maxOpenLoansException) {
            maxOpenLoansCatch(maxOpenLoansException.getMaxOpenLoansMessage());
        }
        catch (NotEnoughMoneyException notEnoughMoneyException) {
            accountBalanceErrorCatch(notEnoughMoneyException.getNotEnoughMoneyMessage());
        }*/
    }

    private void accountBalanceErrorCatch(String notEnoughMoneyMessage) {
        scrambleErrorLabel.setVisible(true);
        scrambleErrorLabel.setText(notEnoughMoneyMessage);
        scrambleErrorLabel.setStyle("-fx-text-fill: red");
        scrambleMoneyToInvestLabel.setStyle("-fx-text-fill: red");
    }

    private void resetLabels() {
        scrambleSuccessMessageLabel.setText("");
        scrambleErrorLabel.setText("");
        scrambleMoneyToInvestLabel.setStyle("-fx-text-fill: #273a3a");
        scrambleMinTotalYazLabel.setStyle("-fx-text-fill: #273a3a");
        scrambleMaxOpenLoansLabel.setStyle("-fx-text-fill: #273a3a");
    }

    private void maxOpenLoansCatch(String maxOpenLoansMessage) {
        scrambleErrorLabel.setVisible(true);
        scrambleErrorLabel.setText(maxOpenLoansMessage);
        scrambleErrorLabel.setStyle("-fx-text-fill: red");
        scrambleMaxOpenLoansLabel.setStyle("-fx-text-fill: red");
    }

    private void minTotalYazCatch(String minTotalYazMessage) {
        scrambleErrorLabel.setVisible(true);
        scrambleErrorLabel.setText(minTotalYazMessage);
        scrambleErrorLabel.setStyle("-fx-text-fill: red");
        scrambleMinTotalYazLabel.setStyle("-fx-text-fill: red");
    }

    private void moneyToInvestCatch(String moneyToInvestMessage) {
        scrambleErrorLabel.setVisible(true);
        scrambleErrorLabel.setText(moneyToInvestMessage);
        scrambleErrorLabel.setStyle("-fx-text-fill: red");
        scrambleMoneyToInvestLabel.setStyle("-fx-text-fill: red");
    }

    private void checkMaxOpenLoans(String maxOpenLoansTF) throws MaxOpenLoansException {
        if (maxOpenLoansTF.equals("")) {
            maxOpenLoans = 0;
        } else {
            try {
                maxOpenLoans = Integer.parseInt(maxOpenLoansTF);

                if (maxOpenLoans <= 0) {
                    throw new MaxOpenLoansException("Invalid Input, Please enter an integer bigger than 0");
                }
            } catch (NumberFormatException ex) {
                throw new MaxOpenLoansException("Invalid Input, Should be numbers only");
            }
        }
    }

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

   /* private void checkMoneyToInvest(String moneyToInvestTF) throws Exception {
        if(moneyToInvestTF.equals("")) {
            throw new MoneyToInvestException("Required Field");
        }
        else {
            try {
                moneyToInvest = Integer.parseInt(moneyToInvestTF);

                if (moneyToInvest <= 0) {
                    throw new MoneyToInvestException("Invalid Input, Please enter an integer bigger than 0");
                }

                ClientInformationDTO customer = mainController.getCustomerByName(customerName);
                if (moneyToInvest > customer.getClientBalance()) {
                    throw new NotEnoughMoneyException("There is not enough money in the account");
                }
            }
            catch (NumberFormatException ex) {
                throw new MoneyToInvestException("Invalid Input, Should be numbers only");
            }
        }
    }

    private void checkMinOfTotalYazTextField(String minOfTotalYazTF) throws Exception {
        if (minOfTotalYazTF.equals("")) {
            minTotalYaz = 0;
        }
        else {
            try {
                minTotalYaz = Integer.parseInt(minOfTotalYazTF);

                if (minTotalYaz <= 0) {
                    throw new MinTotalYazException("Invalid Input, Please enter an integer bigger than 0");
                }
            }
            catch (NumberFormatException ex) {
                throw new MinTotalYazException("Invalid Input, Should be numbers only");
            }
        }
    }

    private void loadLoanAndShowInformation(List<LoanInformationDTO> loanOptions, ScrollPane statusInfoScrollPane) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/mainApp/admin/loanInfo.fxml");
        fxmlLoader.setLocation(url);
        TableView loanOptionsTable = null;
        try {
            loanOptionsTable = fxmlLoader.load(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoanInfoController loanInfoController = fxmlLoader.getController();
        mainController.setLoanInfoController(loanInfoController);

        mainController.showLoanInfo(loanOptions, false, false, statusInfoScrollPane);
        loansOptionsScrollPane.setContent(loanOptionsTable);

        chosenLoans = loanOptionsTable.getSelectionModel().getSelectedItems();
        loanOptionsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        scrambleSetVisibility(true);
    }

    private void sleepForAWhile(int sleepTime) {
        if (sleepTime != 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {

            }
        }
    }

    public CustomerController() {
        scrambleInterestSliderValue = new SimpleIntegerProperty(0);
        scrambleOwnershipPercentageSliderValue = new SimpleIntegerProperty(0);
    }

    private void fillLoansTables(TableColumn<LoanInformationDTO, String> LoanIDCol, TableColumn<LoanInformationDTO, String> CategoryCol, TableColumn<LoanInformationDTO, Integer> InitialFundCol, TableColumn<LoanInformationDTO, Integer> YazBetweenPaymentsCol, TableColumn<LoanInformationDTO, Integer> InterestCol, TableColumn<LoanInformationDTO, Integer> SumCol, TableColumn<LoanInformationDTO, String> StatusCol) {
        LoanIDCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, String>("loanNameID"));
        CategoryCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, String>("loanCategory"));
        InitialFundCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, Integer>("loanStartSum"));
        YazBetweenPaymentsCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, Integer>("timeUnitsBetweenPayments"));
        InterestCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, Integer>("loanInterest"));
        SumCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, Integer>("sumAmount"));
        StatusCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, String>("loanStatus"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scrambleSetVisibility(false);
        scrambleProgressBar.setVisible(false);
        scrambleProgressMessage.setVisible(false);

        topInfoScrollPane.setVisible(false);
        bottomInfoScrollPane.setVisible(false);

        scrambleInterestSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            scrambleInterestRangeLabel.setText(String.valueOf(newValue.intValue()));
        });

        scrambleOwnershipPercentageSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            scrambleOwnershipPercentageRangeLabel.setText(String.valueOf(newValue.intValue()));
        });
    }

    public void setPayPaymentAndCloseLoanDisableByPaymentNotification(LoanInformationDTO currentLoan) {
        try {
            if (!(currentLoan.getLoanStatus().equals("NEW")) && mainController.isNewPaymentNotificationExist(customerName, currentLoan.getLoanNameID())) {
                payPaymentButton.setDisable(false);
                paymentCloseLoanButton.setDisable(false);
            } else {
                payPaymentButton.setDisable(true);
                paymentCloseLoanButton.setDisable(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scrambleSetVisibility(boolean visibility) {
        scrambleLoansOptionsLabel.setVisible(visibility);
        loansOptionsScrollPane.setVisible(visibility);
        statusInfoScrollPane.setVisible(visibility);
        scrambleChooseLoansToInvestButton.setVisible(visibility);
    }

    private void insertByStatus(LoanInformationDTO currentLoan, ScrollPane infoScrollPane) throws IOException {
        infoScrollPane.setVisible(false);
        switch (currentLoan.getLoanStatus().toString()) {
            case "PENDING": {
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource("/mainApp/admin/loan/pending/pendingStatusInfo.fxml");
                fxmlLoader.setLocation(url);
                GridPane pendingInfo = fxmlLoader.load(url.openStream());
                PendingInfoController pendingInfoController = fxmlLoader.getController();
                mainController.setPendingInfoController(pendingInfoController);

                //TODO:CSS
                //pendingInfo.getStylesheets().add(getClass().getResource("/mainApp/admin/PendingCSS2.css").toExternalForm());
                //pendingInfo.getStylesheets().add(getClass().getResource("/mainApp/admin/PendingCSS.css").toExternalForm());


                mainController.showPendingInfo(currentLoan);
                infoScrollPane.setVisible(true);
                infoScrollPane.setContent(pendingInfo);
                break;
            }
            case "ACTIVE": {
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource("/mainApp/admin/loan/active/activeStatusInfo.fxml");
                fxmlLoader.setLocation(url);
                GridPane activeInfo = fxmlLoader.load(url.openStream());
                ActiveInfoController activeInfoController = fxmlLoader.getController();
                mainController.setActiveInfoController(activeInfoController);

                //TODO:CSS
                //activeInfo.getStylesheets().add(getClass().getResource("/mainApp/admin/ActiveCSS2.css").toExternalForm());
                //activeInfo.getStylesheets().add(getClass().getResource("/mainApp/admin/ActiveCSS.css").toExternalForm());


                mainController.showActiveInfo(currentLoan);
                infoScrollPane.setVisible(true);
                infoScrollPane.setContent(activeInfo);
                break;
            }
            case "RISK": {
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource("/mainApp/admin/loan/risk/riskStatusInfo.fxml");
                fxmlLoader.setLocation(url);
                GridPane riskInfo = fxmlLoader.load(url.openStream());
                RiskInfoController riskInfoController = fxmlLoader.getController();
                mainController.setRiskInfoController(riskInfoController);

                //TODO:CSS
                //riskInfo.getStylesheets().add(getClass().getResource("/mainApp/admin/RiskCSS2.css").toExternalForm());
                //riskInfo.getStylesheets().add(getClass().getResource("/mainApp/admin/RiskCSS.css").toExternalForm());

                mainController.showRiskInfo(currentLoan);
                infoScrollPane.setVisible(true);
                infoScrollPane.setContent(riskInfo);
                break;
            }
            case "FINISHED": {
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource("/mainApp/admin/loan/finished/finishedStatusInfo.fxml");
                fxmlLoader.setLocation(url);
                GridPane finishedInfo = fxmlLoader.load(url.openStream());
                FinishedInfoController finishedInfoController = fxmlLoader.getController();
                mainController.setFinishedInfoController(finishedInfoController);

                //TODO:CSS
                //finishedInfo.getStylesheets().add(getClass().getResource("/mainApp/admin/FinishedCSS2.css").toExternalForm());
                //finishedInfo.getStylesheets().add(getClass().getResource("/mainApp/admin/FinishedCSS.css").toExternalForm());

                mainController.showFinishedInfo(currentLoan);
                infoScrollPane.setVisible(true);
                infoScrollPane.setContent(finishedInfo);
                break;
            }
        }
    }

    public void setCustomerComponent(ScrollPane customerView) {
        this.customerComponent = customerView;
    }

    public void setCustomerStyleSheet(String value) {
        switch (value) {
            case "Default":
                customerComponent.getStylesheets().clear();
                break;
            case "Skin1":
                customerComponent.getStylesheets().clear();
                customerComponent.getStylesheets().add(getClass().getResource("customerCSS.css").toExternalForm());
                break;
            case "Skin2":
                customerComponent.getStylesheets().clear();
                customerComponent.getStylesheets().add(getClass().getResource("customerCSS2.css").toExternalForm());
                break;
        }
    }

    public void startInvest() {
        Task<Boolean> taskLoan = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                loanOptions = mainController.getLoanOptions(customerName, moneyToInvest, selectedCategories, minInterest, minTotalYaz, maxOpenLoans);

                updateMessage("working...");
                updateProgress(0,100);

                for (int i = 0; i < 100; i++) {
                    Thread.sleep(10);
                    updateProgress(i, 100);
                }

                updateProgress(100,100);
                updateMessage("done!");
                return true;
            }
        };
        scrambleProgressBar.progressProperty().bind(taskLoan.progressProperty());
        scrambleProgressMessage.textProperty().bind(taskLoan.messageProperty());

        // Task listener
        taskLoan.valueProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    loadLoanAndShowInformation(loanOptions, statusInfoScrollPane);
                    statusInfoScrollPane.setVisible(false);
                }
            }
        });

        new Thread(taskLoan, "Loan investing thread").start();
    }*/

    public void fillCustomerLoansTables(String customerName) {
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
                            rawBody = response.body().string();
                            CustomersListDTO customersListDTO = GSON_INSTANCE.fromJson(rawBody, CustomersListDTO.class);
                            ClientInformationDTO relevantCustomer = findCustomerDTOByName(customersListDTO, customerName);
                            //update customerDTO
                            customer = relevantCustomer;
                            if (relevantCustomer != null) {
                                //todo: to pull
                                loadCustomerTablesFromFXML(lenderLoansScrollPane);
                                mainController.showLoanInfo(relevantCustomer.getClientAsLenderLoanList(), false, false, bottomInfoScrollPane);
                                loadCustomerTablesFromFXML(loanerLoansScrollPane);
                                mainController.showLoanInfo(relevantCustomer.getClientAsBorrowerLoanList(), false, false, topInfoScrollPane);
                                fillAccountTransactionTableAndUpdateBalanceLabel();
                            } else {
                                throw new Exception("customer not found");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private ClientInformationDTO findCustomerDTOByName(CustomersListDTO customersListDTO, String customerName) {
        List<ClientInformationDTO> customersList = customersListDTO.getCustomerList();

        for (ClientInformationDTO clientDTO : customersList) {
            if (clientDTO.getClientName().equals(customerName)) {
                return clientDTO;
            }
        }

        return null;
    }

    private void loadCustomerTablesFromFXML(ScrollPane scrollPaneToLoadTo) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/mainApp/admin/loanInfo.fxml");
        fxmlLoader.setLocation(url);
        TableView loansTable = fxmlLoader.load(url.openStream());
        LoanInfoController loanInfoController = fxmlLoader.getController();
        mainController.setLoanInfoController(loanInfoController);

        scrollPaneToLoadTo.setContent(loansTable);
    }

    public void makeLoanTablesVisible() {
        loanerLoansScrollPane.setVisible(true);
        lenderLoansScrollPane.setVisible(true);
    }

    public void setCustomerNameInCustomerController(String userName) {
        customerName = userName;
    }

    private void fillAccountTransactionTableAndUpdateBalanceLabel() {
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
                            rawBody = response.body().string();
                            CustomersListDTO customersListDTO = GSON_INSTANCE.fromJson(rawBody, CustomersListDTO.class);
                            ClientInformationDTO relevantCustomer = findCustomerDTOByName(customersListDTO, customerName);
                            if (relevantCustomer != null) {
                                List<RecentTransactionDTO> accountTransactions = relevantCustomer.getRecentTransactionList();
                                setTransactionsTable(accountTransactions);
                                currentBalanceLabel.setText("Current Balance: " + (int) relevantCustomer.getClientBalance());
                            } else {
                                throw new Exception("customer not found");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void setTransactionsTable(List<RecentTransactionDTO> accountTransactions) {
        lastTransactionNumberCol.setCellValueFactory(new PropertyValueFactory<RecentTransactionDTO, Integer>("transActionNumber"));
        transactionYazCol.setCellValueFactory(new PropertyValueFactory<RecentTransactionDTO, Integer>("transactionTimeUnit"));
        transactionAmountCol.setCellValueFactory(new PropertyValueFactory<RecentTransactionDTO, Integer>("amountOfTransaction"));
        transactionKindCol.setCellValueFactory(new PropertyValueFactory<RecentTransactionDTO, Character>("kindOfTransaction"));
        transactionBalanceBeforeCol.setCellValueFactory(new PropertyValueFactory<RecentTransactionDTO, Integer>("balanceBeforeTransaction"));
        transactionBalanceAfterCol.setCellValueFactory(new PropertyValueFactory<RecentTransactionDTO, Integer>("balanceAfterTransaction"));

        accountTransactionsTableView.setItems(FXCollections.observableArrayList(accountTransactions));
    }

    public void loadCustomerInformation(String customerName, List<String> categoriesList, List<PaymentsNotificationsDTO> paymentsNotificationsDTOList) throws Exception {
        clearAllScrambleFields();

        if (customer != null) {
            // information tab

            loadCustomerTablesFromFXML(lenderLoansScrollPane);
            mainController.showLoanInfo(customer.getClientAsLenderLoanList(), false, false, bottomInfoScrollPane);
            loadCustomerTablesFromFXML(loanerLoansScrollPane);
            mainController.showLoanInfo(customer.getClientAsBorrowerLoanList(), false, false, topInfoScrollPane);
            fillAccountTransactionTableAndUpdateBalanceLabel();

            currentBalanceLabel.setText("Current Balance: " + (int) customer.getClientBalance());

            // scramble tab
            fillCategoriesOnScrambleTab(categoriesList);
            scrambleCurrentBalanceLabel.setText("(Current Balance: " + (int) customer.getClientBalance() + ")");

            // payment tab
            // TODO: a method for loan loading
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/mainApp/admin/loanInfo.fxml");
            fxmlLoader.setLocation(url);
            customerOpenLoansToPayTable = fxmlLoader.load(url.openStream());
            LoanInfoController loanInfoController = fxmlLoader.getController();
            mainController.setLoanInfoController(loanInfoController);

            fillOpenLoansToPay();
            setNotificationAreaTable(paymentsNotificationsDTOList);

            topInfoScrollPane.setVisible(false);
            bottomInfoScrollPane.setVisible(false);
            payPaymentButton.setDisable(true);
            paymentCloseLoanButton.setDisable(true);
        } else {
            throw new Exception("client was not found");
        }
    }

    private void fillOpenLoansToPay() {
        String finalUrl = HttpUrl
                .parse(CUSTOMER_OPEN_LOANS_TO_PAY)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Customer information error", "Could not load information", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Customer information error", "Could not load information", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            LoanListDTO loanListDTO = GSON_INSTANCE.fromJson(rawBody, LoanListDTO.class);
                            List<LoanInformationDTO> customerOpenLoansToPayList = loanListDTO.getLoanList();
                            mainController.showLoanInfo(customerOpenLoansToPayList, false, false, paymentInfoScrollPane);
                            paymentLoanerLoansScrollPane.setContent(customerOpenLoansToPayTable);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void fillCategoriesOnScrambleTab(List<String> categoriesList) {
        for (String category : categoriesList) {
            scrambleCategoriesListView.getItems().add(category);
        }

        scrambleCategoriesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void setNotificationAreaTable(List<PaymentsNotificationsDTO> paymentsNotificationsDTOList) {
        messagePaymentNumberCol.setCellValueFactory(new PropertyValueFactory<PaymentsNotificationsDTO, Integer>("paymentNotificationNumber"));
        messageLoanIDCol.setCellValueFactory(new PropertyValueFactory<PaymentsNotificationsDTO, String>("loanID"));
        messagePaymentYaz.setCellValueFactory(new PropertyValueFactory<PaymentsNotificationsDTO, Integer>("paymentYaz"));
        messageSumCol.setCellValueFactory(new PropertyValueFactory<PaymentsNotificationsDTO, Character>("sum"));

        notificationAreaTable.setItems(FXCollections.observableList(paymentsNotificationsDTOList));
    }

    public void setCustomerDTOInCustomerController(String userName) {
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
                            rawBody = response.body().string();
                            CustomersListDTO customersListDTO = GSON_INSTANCE.fromJson(rawBody, CustomersListDTO.class);
                            ClientInformationDTO relevantCustomer = findCustomerDTOByName(customersListDTO, userName);
                            if (relevantCustomer != null) {
                                customer = relevantCustomer;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}