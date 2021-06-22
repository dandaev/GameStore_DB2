package sample.view.dialog.genre;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Genre;
import sample.repository.Datenbank;

import java.sql.SQLException;

public class GenreEditDialogController {
    private Stage stage;
    private Genre editedGenre;

    @FXML
    private TextField genreNameTextField;

    @FXML
    private void initialize(){
    }

    @FXML
    private void handleEditButton(){
        if(!genreNameTextField.getText().isEmpty()){
            try {
                Datenbank.genreRepo.updateGenre(editedGenre.getGeNr(),genreNameTextField.getText());
                alert("Genre Name bearbeited!");
                stage.close();
            } catch (SQLException e){
                alert(e.getMessage());
            }
        }else {
            alert("Genre Name kann nicht leer sein! Bitte, Fügen Sie Name an");
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

    public void setEditedGenre(Genre editedGenre){
        this.editedGenre = editedGenre;
        genreNameTextField.setText(editedGenre.getGeName());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
