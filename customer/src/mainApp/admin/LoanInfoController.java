package mainApp.admin;
import DTO.loan.LoanInformationDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import mainApp.AppController;
import mainApp.admin.loan.active.ActiveInfoController;
import mainApp.admin.loan.finished.FinishedInfoController;
import mainApp.admin.loan.pending.PendingInfoController;
import mainApp.admin.loan.risk.RiskInfoController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoanInfoController implements Initializable {
    private AppController mainController;
    private ScrollPane infoScrollPane;
    private ObservableList<LoanInformationDTO> customerLenderList;
    private ObservableList<LoanInformationDTO> customerLoanerList;
    private ObservableList<LoanInformationDTO> customerPaymentLoanerLoansList;

    @FXML
    private TableView<LoanInformationDTO> loanTableView;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> numberCol;

    @FXML
    private TableColumn<LoanInformationDTO, String> IDCol;

    @FXML
    private TableColumn<LoanInformationDTO, String> OwnerCol;

    @FXML
    private TableColumn<LoanInformationDTO, String> categoryCol;

    @FXML
    private TableColumn<LoanInformationDTO, String> statusCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> fundCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> sumCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> totalYazCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> InterestCol;

    @FXML
    private TableColumn<LoanInformationDTO, Integer> yazBetweenPaymentCol;

    public void setTableColumns(List<LoanInformationDTO> listToShow, Boolean isOwnerVisible, Boolean isNumberLoanVisible, ScrollPane infoScrollPane, String toObservableList) {
        this.infoScrollPane = infoScrollPane;
        infoScrollPane.setVisible(false);
        infoScrollPane.setContent(null);
        numberCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, Integer>("loanNumber"));
        IDCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, String>("loanNameID"));
        OwnerCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, String>("borrowerName"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, String>("loanCategory"));
        statusCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, String>("loanStatus"));
        fundCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, Integer>("loanStartSum"));
        InterestCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, Integer>("loanInterest"));
        totalYazCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, Integer>("loanSumOfTimeUnit"));
        yazBetweenPaymentCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, Integer>("timeUnitsBetweenPayments"));
        sumCol.setCellValueFactory(new PropertyValueFactory<LoanInformationDTO, Integer>("sumAmount"));

        numberCol.setVisible(isNumberLoanVisible);
        OwnerCol.setVisible(isOwnerVisible);

        loanTableView.setItems(FXCollections.observableArrayList(listToShow));
        chooseWhichObservableList(toObservableList, listToShow);
    }

    private void chooseWhichObservableList(String toObservableList, List<LoanInformationDTO> listToShow) {
        switch (toObservableList) {
            case "Customer Lender Loans":
                customerLenderList = FXCollections.observableList(listToShow);
                break;
            case "Customer Loaner Loans":
                customerLoanerList = FXCollections.observableList(listToShow);
                break;
            case "Customer Payment Loaner Loans":
                customerPaymentLoanerLoansList = FXCollections.observableList(listToShow);
                break;
            default:
                break;
        }
    }

    public void setCustomerLenderList(ObservableList<LoanInformationDTO> customerLenderList) {
        this.customerLenderList = customerLenderList;
    }

    public void setCustomerLoanerList(ObservableList<LoanInformationDTO> customerLoanerList) {
        this.customerLoanerList = customerLoanerList;
    }

    public void setCustomerPaymentLoanerLoansList(ObservableList<LoanInformationDTO> customerPaymentLoanerLoansList) {
        this.customerPaymentLoanerLoansList = customerPaymentLoanerLoansList;
    }

    public ObservableList<LoanInformationDTO> getCustomerLenderList() {
        return customerLenderList;
    }

    public ObservableList<LoanInformationDTO> getCustomerLoanerList() {
        return customerLoanerList;
    }

    public ObservableList<LoanInformationDTO> getCustomerPaymentLoanerLoansList() {
        return customerPaymentLoanerLoansList;
    }

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loanTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && (oldValue == null || !(newValue.getLoanNameID().equals(oldValue.getLoanNameID())))) {
                try {
                    LoanInformationDTO currentLoan = loanTableView.getSelectionModel().selectedItemProperty().get();
                    insertByStatus(currentLoan, infoScrollPane);
                    mainController.setPayPaymentAndCloseLoanDisableByPaymentNotification(currentLoan);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void insertByStatus(LoanInformationDTO currentLoan, ScrollPane infoScrollPane) throws IOException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                infoScrollPane.setVisible(false);
                switch (currentLoan.getLoanStatus().toString()) {
                    case "PENDING": {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        URL url = getClass().getResource("/mainApp/admin/loan/pending/pendingStatusInfo.fxml");
                        fxmlLoader.setLocation(url);
                        try {
                            GridPane pendingInfo = fxmlLoader.load(url.openStream());
                            PendingInfoController pendingInfoController = fxmlLoader.getController();
                            mainController.setPendingInfoController(pendingInfoController);
                            mainController.setPendingInfoComponent(pendingInfo);
                            mainController.setPendingInfoStyleSheet();
                            mainController.showPendingInfo(currentLoan);
                            infoScrollPane.setVisible(true);
                            infoScrollPane.setContent(pendingInfo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "ACTIVE": {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        URL url = getClass().getResource("/mainApp/admin/loan/active/activeStatusInfo.fxml");
                        fxmlLoader.setLocation(url);
                        try {
                            GridPane activeInfo = fxmlLoader.load(url.openStream());
                            ActiveInfoController activeInfoController = fxmlLoader.getController();
                            mainController.setActiveInfoController(activeInfoController);
                            mainController.setActiveInfoComponent(activeInfo);
                            mainController.setActiveInfoStyleSheet();
                            mainController.showActiveInfo(currentLoan);
                            infoScrollPane.setVisible(true);
                            infoScrollPane.setContent(activeInfo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "RISK": {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        URL url = getClass().getResource("/mainApp/admin/loan/risk/riskStatusInfo.fxml");
                        fxmlLoader.setLocation(url);
                        try {
                            GridPane riskInfo = fxmlLoader.load(url.openStream());
                            RiskInfoController riskInfoController = fxmlLoader.getController();
                            mainController.setRiskInfoController(riskInfoController);
                            mainController.setRiskInfoComponent(riskInfo);
                            mainController.setRiskInfoStyleSheet();
                            mainController.showRiskInfo(currentLoan);
                            infoScrollPane.setVisible(true);
                            infoScrollPane.setContent(riskInfo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "FINISHED": {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        URL url = getClass().getResource("/mainApp/admin/loan/finished/finishedStatusInfo.fxml");
                        fxmlLoader.setLocation(url);
                        try {
                            GridPane finishedInfo = fxmlLoader.load(url.openStream());
                            FinishedInfoController finishedInfoController = fxmlLoader.getController();
                            mainController.setFinishedInfoController(finishedInfoController);
                            mainController.setFinishedInfoComponent(finishedInfo);
                            mainController.setFinishedInfoStyleSheet();
                            mainController.showFinishedInfo(currentLoan);
                            infoScrollPane.setVisible(true);
                            infoScrollPane.setContent(finishedInfo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "NEW": {

                    }
                }
            }
        });
    }

}
