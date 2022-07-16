package mainApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mainApp.header.HeaderController;
import mainApp.login.LoginController;

import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Customer App");

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("login/login.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane loginComponent = fxmlLoader.load(url.openStream());
        LoginController loginController = fxmlLoader.getController();

        //load header component
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("header/header.fxml");
        fxmlLoader.setLocation(url);
        GridPane headerComponent = fxmlLoader.load(url.openStream());
        HeaderController headerController = fxmlLoader.getController();

        // load main app and controller from fxml
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("app.fxml");
        fxmlLoader.setLocation(url);
        BorderPane root = fxmlLoader.load(url.openStream());
        AppController appController = fxmlLoader.getController();

        root.setTop(headerComponent);
        root.setCenter(loginComponent);

        // connect between controllers
        appController.setLoginController(loginController);
        appController.setHeaderController(headerController);

        appController.setHeaderComponent(headerComponent);

        Scene scene = new Scene(root, 1300, 900);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
