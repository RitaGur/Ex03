package mainApp.admin.loan.active;

import DTO.loan.LoanInformationDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import mainApp.AdminAppController;
import mainApp.admin.adminController.AdminController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ActiveInfoController implements Initializable{
    private AdminAppController mainController;
    private LoanInformationDTO currentLoan;
    private AdminController adminController;
    private GridPane activeInfoComponent;

    @FXML
    private Button activeLendersButton;

    @FXML
    private ScrollPane infoScrollPane;

    @FXML
    private Label activeBeginningYazLabel;

    @FXML
    private Label activeNextPaymentYazLabel;

    @FXML
    private Label activePaidFundLabel;

    @FXML
    private Label activePaidInterestLabel;

    @FXML
    private Label activeFundLeftToPayLabel;

    @FXML
    private Label activeInterestLeftToPayLabel;

    @FXML
    private Button paymentsButton;

    @FXML
    void LendersClicked(ActionEvent event) throws IOException {
        mainController.showLendersTable(currentLoan);
    }

    @FXML
    void paymentsClicked(ActionEvent event) throws IOException {
        mainController.showPaymentsTable(currentLoan);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMainController(AdminAppController adminAppController) {
        this.mainController = adminAppController;
    }

    public void showActiveInfo(LoanInformationDTO currentLoan) {
        this.currentLoan = currentLoan;
        activeBeginningYazLabel.setText("Beginning Yaz: " + currentLoan.getBeginningTimeUnit());
        activeNextPaymentYazLabel.setText("Next Payment Yaz: " + currentLoan.getNextPaymentTimeUnit());
        activePaidFundLabel.setText("Paid Fund: " + currentLoan.getPaidFund());
        activePaidInterestLabel.setText("Paid Interest: " + currentLoan.getPaidInterest());
        activeFundLeftToPayLabel.setText("Fund Left to Pay: " + currentLoan.getFundLeftToPay());
        activeInterestLeftToPayLabel.setText("Interest Left to Pay: " + currentLoan.getInterestLeftToPay());
    }

    public void setPaymentsTable(TableView payments) {
        infoScrollPane.setContent(payments);
    }

    public void setLendersTable(TableView lenders) {
        infoScrollPane.setContent(lenders);
    }

    public void setActiveInfoComponent(GridPane activeInfo) {
        this.activeInfoComponent = activeInfo;
    }

    public void setActiveInfoStyleSheet(String value) {
        switch (value) {
            case "Default":
                activeInfoComponent.getStylesheets().clear();
                break;
            case "Skin1":
                activeInfoComponent.getStylesheets().clear();
                activeInfoComponent.getStylesheets().add(getClass().getResource("ActiveCSS.css").toExternalForm());
                break;
            case "Skin2":
                activeInfoComponent.getStylesheets().clear();
                activeInfoComponent.getStylesheets().add(getClass().getResource("ActiveCSS2.css").toExternalForm());
                break;
        }
    }
}
