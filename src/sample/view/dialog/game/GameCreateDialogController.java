package sample.view.dialog.game;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.*;
import sample.repository.Datenbank;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GameCreateDialogController {
    private Stage stage;

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField dateTextField;
    @FXML
    private TextField preisTextField;
    @FXML
    private TextField imageTextField;
    @FXML
    private TextField linkTextField;
    @FXML
    private TextField beschreibungTextField;
    @FXML
    private ComboBox<Publisher> publisherComboBox;
    @FXML
    private ComboBox<Kategorie> kategorieComboBox;
    @FXML
    private void initialize() {
        List<Publisher> publishers = Objects.requireNonNull(Datenbank.publisherRepo.getAllPublishers().stream().findFirst().orElse(null));
        publisherComboBox.getItems().addAll(publishers);
        publisherComboBox.converterProperty();
        publisherComboBox.getSelectionModel().select(0);

        List<Kategorie> kategories = Objects.requireNonNull(Datenbank.kategorieRepo.getAllKategories().stream().findFirst().orElse(null));
        kategorieComboBox.getItems().addAll(kategories);
        kategorieComboBox.converterProperty();
        kategorieComboBox.getSelectionModel().select(0);
    }

    @FXML
    private void handleEditButton() {
        if(!nameTextField.getText().isEmpty() &&
                !dateTextField.getText().isEmpty() &&
                !preisTextField.getText().isEmpty() &&
                !imageTextField.getText().isEmpty() &&
                !linkTextField.getText().isEmpty() &&
                !beschreibungTextField.getText().isEmpty() &&
                publisherComboBox.getSelectionModel().getSelectedItem() != null &&
                kategorieComboBox.getSelectionModel().getSelectedItem() != null ){
            try {
                Datenbank.spielRepo.addSpiel(nameTextField.getText(),
                        beschreibungTextField.getText(),dateTextField.getText(),
                        publisherComboBox.getSelectionModel().getSelectedItem().getPubNr(),
                        kategorieComboBox.getSelectionModel().getSelectedItem().getKatNr(),
                        Double.parseDouble(preisTextField.getText()),imageTextField.getText(),
                        linkTextField.getText());
                alert("Spiel erstellt!");
                stage.close();
            } catch (SQLException e){
                alert(e.getMessage());
            }
        }else {
            alert("Spiel kann nicht leer sein! Bitte, Fügen Sie alle an");
        }
    }

    @FXML
    private void handleCancelButton() {
        stage.close();
    }

    @FXML
    private void handleChooseImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("D:/DE-Study/Sommersemester(2021)/DB2/Project/GameStore_DB2/src/resources/images/games"));
        File image = fileChooser.showOpenDialog(null);

        if (image != null){
            imageTextField.setText(image.getName());
        }else {
            alert("Image wird nicht hochgeladen");
        }
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
