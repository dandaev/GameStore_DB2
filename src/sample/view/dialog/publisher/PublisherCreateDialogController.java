package sample.view.dialog.publisher;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Publisher;
import sample.repository.Datenbank;

import java.sql.SQLException;

public class PublisherCreateDialogController {
    private Stage stage;

    @FXML
    private TextField publisherNameTextField;

    @FXML
    private void initialize(){
    }

    @FXML
    private void handleEditKommentarButton(){
        if(!publisherNameTextField.getText().isEmpty()){
            try {
                Datenbank.publisherRepo.addPublisher(publisherNameTextField.getText());
                alert("Publisher erstellt!");
                stage.close();
            } catch (SQLException e){
                alert(e.getMessage());
            }
        }else {
            alert("Publisher Name kann nicht leer sein! Bitte, Fügen Sie Name an");
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
