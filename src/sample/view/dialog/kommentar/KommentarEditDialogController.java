package sample.view.dialog.kommentar;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Kommentar;
import sample.repository.Datenbank;

import java.sql.SQLException;

public class KommentarEditDialogController {
    private Stage stage;
    private Kommentar editedKommentar;

    @FXML
    private TextField kommentarTextField;

    @FXML
    private void initialize(){
    }

    @FXML
    private void handleEditKommentarButton(){
        if(!kommentarTextField.getText().isEmpty()){
            try {
                Datenbank.kommentarRepo.updateKommentar(kommentarTextField.getText(),editedKommentar.getKommNr());
                alert("Ihre Kommentar bearbeited!");
                stage.close();
            } catch (SQLException e){
                alert(e.getMessage());
            }
        }else {
            alert("Kommentar kann nicht leer sein! Bitte, Fügen Sie Kommentar an");
        }
    }

    @FXML
    private void handleCancelButton(){
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

    public void setEditedKommentar(Kommentar editedKommentar){
        this.editedKommentar = editedKommentar;
        kommentarTextField.setText(editedKommentar.getKommText());
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
