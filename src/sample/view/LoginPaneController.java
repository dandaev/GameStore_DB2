package sample.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPaneController implements Initializable {

    @FXML
    private TextField loginUserName;
    @FXML private PasswordField loginPasswordField;
    //@FXML private Button loginButton;
    @FXML private Label errorMessage;

    private Main application;

    public void setApp(Main application) {
        this.application = application;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setText("");
        loginUserName.setPromptText("Username");
        loginPasswordField.setPromptText("Password");
    }

    @FXML
    private void processLogin(){
        if(application == null) {
            errorMessage.setText("Welcome, " + loginUserName.getText());
        } else {
            try {
                if(!application.userLogging(loginUserName.getText(), loginPasswordField.getText())) {
                    errorMessage.setText("UserName/Password is Incorrect");
                }
            } catch (Exception e) {
                errorMessage.setText("processLogin"+e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
