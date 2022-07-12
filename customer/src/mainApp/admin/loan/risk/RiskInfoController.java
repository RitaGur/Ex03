package mainApp.admin.loan.risk;
import DTO.loan.LoanInformationDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import mainApp.AppController;

import java.io.IOException;

public class RiskInfoController {
    private AppController mainController;
    private LoanInformationDTO currentLoan;
    private GridPane riskInfoComponent;
    @FXML
    private Button riskLendersButton;

    @FXML
    private ScrollPane infoScrollPane;

    @FXML
    private Label riskBeginningYazLabel;

    @FXML
    private Label riskNextPaymentYazLabel;

    @FXML
    private Label riskPaidFundLabel;

    @FXML
    private Label riskPaidInterestLabel;

    @FXML
    private Label riskFundLeftToPayLabel;

    @FXML
    private Label riskInterestLeftToPayLabel;

    @FXML
    private Button riskPaymentsButton;

    @FXML
    private Label riskNumberOfUnpaidLabel;

    @FXML
    private Label riskSumOfUnpaidLabel;

    @FXML
    void LendersClicked(ActionEvent event) throws IOException {
        mainController.showLendersTable(currentLoan);
    }

    @FXML
    void paymentsClicked(ActionEvent event) throws IOException {
        mainController.showPaymentsTable(currentLoan);
    }

    public void setPaymentsTable(TableView payments) {
        infoScrollPane.setContent(payments);
    }

    public void setLendersTable(TableView lenders) {
        infoScrollPane.setContent(lenders);
    }

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public void showRiskInfo(LoanInformationDTO currentLoan) {
        this.currentLoan = currentLoan;
        riskBeginningYazLabel.setText("Beginning Yaz: " + currentLoan.getBeginningTimeUnit());
        riskNextPaymentYazLabel.setText("Next Payment Yaz: " + currentLoan.getNextPaymentTimeUnit());
        riskPaidFundLabel.setText("Paid Fund: " + currentLoan.getPaidFund());
        riskPaidInterestLabel.setText("Paid Interest: " + currentLoan.getPaidInterest());
        riskFundLeftToPayLabel.setText("Fund Left to Pay: " + currentLoan.getFundLeftToPay());
        riskInterestLeftToPayLabel.setText("Interest Left to Pay: " + currentLoan.getInterestLeftToPay());
        riskNumberOfUnpaidLabel.setText("Number of unpaid payments: " + currentLoan.getNumberOfUnpaidPayments());
        riskSumOfUnpaidLabel.setText("Sum of unpaid payments: " + currentLoan.getSumAmountToPayEveryTimeUnit() * currentLoan.getNumberOfUnpaidPayments());
    }

    public void setRiskInfoComponent(GridPane riskInfo) {
        this.riskInfoComponent = riskInfo;
    }

    public void setRiskInfoStyleSheet(String value) {
        switch (value) {
            case "Default":
                riskInfoComponent.getStylesheets().clear();
                break;
            case "Skin1":
                riskInfoComponent.getStylesheets().clear();
                riskInfoComponent.getStylesheets().add(getClass().getResource("RiskCSS.css").toExternalForm());
                break;
            case "Skin2":
                riskInfoComponent.getStylesheets().clear();
                riskInfoComponent.getStylesheets().add(getClass().getResource("RiskCSS2.css").toExternalForm());
                break;
        }
    }
}
