package mainApp.admin.loan.finished;

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

public class FinishedInfoController {
    private AppController mainController;
    private LoanInformationDTO currentLoan;
    private GridPane finishedInfoComponent;

    @FXML
    private Button finishedLendersButton;

    @FXML
    private ScrollPane infoScrollPane;

    @FXML
    private Label finishedBeginningYazLabel;

    @FXML
    private Label finishedEndingYazLabel;

    @FXML
    private Button finishedPaymentButton;

    @FXML
    void LendersClicked(ActionEvent event) throws IOException {
        mainController.showLendersTable(currentLoan);
    }

    @FXML
    void paymentsClicked(ActionEvent event) throws IOException {
        mainController.showPaymentsTable(currentLoan);
    }

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public void showFinishedInfo(LoanInformationDTO currentLoan) {
        this.currentLoan = currentLoan;
        finishedBeginningYazLabel.setText("Beginning Yaz: " + currentLoan.getBeginningTimeUnit());
        finishedEndingYazLabel.setText("Ending Yaz: " + currentLoan.getEndingTimeUnit());
    }

    public void setPaymentsTable(TableView payments) {
        infoScrollPane.setContent(payments);
    }

    public void setLendersTable(TableView lenders) {
        infoScrollPane.setContent(lenders);
    }

    public void setFinishedInfoComponent(GridPane finishedInfo) {
        this.finishedInfoComponent = finishedInfo;
    }

    public void setFinishedInfoStyleSheet(String value) {
        switch (value) {
            case "Default":
                finishedInfoComponent.getStylesheets().clear();
                break;
            case "Skin1":
                finishedInfoComponent.getStylesheets().clear();
                finishedInfoComponent.getStylesheets().add(getClass().getResource("FinishedCSS.css").toExternalForm());
                break;
            case "Skin2":
                finishedInfoComponent.getStylesheets().clear();
                finishedInfoComponent.getStylesheets().add(getClass().getResource("FinishedCSS2.css").toExternalForm());
                break;
        }
    }
}
