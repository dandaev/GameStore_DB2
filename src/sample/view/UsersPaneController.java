package sample.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.model.Nutzer;
import sample.repository.Datenbank;
import sample.view.dialog.user.UserCreateDialogController;
import sample.view.dialog.user.UserEditDialogController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UsersPaneController {

    @FXML
    private VBox usersVBox;

    @FXML
    public void initialize(){
        usersVBox.getChildren().clear();
        List<Nutzer> nutzerList = Objects.requireNonNull(Datenbank.nutzerRepo.getAllNutzers().stream().findFirst().orElse(null));
        if (!nutzerList.isEmpty()) {
            for (Nutzer nutzer : nutzerList) {
                VBox vBox = new VBox(6.0);

                HBox hBox = new HBox(5.0);
                Label name = new Label(nutzer.getNutzName());
                Label login = new Label(nutzer.getNutzLogin());
                Label passwort = new Label(nutzer.getNutzPasswort());
                Label role = new Label(nutzer.getNutzRole().toString());
                hBox.getChildren().addAll(name,login,passwort,role);

                HBox buttonHBox = new HBox(10.0);
                Button update = new Button("bearbeiten");
                update.getStyleClass().add("in-button");
                update.setPrefHeight(20);
                update.setOnAction((event -> {
                    try{
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("view/dialog/user/UserEditDialog.fxml"));
                        GridPane page = loader.load();

                        Stage dialogStage = new Stage();
                        dialogStage.setTitle("Nutzer bearbeiten");
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        dialogStage.setScene(new Scene(page));

                        UserEditDialogController controller = loader.getController();
                        controller.setEditedUser(nutzer);
                        controller.setStage(dialogStage);
                        dialogStage.showAndWait();
                        initialize();
                    }catch (IOException e){
                        e.printStackTrace();
                        alert(e.getMessage());
                    }
                }));

                Button delete = new Button("löschen");
                delete.getStyleClass().add("in-button-red");
                delete.setPrefHeight(20);
                delete.setOnAction(event -> {
                    Alert alert = new Alert(
                            Alert.AlertType.NONE,
                            "Nutzer "+nutzer.getNutzLogin()+" löschen?",
                            ButtonType.YES,
                            ButtonType.NO
                    );
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES){
                        try {
                            Datenbank.nutzerRepo.deleteNutzer(nutzer.getNutzNr());
                            initialize();
                            alert("Nutzer "+nutzer.getNutzLogin()+" ist gelöscht");
                        } catch (SQLException e){
                            e.printStackTrace();
                            alert(e.getMessage());
                        }
                    }
                });

                buttonHBox.getChildren().addAll(update,delete);
                buttonHBox.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox,buttonHBox);
                vBox.getStyleClass().add("games-card");
                usersVBox.getChildren().add(vBox);
            }
        }
    }

    @FXML
    public void exportNutzersXmlHandle(){
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "Table Nutzers exportieren?",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES){
            try {
                Datenbank.nutzerRepo.getAllNutzersXML();
                alert("Nutzers exportiert, Bitte prüfen Sie file 'nutzers.xml' in /resources/xml");
            } catch (FileNotFoundException e){
                e.printStackTrace();
                alert(e.getMessage());
            }
        }
    }

    @FXML
    public void onNeuNutzerButtonHandle(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/dialog/user/UserCreateDialog.fxml"));
            GridPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nutzer erstellen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(page));

            UserCreateDialogController controller = loader.getController();
            controller.setStage(dialogStage);
            dialogStage.showAndWait();
            initialize();
        }catch (IOException e){
            e.printStackTrace();
            alert(e.getMessage());
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
