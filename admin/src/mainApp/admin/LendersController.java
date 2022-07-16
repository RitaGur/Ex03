package mainApp.admin;

import DTO.loan.PartInLoanDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import mainApp.admin.adminController.AdminController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LendersController implements Initializable {
    @FXML
    private AdminController mainController;
    @FXML
    private TableView<PartInLoanDTO> lendersTableView;

    @FXML
    private TableColumn<PartInLoanDTO, Integer> lenderNumberCol;

    @FXML
    private TableColumn<PartInLoanDTO, String > lenderNameCol;

    @FXML
    private TableColumn<PartInLoanDTO, Integer> lenderAmountInLoanCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMainController(AdminController mainController) {
        this.mainController = mainController;
    }

    public void showLendersTable(List<PartInLoanDTO> lendersList) {
        lenderNumberCol.setCellValueFactory(new PropertyValueFactory<PartInLoanDTO, Integer>("lenderNumber"));
        lenderNameCol.setCellValueFactory(new PropertyValueFactory<PartInLoanDTO, String>("lenderName"));
        lenderAmountInLoanCol.setCellValueFactory(new PropertyValueFactory<PartInLoanDTO, Integer>("amountOfLoan"));

        lendersTableView.setItems(FXCollections.observableList(lendersList));
    }
}
