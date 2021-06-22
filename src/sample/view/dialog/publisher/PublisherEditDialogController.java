package sample.view.dialog.publisher;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Publisher;
import sample.repository.Datenbank;

import java.sql.SQLException;

public class PublisherEditDialogController {
    private Stage stage;
    private Publisher editedPublisher;

    @FXML
    private TextField publisherNameTextField;

    @FXML
    private void initialize(){
    }

    @FXML
    private void handleEditKommentarButton(){
        if(!publisherNameTextField.getText().isEmpty()){
            try {
                Datenbank.publisherRepo.updatePublisher(editedPublisher.getPubNr(),publisherNameTextField.getText());
                alert("Publisher Name bearbeited!");
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

    public void setEditedPublisher(Publisher editedPublisher){
        this.editedPublisher = editedPublisher;
        publisherNameTextField.setText(editedPublisher.getPublisherName());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
