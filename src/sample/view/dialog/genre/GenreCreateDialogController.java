package sample.view.dialog.genre;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Genre;
import sample.repository.Datenbank;

import java.sql.SQLException;

public class GenreCreateDialogController {
    private Stage stage;

    @FXML
    private TextField genreNameTextField;

    @FXML
    private void initialize(){
    }

    @FXML
    private void handleEditButton(){
        if(!genreNameTextField.getText().isEmpty()){
            try {
                Datenbank.genreRepo.addGenre(genreNameTextField.getText());
                alert("Genre erstellt!");
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
