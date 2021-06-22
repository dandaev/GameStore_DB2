package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.model.Nutzer;
import sample.repository.Datenbank;
import sample.view.LoginPaneController;
import sample.view.MainPaneController;

import java.io.IOException;

public class Main extends Application {

    private Group root = new Group();
    private Nutzer loggedNutzer;
    private Datenbank datenbank;

    // for the use of loggedCustomer in the Checkout Page
    private static Main mainInstance;
    public Main() {
        mainInstance = this;
    }
    public static Main getInstance() {
        return mainInstance;
    }

    public Parent createContent() {
        gotoLogin();
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("GameStore");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public Nutzer getLoggedCustomer() {
        return loggedNutzer;
    }

    public boolean userLogging(String userName, String password) throws Exception {
        Datenbank.giveDatenbankPermissionByUser("reader","reader");
        Nutzer nutzer = Datenbank.nutzerRepo.getNutzerByUsername(userName);
        if(nutzer != null && nutzer.getNutzPasswort().equals(password)) {
            loggedNutzer = nutzer;
            gotoGameStore();
            return true;
        } else {
            System.out.println("Nutzer leer");
            return false;
        }
    }

    public void userLogout() throws Exception {
        loggedNutzer = null;
        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setMinWidth(370);
        primaryStage.setMinHeight(320);
        gotoLogin();
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
    }

    public void gotoGameStore() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainPane.fxml"));
        AnchorPane store = loader.load();
        MainPaneController mainPaneController = loader.getController();
        mainPaneController.setApp(this);
        root.getChildren().clear();
        root.getChildren().add(store);
        Stage primaryStage = (Stage) root.getScene().getWindow();
        store.prefHeightProperty().bind(primaryStage.heightProperty());
        store.prefWidthProperty().bind(primaryStage.widthProperty());
        primaryStage.setWidth(895);
        primaryStage.setHeight(595);
        primaryStage.setResizable(false);
        mainPaneController.setWarningStage(primaryStage);
    }

    public void gotoLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/LoginPane.fxml"));
            AnchorPane page = loader.load();
            root.getChildren().clear();
            root.getChildren().addAll(page);
            LoginPaneController login = loader.getController();
            login.setApp(this);
        } catch (Exception ex) {
            System.out.println(ex.getMessage()+"gotoLogin()");
        }
    }
}
