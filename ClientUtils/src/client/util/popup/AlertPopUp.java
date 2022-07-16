package client.util.popup;

import javafx.scene.control.Alert;

public class AlertPopUp {
    public static void alertPopUp(String setTitle, String headerText, String ContentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(setTitle);
        alert.setHeaderText(headerText);
        alert.setContentText(ContentText);

        alert.showAndWait();
    }
}
