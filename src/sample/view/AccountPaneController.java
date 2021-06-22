package sample.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.Main;
import sample.model.Nutzer;
import sample.model.Role;
import sample.repository.Datenbank;

import java.sql.SQLException;
import java.util.Optional;

public class AccountPaneController {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField loginTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label roleLabel;
    @FXML
    private Button saveChangesButton;

    private Nutzer currentNutzer = Main.getInstance().getLoggedCustomer();

    @FXML
    public void initialize(){
        nameTextField.setText(currentNutzer.getNutzName());
        loginTextField.setText(currentNutzer.getNutzLogin());
        passwordTextField.setText(currentNutzer.getNutzPasswort());
        roleLabel.setText(currentNutzer.getNutzRole().toString());
        if (currentNutzer.getNutzRole() == Role.Reader){
            saveChangesButton.setDisable(true);
            nameTextField.setDisable(true);
            loginTextField.setDisable(true);
            passwordTextField.setDisable(true);
        }
    }

    @FXML
    public void saveChangesButtonHandle(){
        if (!nameTextField.getText().equals(currentNutzer.getNutzName()) ||
                !loginTextField.getText().equals(currentNutzer.getNutzLogin()) ||
                    !passwordTextField.getText().equals(currentNutzer.getNutzPasswort())){
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
                            currentNutzer.getNutzNr(),
                            nameTextField.getText(),
                            loginTextField.getText(),
                            passwordTextField.getText(),
                            currentNutzer.getNutzRole());
                    currentNutzer = Datenbank.nutzerRepo.getNutzerByNr(currentNutzer.getNutzNr());
                    initialize();
                    alert("Die Änderungen wurden erfolgreich durchgeführt!");
                } catch (SQLException throwables) {
                    alert(throwables.getMessage());
                    throwables.printStackTrace();
                }
            }
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
}
