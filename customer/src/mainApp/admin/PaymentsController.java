package mainApp.admin;
import DTO.loan.PaymentsDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PaymentsController {
    @FXML private AdminController mainController;

    @FXML
    private TableView<PaymentsDTO> paymentsTableView;

    @FXML
    private TableColumn<PaymentsDTO, Integer> paymentNumberCol;

    @FXML
    private TableColumn<PaymentsDTO, Integer> paymentYazCol;

    @FXML
    private TableColumn<PaymentsDTO, Integer> fundAmountCol;

    @FXML
    private TableColumn<PaymentsDTO, Integer> interestAmountCol;

    @FXML
    private TableColumn<PaymentsDTO, Integer> sumCol;

    @FXML
    private TableColumn<PaymentsDTO, Boolean> paidCol;

    public void setMainController(AdminController adminController) {
        this.mainController = adminController;
    }

    public void showPaymentsTable(List<PaymentsDTO> paymentsList, String loanStatus) {
        paymentNumberCol.setCellValueFactory(new PropertyValueFactory<PaymentsDTO, Integer>("paymentNumber"));
        paymentYazCol.setCellValueFactory(new PropertyValueFactory<PaymentsDTO, Integer>("paymentNumber"));
        fundAmountCol.setCellValueFactory(new PropertyValueFactory<PaymentsDTO, Integer>("fundPayment"));
        interestAmountCol.setCellValueFactory(new PropertyValueFactory<PaymentsDTO, Integer>("interestPayment"));
        sumCol.setCellValueFactory(new PropertyValueFactory<PaymentsDTO, Integer>("paymentSum"));

        // if loan is in risk:
        if (loanStatus.equals("RISK")) {
            paidCol.setCellValueFactory(new PropertyValueFactory<PaymentsDTO, Boolean>("wasItPaid"));
        }
        else {
            paidCol.setVisible(false);
        }
        paymentsTableView.setItems(FXCollections.observableList(paymentsList));
    }
}
