package mainApp.login;


import client.util.Constants;
import client.util.http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import mainApp.AppController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LoginController {

    @FXML
    protected GridPane loginComponent;

    @FXML
    private Button quitButton;

    @FXML
    private Button loginButton;

    @FXML
    private TextField userNameTextField;

    @FXML
    private Label errorMessageLabel;

    private AppController appController;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        HttpClientUtil.setCookieManagerLoggingFacility(line ->
                Platform.runLater(() ->
                        updateHttpStatusLine(line)));
    }

    @FXML
    private void loginButtonClicked(ActionEvent event) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                        .parse(Constants.CUSTOMER_LOGIN_PAGE)
                        .newBuilder()
                        .addQueryParameter("username", userName)
                        .build()
                        .toString();

        updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            appController.afterCustomerLogin(userName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                response.close();
            }
        });
    }

    @FXML
    private void userNameKeyTyped(KeyEvent event) {
        errorMessageProperty.set("");
    }

    @FXML
    private void quitButtonClicked(ActionEvent e) {
        Platform.exit();
    }

    private void updateHttpStatusLine(String data) {
        //chatAppMainController.updateHttpLine(data);
    }

    public void setMainController(AppController appController) {
        this.appController = appController;
    }

    /*public void setChatAppMainController(ChatAppMainController chatAppMainController) {
        this.chatAppMainController = chatAppMainController;
    }*/
}
