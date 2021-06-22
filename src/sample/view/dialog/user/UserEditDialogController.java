package sample.view.dialog.user;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.model.Nutzer;
import sample.model.Role;
import sample.repository.Datenbank;

import java.sql.SQLException;
import java.util.Optional;

public class UserEditDialogController {
    private Nutzer editedNutzer;
    private Stage stage;

    public void setEditedUser(Nutzer editedNutzer){
        this.editedNutzer = editedNutzer;
        nameTextField.setText(editedNutzer.getNutzName());
        loginTextField.setText(editedNutzer.getNutzLogin());
        passwordTextField.setText(editedNutzer.getNutzPasswort());
        roleComboBox.getItems().addAll(Role.Reader, Role.ReadWriter, Role.Admin);
        roleComboBox.setValue(editedNutzer.getNutzRole());
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField loginTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private ComboBox<Role> roleComboBox;

    @FXML
    public void initialize(){
    }

    @FXML
    public void saveChangesButtonHandle(){
        if (!nameTextField.getText().equals(editedNutzer.getNutzName()) ||
                !loginTextField.getText().equals(editedNutzer.getNutzLogin()) ||
                    !passwordTextField.getText().equals(editedNutzer.getNutzPasswort()) ||
                !roleComboBox.getSelectionModel().getSelectedItem().equals(editedNutzer.getNutzRole())){
            Alert alert = new Alert(
                    Alert.AlertType.NONE,
                    "Möchten Sie die Änderungen erhalten?",
                    ButtonType.YES,
                    ButtonType.NO
            );
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES){
                try {
                    Datenbank.nutzerRepo.updateNutzer(
                            editedNutzer.getNutzNr(),
                            nameTextField.getText(),
                            loginTextField.getText(),
                            passwordTextField.getText(),
                            roleComboBox.getSelectionModel().getSelectedItem());
                    editedNutzer = Datenbank.nutzerRepo.getNutzerByNr(editedNutzer.getNutzNr());
                    alert("Die Änderungen wurden erfolgreich durchgeführt!");
                    stage.close();
                } catch (SQLException throwables) {
                    alert(throwables.getMessage());
                    throwables.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void abbrechenHandle(){
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
}
