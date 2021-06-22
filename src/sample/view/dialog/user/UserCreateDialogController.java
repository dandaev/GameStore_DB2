package sample.view.dialog.user;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.model.Role;
import sample.repository.Datenbank;

import java.sql.SQLException;

public class UserCreateDialogController {
    private Stage stage;

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
        roleComboBox.getItems().addAll(Role.Reader, Role.ReadWriter, Role.Admin);
        roleComboBox.setValue(Role.Reader);
    }

    @FXML
    public void saveChangesButtonHandle(){
        if (!nameTextField.getText().isEmpty() &&
                !loginTextField.getText().isEmpty() &&
                    !passwordTextField.getText().isEmpty()){
                try {
                    Datenbank.nutzerRepo.addNutzer(
                            nameTextField.getText(),
                            loginTextField.getText(),
                            passwordTextField.getText(),
                            roleComboBox.getSelectionModel().getSelectedItem());
                    alert("Der neuen Nutzer "+loginTextField.getText()+" wurden erfolgreich erstellt!");
                    stage.close();
                } catch (SQLException throwables) {
                    alert(throwables.getMessage());
                    throwables.printStackTrace();
                }
        }
        else {
            alert("Bitte alle Felder ausfüllen");
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
