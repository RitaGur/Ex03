package mainApp.admin.loan.pending;

import DTO.loan.LoanInformationDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import mainApp.AppController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PendingInfoController implements Initializable {
    private AppController mainController;
    private LoanInformationDTO currentLoan;
    //private AdminController adminController;
    private GridPane pendingInfoComponent;

    @FXML
    private Label pendingRaisedAmountLabel;

    @FXML
    private Label pendingRemainingAmountLabel;

    @FXML
    private Button pendingLendersButton;

    @FXML
    private ScrollPane lendersScrollPane;

  /*  @FXML
    void LendersClicked(ActionEvent event) throws IOException {
        mainController.showLendersTable(currentLoan);
    }*/

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }


    public void showPendingInfo(LoanInformationDTO currentLoan) {
        this.currentLoan = currentLoan;
        pendingRaisedAmountLabel.setText("Raised Loan Amount: " + currentLoan.getPendingMoney());
        pendingRemainingAmountLabel.setText("Remaining Loan Amount: " + currentLoan.getMissingMoneyToActive());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setLendersTable(TableView lenders) {
        lendersScrollPane.setContent(lenders);
    }

    public void setPendingInfoComponent(GridPane pendingInfo) {
        this.pendingInfoComponent = pendingInfo;
    }

    public void setPendingInfoStyleSheet(String value) {
        switch (value) {
            case "Default":
                pendingInfoComponent.getStylesheets().clear();
                break;
            case "Skin1":
                pendingInfoComponent.getStylesheets().clear();
                pendingInfoComponent.getStylesheets().add(getClass().getResource("PendingCSS.css").toExternalForm());
                break;
            case "Skin2":
                pendingInfoComponent.getStylesheets().clear();
                pendingInfoComponent.getStylesheets().add(getClass().getResource("PendingCSS2.css").toExternalForm());
                break;
        }
    }
}
