package mainApp.customer;

import DTO.client.ClientInformationDTO;
import DTO.client.PaymentsNotificationsDTO;
import DTO.client.RecentTransactionDTO;
import DTO.lists.*;
import DTO.loan.LoanForSaleDTO;
import DTO.loan.LoanInformationDTO;
import DTO.loan.PaymentsDTO;
import DTO.loan.scramble.InvestmentLoanInformationDTO;
import DTO.refresher.ForCustomerRefresherDTO;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import mainApp.customer.scrambleExceptions.MinTotalYazException;
import mainApp.customer.scrambleExceptions.MoneyToInvestException;
import mainApp.customer.scrambleExceptions.NotEnoughMoneyException;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static client.util.Constants.*;
import static client.util.popup.AlertPopUp.alertPopUp;

public class CustomerController implements Initializable {
    private AppController mainController;
    private String customerName;
    private ObservableList<LoanInformationDTO> chosenLoans;
    private int moneyToInvest, minTotalYaz, maxOpenLoans, maxOwnershipPercentage;
    private Parent customerComponent;
    private ClientInformationDTO customer;
    private int customerBalance;
    private LoanForSaleDTO loanToBuy;

    private Task<Boolean> loanOptionsTask;
    List<String> selectedCategories;
    int minInterest;

    List<LoanInformationDTO> loanOptions;
    private SimpleIntegerProperty scrambleInterestSliderValue;
    private SimpleIntegerProperty scrambleOwnershipPercentageSliderValue;

    private Timer timer;
    private TimerTask listRefresher;

    @FXML private TableView lendersComponent;
    @FXML private LendersController lendersComponentController;
    @FXML private TableView paymentsComponent;
    @FXML private PaymentsController paymentsComponentController;

    @FXML
    private TableView paymentLoanerLoansTableView;

    @FXML
    private TableView loansForSaleTableView;

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
    private TableView loanerLoansTableView;

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
    private TableView lenderLoansTableView;

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
    private Slider newLoanInterestSlider;

    @FXML
    private TextField newLoanIDTextField;

    @FXML
    private TextField newLoanAmountTextField;

    @FXML
    private TextField newLoanTotalYazTimeTextField;

    @FXML
    private TextField newLoanYazBetweenPaymentTextField;

    @FXML
    private Label newLoanInterestRangeLabel;

    @FXML
    private Button addNewLoanButton;

    @FXML
    private Label newLoanSuccessLabel;

    @FXML
    private TextField newLoanCategoryTextField;

    @FXML
    private Label loanPriceLabel;

    @FXML
    private Button buyLoanButton;

    @FXML
    private ScrollPane loanForSaleScrollPane;

    @FXML
    private Button sellLoanButton;

    @FXML
    void sellLoanClicked(ActionEvent event) {
        LoanInformationDTO loanToSell = (LoanInformationDTO) lenderLoansTableView.getSelectionModel().getSelectedItem();

        String finalUrl = HttpUrl
                .parse(Constants.CUSTOMER_SELL_LOAN)
                .newBuilder()
                .addQueryParameter("loan", loanToSell.getLoanNameID())
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Sell Loan Error", "Could not put your loan to sale", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Sell Loan Error", "Could not put your loan to sale", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        lenderLoansTableView.getSelectionModel().clearSelection();
                        sellLoanButton.setDisable(true);
                    });
                }
            }
        });
    }

    @FXML
    void buyLoanClicked(ActionEvent event) {
        findLoanToBuy((LoanInformationDTO)loansForSaleTableView.getSelectionModel().getSelectedItem());
    }

    private void findLoanToBuy(LoanInformationDTO selectedItem) {
        String finalUrl = HttpUrl
                .parse(CUSTOMER_LOAN_FOR_SALE)
                .newBuilder()
                .addQueryParameter("username", customerName)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Customer Loans for sale error", "Could not load information", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Customer Loans for sale error", "Could not load information", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            LoanForSaleListDTO loanForSaleListDTO = GSON_INSTANCE.fromJson(rawBody, LoanForSaleListDTO.class);
                            loanToBuy = checkInLoansListForLoanToBuy(loanForSaleListDTO.getLoanForSaleDTOList(), selectedItem);
                            buyLoanCountinue(loanToBuy, selectedItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void buyLoanCountinue(LoanForSaleDTO loanToBuy, LoanInformationDTO selectedItem) {
        if (loanToBuy != null) {
            String finalUrl = HttpUrl
                    .parse(CUSTOMER_BUY_LOAN)
                    .newBuilder()
                    .build()
                    .toString();

            String loanToBuyJson = GSON_INSTANCE.toJson(loanToBuy);

            HttpClientUtil.runAsyncJson(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            alertPopUp("Buy loan error", "Could not buy loan", e.getMessage())
                    );
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();
                        Platform.runLater(() ->
                                alertPopUp("Buy loan error", "Could not buy loan", responseBody)
                        );
                    } else {
                        Platform.runLater(() -> {
                            buyLoanButton.setDisable(true);
                        });
                    }
                }
            }, loanToBuyJson);
        }
        else {
            alertPopUp("Error", "Something went wrong", "");
        }
    }

    private LoanForSaleDTO checkInLoansListForLoanToBuy(List<LoanForSaleDTO> loanForSaleDTOList, LoanInformationDTO selectedItem) {
        for (LoanForSaleDTO loanForSale : loanForSaleDTOList) {
            if (loanForSale.getLoanNumber() == selectedItem.getLoanNumber()) {
                return loanForSale;
            }
        }

        return null;
    }

    @FXML
    void addNewLoanClicked(ActionEvent event) {
        String loanID = newLoanIDTextField.getText();
        int loanAmount, totalYazTime, yazBetweenPayment, loanInterest;
        loanAmount = Integer.parseInt(newLoanAmountTextField.getText());
        totalYazTime = Integer.parseInt(newLoanTotalYazTimeTextField.getText());
        yazBetweenPayment = Integer.parseInt(newLoanYazBetweenPaymentTextField.getText());
        loanInterest = (int) newLoanInterestSlider.getValue();
        String selectedCategory = newLoanCategoryTextField.getText();

        LoanInformationDTO loanToAdd = new LoanInformationDTO();
        loanToAdd.setLoanNameID(loanID);
        loanToAdd.setLoanStartSum(loanAmount);
        loanToAdd.setLoanSumOfTimeUnit(totalYazTime);
        loanToAdd.setTimeUnitsBetweenPayments(yazBetweenPayment);
        loanToAdd.setLoanInterest(loanInterest);
        loanToAdd.setLoanCategory(selectedCategory);
        loanToAdd.setBorrowerName(customerName);

        addNewLoanToCustomer(loanToAdd);
    }

    private void addNewLoanToCustomer(LoanInformationDTO loanToAdd) {
        String finalUrl = HttpUrl
                .parse(CUSTOMER_ADD_NEW_LOAN)
                .newBuilder()
                .build()
                .toString();

        String loanToAddInJson = GSON_INSTANCE.toJson(loanToAdd);

        HttpClientUtil.runAsyncJson(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Add New Loan error", "Could not add new loan", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Add New Loan error", "Could not add new loan", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            clearAllAddNewLoanFields();
                            newLoanSuccessLabel.setText("Loan was added successfully!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }, loanToAddInJson);
    }

    private void clearAllAddNewLoanFields() {
        newLoanInterestRangeLabel.setText("");
        newLoanInterestSlider.setValue(0);
        newLoanAmountTextField.clear();
        newLoanIDTextField.clear();
        newLoanCategoryTextField.clear();
        newLoanTotalYazTimeTextField.clear();
        newLoanYazBetweenPaymentTextField.clear();
    }

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
        LoanInformationDTO selectedLoan = (LoanInformationDTO) paymentLoanerLoansTableView.getSelectionModel().getSelectedItem();

        String finalUrl = HttpUrl
                .parse(CUSTOMER_CLOSE_LOAN)
                .newBuilder()
                .build()
                .toString();

        String loanToCloseInJson = GSON_INSTANCE.toJson(selectedLoan);

        HttpClientUtil.runAsyncJson(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Close Loan error", "Could not close loan", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Close Loan error", "Could not close loan", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            setCustomerTableViewVisibilityAndUnselected();
                            payPaymentButton.setDisable(true);
                            paymentCloseLoanButton.setDisable(true);
                        } catch (Exception e) {
                            setCustomerTableViewVisibilityAndUnselected();
                            payPaymentButton.setDisable(true);
                            paymentCloseLoanButton.setDisable(true);
                            e.printStackTrace();
                        }
                    });
                }
            }
        }, loanToCloseInJson);
    }

    @FXML
    void payPaymentOnActionListener(ActionEvent event) throws Exception {
        LoanInformationDTO selectedLoanToPay = (LoanInformationDTO) paymentLoanerLoansTableView.getSelectionModel().getSelectedItem();
        int amountToPay = 0;

        if (selectedLoanToPay.getLoanStatus().equals("RISK")) { // if selected loan is in risk
            try {
                amountToPay = getAmountToPayFromClient(selectedLoanToPay);
            }
            catch (Exception e) {
                paymentLoanerLoansTableView.getSelectionModel().clearSelection();
                return ; // the customer did not fill amount of money, get out of pay payment.
            }
        }  // else - the server is taking care of that

        LoanInfoAndPaymentAmountDTO loanInfoAndPaymentAmountDTO = new LoanInfoAndPaymentAmountDTO();
        loanInfoAndPaymentAmountDTO.setAmountToPay(amountToPay);
        loanInfoAndPaymentAmountDTO.setLoanToPay(selectedLoanToPay);

        String finalUrl = HttpUrl
                .parse(CUSTOMER_LOAN_PAYMENT)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncPayLoanPayment(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Loan Payment error","Something went wrong: ", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Loan Payment error","Something went wrong: ", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        setCustomerTableViewVisibilityAndUnselected();
                        payPaymentButton.setDisable(true);
                        paymentCloseLoanButton.setDisable(true);
                    });
                }
            }
        }, loanInfoAndPaymentAmountDTO);
    }

    private void setCustomerTableViewVisibilityAndUnselected() {
        topInfoScrollPane.setVisible(false);
        bottomInfoScrollPane.setVisible(false);
        paymentInfoScrollPane.setVisible(false);
        lenderLoansTableView.getSelectionModel().clearSelection();
        loanerLoansTableView.getSelectionModel().clearSelection();
        paymentLoanerLoansTableView.getSelectionModel().clearSelection();
    }

    private int getAmountToPayFromClient(LoanInformationDTO selectedLoanToPay) throws Exception {
        int amountToPay = 0;

        TextInputDialog paymentDialog = new TextInputDialog();
        paymentDialog.setTitle("Loan In Risk");
        paymentDialog.setContentText("Please enter the amount of money you would like to pay:");
        paymentDialog.setHeaderText("Current Balance: " + customerBalance + "\nDebt Amount: " + selectedLoanToPay.getDebt());
        paymentDialog.getDialogPane().setStyle("");
        paymentDialog.showAndWait();

        if (paymentDialog.getResult() != null) {
            try {
                checkAmountOfPaymentToPayRiskLoan(paymentDialog.getResult(), selectedLoanToPay.getDebt());
                amountToPay = Integer.parseInt(paymentDialog.getResult());
            } catch (NumberFormatException ex) {
                showInvalidInputAlertDialog(ex);
                throw new Exception("");
            } catch (Exception ex) {
                showPaymentErrorDialog(ex);
                throw new Exception("");
            }
        }
        else {
            throw new Exception("The customer did not fill the field.");
        }

        return amountToPay;
    }

    private void updateCustomersList(ForCustomerRefresherDTO customerRefresherDTO) {
        Platform.runLater(() -> {
            try {
                // Information Loaner Loans update:
                ObservableList<LoanInformationDTO> loanerLoansFromTableView = loanerLoansTableView.getItems();
                checkForChangesLoans(loanerLoansFromTableView, customerRefresherDTO.getCustomerLonerLoansList());
                loanerLoansTableView.refresh();

                // Information Lender Loans update:
                ObservableList<LoanInformationDTO> lenderLoansFromTableView = lenderLoansTableView.getItems();
                checkForChangesLoans(lenderLoansFromTableView, customerRefresherDTO.getCustomerLenderLoansList());
                lenderLoansTableView.refresh();

                // Recent Transactions List:
                ObservableList<RecentTransactionDTO> recentTransactionsFromTableView = accountTransactionsTableView.getItems();
                checkForChangesInRecentTransactions(recentTransactionsFromTableView, customerRefresherDTO.getCustomerRecentTransactionsList());
                accountTransactionsTableView.refresh();

                // Customer Balance:
                currentBalanceLabel.setText("Current Balance: " + customerRefresherDTO.getCustomerBalance());
                scrambleCurrentBalanceLabel.setText("(Current Balance: " + customerRefresherDTO.getCustomerBalance() + ")");
                customerBalance = customerRefresherDTO.getCustomerBalance();

                // Customer Payment Loaner Loans List:
                ObservableList<LoanInformationDTO> customerPaymentLoanerLoansListFromTableView = paymentLoanerLoansTableView.getItems();
                checkForChangesLoans(customerPaymentLoanerLoansListFromTableView, customerRefresherDTO.getCustomerPaymentLoanerLoansList());
                paymentLoanerLoansTableView.refresh();

                // Payment Notifications:
                ObservableList<PaymentsNotificationsDTO> paymentsNotificationsFromTableView = notificationAreaTable.getItems();
                checkForChangesInNotificationArea(paymentsNotificationsFromTableView, customerRefresherDTO.getCustomerPaymentNotificationsList());
                notificationAreaTable.refresh();

                // Current Yaz:
                if (customerRefresherDTO.getCurrentYaz() > mainController.getSavedCurrentYaz()) {
                    setCustomerTableViewVisibilityAndUnselected();
                    payPaymentButton.setDisable(true);
                    paymentCloseLoanButton.setDisable(true);
                    mainController.setSavedCurrentYaz(customerRefresherDTO.getCurrentYaz());
                    checkForLoansForSalesRiskLoans(customerRefresherDTO.getCustomerLenderLoansList());
                }
                mainController.updateCurrentYazByNumber(String.valueOf(customerRefresherDTO.getCurrentYaz()));

                // Categories List:
                fillCategoriesOnScrambleTab(customerRefresherDTO.getLoanCategoryList());

                // buy/sell loans
                ObservableList<LoanInformationDTO> loansForSaleFromTableView = loansForSaleTableView.getItems();
                checkForChangesLoans(loansForSaleFromTableView, customerRefresherDTO.getLoansForSaleList().getLoanForSaleListInformationDTO());
                //fillLoanPriceLoansForSale(customerRefresherDTO.getLoansForSaleList().getLoanForSaleDTOList());
                loansForSaleTableView.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void checkForLoansForSalesRiskLoans(List<LoanInformationDTO> customerLenderLoansList) {
        for (LoanInformationDTO loan : customerLenderLoansList) { //todo: fix
           if (loan.getLoanStatus().equals("RISK") && loansForSaleTableView.getItems().contains(loan)) {
               alertPopUp("Attention!","Loans For Sale list has changed","The loan: " + loan.getLoanNameID() + "is in RISK now.");
           }
        }
    }

    private void checkForChangesInNotificationArea(ObservableList<PaymentsNotificationsDTO> oldPaymentsNotificationsList, List<PaymentsNotificationsDTO> newPaymentsNotificationsList) {
        int i = 0;

        for (PaymentsNotificationsDTO oldPaymentsNotification : oldPaymentsNotificationsList) {
            PaymentsNotificationsDTO currentPaymentsNotification = newPaymentsNotificationsList.get(i++);
            if (oldPaymentsNotification.getPaymentNotificationNumber() == currentPaymentsNotification.getPaymentNotificationNumber()) {
                oldPaymentsNotification.setPaymentYaz(currentPaymentsNotification.getPaymentYaz());
                oldPaymentsNotification.setSum(currentPaymentsNotification.getSum());
            }
        }

        while (newPaymentsNotificationsList.size() > i) {
            oldPaymentsNotificationsList.add(newPaymentsNotificationsList.get(i++));
        }

    }

    private void checkForChangesInRecentTransactions(ObservableList<RecentTransactionDTO> oldRecentTransactionList, List<RecentTransactionDTO> newRecentTransactionList) {
        int i = 0;

        for (RecentTransactionDTO oldRecentTransactionDTO : oldRecentTransactionList) {
            RecentTransactionDTO currentRecentTransaction = newRecentTransactionList.get(i++);
            if (oldRecentTransactionDTO.getTransActionNumber() == currentRecentTransaction.getTransActionNumber()) {
                oldRecentTransactionDTO.setAmountOfTransaction((int)currentRecentTransaction.getAmountOfTransaction());
                oldRecentTransactionDTO.setKindOfTransaction(currentRecentTransaction.getKindOfTransaction());
                oldRecentTransactionDTO.setBalanceAfterTransaction((int)currentRecentTransaction.getBalanceAfterTransaction());
                oldRecentTransactionDTO.setTransactionTimeUnit(currentRecentTransaction.getTransactionTimeUnit());
                oldRecentTransactionDTO.setBalanceBeforeTransaction((int)currentRecentTransaction.getBalanceBeforeTransaction());
            }
        }

        while (newRecentTransactionList.size() > i) {
            oldRecentTransactionList.add(newRecentTransactionList.get(i++));
        }
    }

    private void checkForChangesLoans(ObservableList<LoanInformationDTO> oldLoansList, List<LoanInformationDTO> newLoanList) {
        if (newLoanList.size() < oldLoansList.size()) {
            oldLoansList.clear();
            oldLoansList.addAll(newLoanList);
        }

        int i = 0;

        for (LoanInformationDTO oldLoanDTO : oldLoansList) {
            LoanInformationDTO currentNewLoan = newLoanList.get(i++);
            if (oldLoanDTO.getLoanNameID().equals(currentNewLoan.getLoanNameID())) {
                oldLoanDTO.setStatus(currentNewLoan.getLoanStatus());
                oldLoanDTO.setFundAmount(currentNewLoan.getFundAmount());
                oldLoanDTO.setInterestAmount(currentNewLoan.getInterestAmount());
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
                oldLoanDTO.setDebt(currentNewLoan.getDebt());
                oldLoanDTO.setLastPaymentTimeunit(currentNewLoan.getLastPaymentTimeunit());
                oldLoanDTO.setAmountToPayNextPayment(currentNewLoan.getAmountToPayNextPayment());
                oldLoanDTO.setFundToPayNextPayment(currentNewLoan.getFundToPayNextPayment());
                oldLoanDTO.setInterestToPayNextPayment(currentNewLoan.getInterestToPayNextPayment());
            }
        }

        while (newLoanList.size() > i) {
            oldLoansList.add(newLoanList.get(i++));
        }
    }

/*    private void checkForChangesInPaymentsOfLoan(List<PaymentsDTO> oldPaymentsList, List<PaymentsDTO> newPaymentsList) {
        int i = 0;

        for (PaymentsDTO oldPaymentDTO : oldPaymentsList) {
            PaymentsDTO currentPaymentDTO = newPaymentsList.get(i++);
            if (oldPaymentDTO.g().equals(currentPaymentDTO.getLenderName())) {
                oldPaymentDTO.setAmountOfLoan(currentPaymentDTO.getAmountOfLoan());
            }
        }

        while (newLenderSetAndAmountsList.size() > i) {
            oldLenderSetAndAmountsList.add(newLenderSetAndAmountsList.get(i++));
        }
    }

    private void checkForChangesInLendersOfLoan(List<PartInLoanDTO> oldLenderSetAndAmountsList, List<PartInLoanDTO> newLenderSetAndAmountsList) {
        int i = 0;

        for (PartInLoanDTO oldPartInLoanDTO : oldLenderSetAndAmountsList) {
            PartInLoanDTO currentPartInLoanDTO = newLenderSetAndAmountsList.get(i++);
            if (oldPartInLoanDTO.getLenderName().equals(currentPartInLoanDTO.getLenderName())) {
                oldPartInLoanDTO.setAmountOfLoan(currentPartInLoanDTO.getAmountOfLoan());
            }
        }

        while (newLenderSetAndAmountsList.size() > i) {
            oldLenderSetAndAmountsList.add(newLenderSetAndAmountsList.get(i++));
        }
    }*/

    public void startListRefresher() {
        listRefresher = new CustomerRefresher(
                this::updateCustomersList
        );
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
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
        else if (paymentAmountByUser > customerBalance) {
            throw new Exception("There is not enough money in the account.");
        }
    }

    @FXML
    void chargeOnActionListener(ActionEvent event) throws Exception {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Charge Money");
        dialog.setContentText("Please enter the amount of money you would like to add");
        dialog.setHeaderText("Current Balance: " + customerBalance);
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
        dialog.setHeaderText("Current Balance: " + customerBalance);
        dialog.showAndWait();

        try {
            checkAmountToWithdraw(dialog.getResult());
        } catch (Exception e) {
            alertPopUp("Withdraw Error", "Could not withdraw the money", e.getMessage());
            return ;
        }

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

    private void checkAmountToWithdraw(String amountToWithdraw) throws Exception {
        if(amountToWithdraw.equals("")) {
            throw new MoneyToInvestException("Required Field");
        } else {
            int amountInInt = Integer.parseInt(amountToWithdraw);

            if (amountInInt <= 0) {
                throw new MoneyToInvestException("Invalid Input, Please enter an integer bigger than 0");
            }

            if (amountInInt > customerBalance) {
                throw new Exception("There is not enough money in the account");
            }
        }
    }

    @FXML
    void chooseLoansOnActionListener(ActionEvent event) throws Exception {
        //todo: delete progress details
        scrambleProgressMessage.setVisible(false);
        scrambleProgressBar.setVisible(false);

        LoanListDTO loanListDTO = new LoanListDTO();
        loanListDTO.setLoanList(chosenLoans);

        String finalUrl = HttpUrl
                .parse(CUSTOMER_INVEST_IN_CHOSEN_LOANS)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncSelectedLoans(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("There is not Customer by this name","Something went wrong: ", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Error","Something went wrong: ", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            scrambleSuccessMessageLabel.setText("Your investment money was distributed successfully!");
                            clearAllScrambleFields();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }, loanListDTO);
    }

    private void clearAllScrambleFields() {
        scrambleProgressBar.setVisible(false);
        scrambleProgressMessage.setVisible(false);
        scrambleMoneyAmountTextField.clear();
        scrambleInterestSlider.setValue(0);
        scrambleInterestRangeLabel.setText("");
        scrambleMinOfTotalYazTextField.clear();
        scrambleMaxOpenLoansTextField.clear();
        scrambleOwnershipPercentageSlider.setValue(0);
        scrambleOwnershipPercentageRangeLabel.setText("");
        scrambleSetVisibility(false);
    }

    @FXML
    void submitOnActionListener(ActionEvent event) throws Exception {
        resetLabels();

        try {
            // calculate loans by parameters
            checkMoneyToInvest(scrambleMoneyAmountTextField.getText());
            checkMinOfTotalYazTextField(scrambleMinOfTotalYazTextField.getText());
            selectedCategories = scrambleCategoriesListView.getSelectionModel().getSelectedItems();
            minInterest = (int) scrambleInterestSlider.getValue();
            checkMaxOpenLoans(scrambleMaxOpenLoansTextField.getText());
            maxOwnershipPercentage = scrambleOwnershipPercentageSlider.getValue() == 0 ? 0 : (int) scrambleOwnershipPercentageSlider.getValue();

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
        }
    }

    private void startInvest() {
        String finalUrl = HttpUrl
                .parse(CUSTOMER_GET_LOANS_OPTIONS_TO_INVEST)
                .newBuilder()
                .build()
                .toString();
        InvestmentLoanInformationDTO investmentLoanInformationDTO = getInvestmentParams();
        String investmentParamsInJson = GSON_INSTANCE.toJson(investmentLoanInformationDTO);

        HttpClientUtil.runAsyncJson(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Scramble error", "Could not load loan options to invest", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Scramble error", "Could not load loan options to invest", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            LoanListDTO loanListDTO = GSON_INSTANCE.fromJson(rawBody, LoanListDTO.class);
                            loadLoanAndShowInformation(loanListDTO.getLoanList(), statusInfoScrollPane);
                            statusInfoScrollPane.setVisible(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }, investmentParamsInJson);
    }

    private InvestmentLoanInformationDTO getInvestmentParams() {
        InvestmentLoanInformationDTO investmentLoanInformationDTO = new InvestmentLoanInformationDTO();

        investmentLoanInformationDTO.setInterest(minInterest);
        investmentLoanInformationDTO.setMaxOpenLoans(maxOpenLoans);
        investmentLoanInformationDTO.setAmountOfMoneyToInvest(moneyToInvest);
        investmentLoanInformationDTO.setMinimumTotalTimeunits(minTotalYaz);
        investmentLoanInformationDTO.setChosenCategories(selectedCategories);
        investmentLoanInformationDTO.setCustomerOfInvestmentName(customerName);
        investmentLoanInformationDTO.setMaxOwnershipPercentage(maxOwnershipPercentage);

        return investmentLoanInformationDTO;
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
        scrambleSetVisibility(false);
        sellLoanButton.setDisable(true);

        paymentTab.setOnSelectionChanged(event -> {
            payPaymentButton.setDisable(true);
            paymentCloseLoanButton.setDisable(true);
        });

        newLoanInterestSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            newLoanInterestRangeLabel.setText(String.valueOf(newValue.intValue()));
        });

        scrambleInterestSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            scrambleInterestRangeLabel.setText(String.valueOf(newValue.intValue()));
        });

        scrambleOwnershipPercentageSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            scrambleOwnershipPercentageRangeLabel.setText(String.valueOf(newValue.intValue()));
        });

        scrambleLoansOptionsLabel.setVisible(false);
    }

    private void checkMoneyToInvest(String moneyToInvestTF) throws Exception {
        if(moneyToInvestTF.equals("")) {
            throw new MoneyToInvestException("Required Field");
        }
        else {
            try {
                moneyToInvest = Integer.parseInt(moneyToInvestTF);

                if (moneyToInvest <= 0) {
                    throw new MoneyToInvestException("Invalid Input, Please enter an integer bigger than 0");
                }

                if (moneyToInvest > customerBalance) {
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

        mainController.showLoanInfo(loanOptions, false, false, statusInfoScrollPane, "");
        loansOptionsScrollPane.setContent(loanOptionsTable);

        chosenLoans = loanOptionsTable.getSelectionModel().getSelectedItems();
        loanOptionsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        scrambleSetVisibility(true);
    }

    private void scrambleSetVisibility(boolean visibility) {
        scrambleLoansOptionsLabel.setVisible(visibility);
        loansOptionsScrollPane.setVisible(visibility);
        statusInfoScrollPane.setVisible(visibility);
        scrambleChooseLoansToInvestButton.setVisible(visibility);
    }
/*
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

    */

    public void setPayPaymentAndCloseLoanDisableByPaymentNotification(LoanInformationDTO currentLoan) {
        String finalUrl = HttpUrl
                .parse(CUSTOMER_PAYMENT_NOTIFICATIONS)
                .newBuilder()
                .addQueryParameter("username", customerName)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Payment Notifications error", "Could not load information", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Payment Notifications error", "Could not load information", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            PaymentNotificationsListDTO paymentNotificationsListDTO = GSON_INSTANCE.fromJson(rawBody, PaymentNotificationsListDTO.class);
                            if (!(currentLoan.getLoanStatus().equals("NEW")) && isNewPaymentNotificationExist(paymentNotificationsListDTO.getPaymentsNotificationsDTOList(), customerName, currentLoan.getLoanNameID())) {
                                payPaymentButton.setDisable(false);
                                paymentCloseLoanButton.setDisable(false);
                            } else {
                                payPaymentButton.setDisable(true);
                                paymentCloseLoanButton.setDisable(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private boolean isNewPaymentNotificationExist(List<PaymentsNotificationsDTO> paymentsNotificationList, String customerName, String loanNameID) {
        for (PaymentsNotificationsDTO paymentNotification : paymentsNotificationList) {
            if (paymentNotification.getLoanID().equals(loanNameID) && paymentNotification.getNewNotification()) {
                return true;
            }
        }

        return false;
    }


    private void fillCustomerLoansTables(String customerName) {
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
                                loadCustomerTablesFromFXML(lenderLoansScrollPane, "Lender Loans");
                                mainController.showLoanInfo(relevantCustomer.getClientAsLenderLoanList(), false, false, bottomInfoScrollPane, "Customer Lender Loans");
                                loadCustomerTablesFromFXML(loanerLoansScrollPane, "Information Loaner Loans");
                                mainController.showLoanInfo(relevantCustomer.getClientAsBorrowerLoanList(), false, false, topInfoScrollPane, "Customer Loaner Loans");
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

    private void loadCustomerTablesFromFXML(ScrollPane scrollPaneToLoadTo, String relevantTableView) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/mainApp/admin/loanInfo.fxml");
        fxmlLoader.setLocation(url);
        TableView loansTable = fxmlLoader.load(url.openStream());
        LoanInfoController loanInfoController = fxmlLoader.getController();
        mainController.setLoanInfoController(loanInfoController);

        scrollPaneToLoadTo.setContent(loansTable);
        setTableView(relevantTableView, loansTable);
    }

    private void setTableView(String relevantTableView, TableView loansTable) {
        switch (relevantTableView) {
            case "Information Loaner Loans":
                loanerLoansTableView = loansTable;
                break;
            case "Lender Loans":
                lenderLoansTableView = loansTable;
                break;
            case "Payment Loaner Loans":
                paymentLoanerLoansTableView = loansTable;
                break;
            case "Loans for sale":
                loansForSaleTableView = loansTable;
        }
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

    private void fillPaymentLoanerLoansTable() {
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
                            loadCustomerTablesFromFXML(paymentLoanerLoansScrollPane, "Payment Loaner Loans");
                            mainController.showLoanInfo(customerOpenLoansToPayList, false, false, paymentInfoScrollPane, "Customer Payment Loaner Loans");
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
            if (!(scrambleCategoriesListView.getItems().contains(category))) {
                scrambleCategoriesListView.getItems().add(category);
            }
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

    public void afterCustomerLoginLoading(String userName) {
        makeLoanTablesVisible();
        setCustomerNameInCustomerController(userName);
        setCustomerDTOInCustomerController(userName);
        fillCustomerLoansTables(userName);
        fillPaymentLoanerLoansTable();
        loadAndSetNotificationAreaTable(userName);
        fillAccountTransactionTableAndUpdateBalanceLabel();
        fillLoansForSaleTable(userName);
        loadAndFillCategoriesOnScrambleTab();
        disablePayPaymentAndCloseLoan();
    }

    private void fillLoansForSaleTable(String userName) {
        String finalUrl = HttpUrl
                .parse(CUSTOMER_LOAN_FOR_SALE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Customer Loans for sale error", "Could not load information", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Customer Loans for sale error", "Could not load information", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            LoanForSaleListDTO loanForSaleListDTO = GSON_INSTANCE.fromJson(rawBody, LoanForSaleListDTO.class);
                            List<LoanInformationDTO> loanForSaleForTableView = loanForSaleListDTO.getLoanForSaleListInformationDTO();
                            loadCustomerTablesFromFXML(loanForSaleScrollPane, "Loans for sale");
                            mainController.showLoanInfo(loanForSaleForTableView, false, false, paymentInfoScrollPane, "Customer Payment Loaner Loans");
                            buyLoanButton.setDisable(true);
                            // todo: take care of the amount label
                            //todo: check payment scrollpane
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }


    private void disablePayPaymentAndCloseLoan() {
        payPaymentButton.setDisable(true);
        paymentCloseLoanButton.setDisable(true);
    }

    private void loadAndFillCategoriesOnScrambleTab() {
        String finalUrl = HttpUrl
                .parse(CUSTOMER_CATEGORIES_LIST)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Categories error", "Could not load information", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Categories error", "Could not load information", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            CategoriesList categoriesList = GSON_INSTANCE.fromJson(rawBody, CategoriesList.class);
                            fillCategoriesOnScrambleTab(categoriesList.getCategories());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void loadAndSetNotificationAreaTable(String userName) {
        String finalUrl = HttpUrl
                .parse(CUSTOMER_PAYMENT_NOTIFICATIONS)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Payment Notifications error", "Could not load information", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Payment Notifications error", "Could not load information", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            PaymentNotificationsListDTO paymentNotificationsListDTO = GSON_INSTANCE.fromJson(rawBody, PaymentNotificationsListDTO.class);
                            setNotificationAreaTable(paymentNotificationsListDTO.getPaymentsNotificationsDTOList());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
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

    public void setSellLoanButtonAble(LoanInformationDTO selectedItem) {
        if (lenderLoansTableView.getItems().contains(selectedItem)) {
            sellLoanButton.setDisable(false);
        }
    }

    public void fillLoanPriceLoansForSale(LoanInformationDTO selectedItem) {
        String finalUrl = HttpUrl
                .parse(CUSTOMER_LOAN_FOR_SALE)
                .newBuilder()
                .addQueryParameter("username", customerName)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        alertPopUp("Customer Loans for sale error", "Could not load information", e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            alertPopUp("Customer Loans for sale error", "Could not load information", responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            LoanForSaleListDTO loanForSaleListDTO = GSON_INSTANCE.fromJson(rawBody, LoanForSaleListDTO.class);
                            List<LoanForSaleDTO> loanForSaleToCheck = loanForSaleListDTO.getLoanForSaleDTOList();
                            intoLoanPriceLabel(loanForSaleToCheck, selectedItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void intoLoanPriceLabel(List<LoanForSaleDTO> loanForSaleToCheck, LoanInformationDTO selectedItem) {
        for (LoanForSaleDTO loan : loanForSaleToCheck) {
            if (selectedItem.getLoanNumber() == loan.getLoanNumber()) {
                loanPriceLabel.setText("Loan Price: " + loan.getLoanPrice());
            }
        }
    }

    public void setBuyLoanButtonAble(LoanInformationDTO selectedItem) {
        if (loansForSaleTableView.getItems().contains(selectedItem) && !(selectedItem.getBorrowerName().equals(customerName))) {
            buyLoanButton.setDisable(false);
        }
    }
}