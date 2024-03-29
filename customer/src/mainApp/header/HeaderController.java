package mainApp.header;
import DTO.client.ClientInformationDTO;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import mainApp.AppController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {
    private AppController mainController;
    private Parent adminView;
    private GridPane header;
    private ScrollPane customerView;
    private int currentYaz;

    @FXML
    private GridPane headerComponent;

    @FXML
    private Label filePathLabel;

    @FXML
    private Label currentYazLabel;

    @FXML
    private Label userNameLabel;

    private Parent loadScene(String sc) throws IOException {
        return FXMLLoader.load(getClass().getResource(sc));
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentYaz = 1;
    }

    public void updateLabels(int currentTimeUnit, String filePath) {
        currentYazLabel.setText("Current Yaz: " + currentTimeUnit);
        filePathLabel.setText("  File Path: " + filePath);
    }

    public void promoteIncreaseYazLabel(int currentYaz) {
        currentYazLabel.setText("Current Yaz: " + currentYaz);
    }

    public void setCustomerViewParameter(ScrollPane customerView) {
       this.customerView = customerView;
    }

    public void setHeaderComponent(GridPane headerComponent) {
        header = headerComponent;
    }

    public void updateUsernameLabel(String userName) {
        userNameLabel.setText("Hello " + userName);
    }

    public void updateFilePath(String chosenFile) {
        filePathLabel.setText("  File Path: " + chosenFile);
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
                            String s = response.body().string();
                            currentYaz = Integer.parseInt(s.trim());
                            currentYazLabel.setText("Current Yaz: " + currentYaz);
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

    public int getCurrentYaz() {
        return currentYaz;
    }

    public void setCurrentYaz(int newCurrentYaz) {
        currentYaz = newCurrentYaz;
    }
}
