package mainApp.header;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import mainApp.AdminAppController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {
    private AdminAppController mainController;
    private Parent adminView;
    private GridPane header;
    private ScrollPane customerView;
    private int currentYaz;
    private int counterForYazComboBox = 2;

    @FXML
    private GridPane headerComponent;

    @FXML
    private Label filePathLabel;

    @FXML
    private Label currentYazLabel;

   /* @FXML
    private ComboBox<String> skinComboBox;*/

    @FXML
    private Label userNameLabel;

    @FXML
    private ComboBox<String> yazComboBox;

    @FXML
    void yazComboBoxChanged(ActionEvent event) {
        int yazChosen = Integer.parseInt(yazComboBox.getSelectionModel().getSelectedItem());
        currentYazLabel.setText("Current Yaz: " + yazChosen);
        mainController.updateRefresherYaz(yazChosen);
    }

    public void setMainController(AdminAppController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yazComboBox.getItems().addAll("1");
        yazComboBox.getSelectionModel().select("1");;
    }

    public void updateLabels(int currentTimeUnit, String filePath) {
        currentYazLabel.setText("Current Yaz: " + currentTimeUnit);
        filePathLabel.setText("  File Path: " + filePath);
    }

    public void addYazToComboBox() {
        yazComboBox.getItems().add(String.valueOf(counterForYazComboBox));
        yazComboBox.getSelectionModel().select(String.valueOf(counterForYazComboBox));
        counterForYazComboBox++;
    }

    public void setCustomerViewParameter(ScrollPane customerView) {
       this.customerView = customerView;
    }

    public void setHeaderComponent(GridPane headerComponent) {
        header = headerComponent;
    }

    public void setHeaderStyleSheet(String value) {
        switch (value) {
            case "Default":
                header.getStylesheets().clear();
                break;
            case "Skin1":
                header.getStylesheets().clear();
                header.getStylesheets().add(getClass().getResource("headerCSS.css").toExternalForm());
                break;
            case "Skin2":
                header.getStylesheets().clear();
                header.getStylesheets().add(getClass().getResource("headerCSS2.css").toExternalForm());
                break;
        }
    }

    public void updateUsernameLabel(String userName) {
        userNameLabel.setText("Hello " + userName);

    }

    public void updateCurrentYaz() {
        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_CURRENT_YAZ)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Current Yaz Error");
                            alert.setHeaderText("Could not load current yaz");
                            alert.setContentText(e.getMessage());

                            alert.showAndWait();
                        }
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Current Yaz Error");
                        alert.setHeaderText("Could not load current yaz");
                        alert.setContentText(responseBody);

                        alert.showAndWait();
                    });
                } else {
                    Platform.runLater(() -> {
                        try {
                            currentYazLabel.setText("Current Yaz: " + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    public void updateCurrentYazByNumber(String currentYaz) {
        currentYazLabel.setText("Current Yaz: " + String.valueOf(currentYaz));
    }

    public int getSavedYaz() {
        return currentYaz;
    }

    public void setSavedYaz(int newYaz) {
        currentYaz = newYaz;
    }
}
