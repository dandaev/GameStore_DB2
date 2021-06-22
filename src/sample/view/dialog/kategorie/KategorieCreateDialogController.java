package sample.view.dialog.kategorie;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Kategorie;
import sample.repository.Datenbank;

import java.sql.SQLException;

public class KategorieCreateDialogController {
    private Stage stage;

    @FXML
    private TextField kategorieNameTextField;

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleEditButton() {
        if (!kategorieNameTextField.getText().isEmpty()) {
            try {
                Datenbank.kategorieRepo.addKategorie(kategorieNameTextField.getText());
                alert("Kategorie erstellt!");
                stage.close();
            } catch (SQLException e) {
                alert(e.getMessage());
            }
        } else {
            alert("Kategorie Name kann nicht leer sein! Bitte, Fügen Sie Name an");
        }
    }

    @FXML
    private void handleCancelButton() {
        stage.close();
    }

    private void alert(String text) {
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                text,
                ButtonType.CLOSE
        );
        alert.showAndWait();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
