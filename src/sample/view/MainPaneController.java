package sample.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.model.*;

import sample.repository.Datenbank;
import sample.view.dialog.kommentar.KommentarEditDialogController;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class MainPaneController {

    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.GERMANY);
    private Main application;
    private Stage warningStage;
    private final int currentNutzerNr = Main.getInstance().getLoggedCustomer().getNutzNr();
    private List<Spiel> currentNutzerSpiels;

    public void setApp(Main application) {
        this.application = application;
    }

    public void setWarningStage(Stage warningStage) {
        this.warningStage = warningStage;
    }

    private Spiel currentSpiel;

    private ObservableList<Spiel> spiels = FXCollections.observableArrayList();

    @FXML
    private ListView<Spiel> spielListView;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private VBox spielListe;
    @FXML
    private ScrollPane gamesScrollPane;
    @FXML
    private Label spielName;
    @FXML
    private Button addToMygamesButton;
    @FXML
    private Label spielPublikationdatum;
    @FXML
    private Label spielPublisher;
    @FXML
    private Label spielKategorie;
    @FXML
    private Label spielPreis;
    @FXML
    private FlowPane spielGenresFlowPane;
    @FXML
    private Label spielBeschreibung;
    @FXML
    private VBox kommentarVBox;
    @FXML
    private ImageView spielImage;
    @FXML
    private TextField kommentarTextField;
    @FXML
    private Button kommentarButton;
    @FXML
    private Hyperlink myGamesHyperLink;
    @FXML
    private Hyperlink usersHyperLink;

    private int selectedIndex = 0;

    public void initialize() {
        try {
            giveNutzerPermission(Main.getInstance().getLoggedCustomer());
            spiels.addAll(Objects.requireNonNull(Datenbank.spielRepo.getAllSpiels().stream().findFirst().orElse(null)));
            spielListView.setItems(spiels);
            gotoStore();
        } catch (Exception e) {
            alert(e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    private void proccessLogout() {
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "Wollen Sie wirklich aus dem Konto raus??",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (application == null) {
                return;
            }
            try {
                application.userLogout();
            } catch (Exception e) {
                e.printStackTrace();
                alert(e.getMessage());
            }
        }
    }

    @FXML
    private void gotoStore() {
        if (Main.getInstance().getLoggedCustomer().getNutzRole().equals(Role.Admin)) {
            goToAdmin();
        } else {
            currentNutzerSpiels = Objects.requireNonNull(Datenbank.spielRepo.getSpielsByNutzNr(currentNutzerNr).stream().findFirst().orElse(null));
            spielListView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        currentSpiel = newValue;
                        if (checkIfNutzerHatSpiel(newValue)) {
                            addToMygamesButton.setText("Schon im Mygames");
                            addToMygamesButton.setDisable(true);
                        } else if (Main.getInstance().getLoggedCustomer().getNutzRole().equals(Role.Reader)) {
                            addToMygamesButton.setVisible(false);
                        } else {
                            addToMygamesButton.setText("Kaufen");
                            addToMygamesButton.setDisable(false);
                        }
                        spielName.setText(newValue.getSpName());
                        spielPublikationdatum.setText(newValue.getPublikationsdatum());
                        spielPublisher.setText(Datenbank.publisherRepo.getPublisherByPubNr(newValue.getPublisherNr()).getPublisherName());
                        spielKategorie.setText(Datenbank.kategorieRepo.getKategorieByKatNr(newValue.getKategorieNr()).getKatName());
                        spielPreis.setText(currencyFormatter.format(newValue.getSpPreis()));
                        spielGenresFlowPane.getChildren().clear();
                        setSpielGenres(Objects.requireNonNull(Datenbank.genreRepo.getGenresBySpNr(newValue.getSpNr()).stream().findFirst().orElse(null)));
                        spielBeschreibung.setText(newValue.getBeschreibung());
                        spielImage.setImage(new Image("@../../resources/images/games/" + newValue.getSpImage()));
                        kommentarListUpdate(newValue.getSpNr());
                    }
            );
            spielListView.getSelectionModel().select(selectedIndex);
            if (checkIfNutzerHatSpiel(spielListView.getSelectionModel().getSelectedItem())) {
                addToMygamesButton.setText("Schon im Mygames");
                addToMygamesButton.setDisable(true);
            } else if (Main.getInstance().getLoggedCustomer().getNutzRole().equals(Role.Reader)) {
                addToMygamesButton.setVisible(false);
            } else {
                addToMygamesButton.setText("Kaufen");
                addToMygamesButton.setDisable(false);
            }
            spielListView.setCellFactory((listview) -> new ImageTextCell());
            mainBorderPane.setCenter(gamesScrollPane);
            mainBorderPane.setRight(spielListe);
        }
    }

    @FXML
    private void gotoMygames() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MygamesPane.fxml"));
            ScrollPane mygames = loader.load();
            mainBorderPane.setCenter(mygames);
            mainBorderPane.setRight(null);
            selectedIndex = 0;
        } catch (IOException e) {
            e.printStackTrace();
            alert(e.getMessage());
        }

    }

    @FXML
    private void gotoAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountPane.fxml"));
            GridPane account = loader.load();
            mainBorderPane.setCenter(account);
            mainBorderPane.setRight(null);
            selectedIndex = 0;
        } catch (IOException e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    @FXML
    private void gotoUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UsersPane.fxml"));
            AnchorPane users = loader.load();
            mainBorderPane.setCenter(users);
            mainBorderPane.setRight(null);
        } catch (IOException e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    @FXML
    private void kaufenButtonClicked() {
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "Kaufen Sie das Spiel " + currentSpiel.getSpName() + "?",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                Datenbank.nutzerRepo.addSpielForNutzer(currentNutzerNr, currentSpiel.getSpNr());
                selectedIndex = spielListView.getSelectionModel().getSelectedIndex();
                gotoStore();
                alert("Spiel zu MyGames hinzugefügt");
            } catch (SQLException throwables) {
                alert(throwables.getMessage());
                throwables.printStackTrace();
            }
        }
    }

    @FXML
    private void kommentarEingeben() {
        if (!kommentarTextField.getText().isEmpty()) {
            try {
                Datenbank.kommentarRepo.addKommentar(kommentarTextField.getText(), currentSpiel.getSpNr(), Main.getInstance().getLoggedCustomer().getNutzNr());
                kommentarTextField.clear();
                kommentarListUpdate(currentSpiel.getSpNr());
                alert("Ihr Kommentar wurde hinzugefügt, Danke für das Feedback!");
            } catch (SQLException e) {
                e.printStackTrace();
                alert(e.getMessage());
            }
        } else {
            alert("Fügen Sie Kommentar ein");
        }
    }

    private void giveNutzerPermission(Nutzer nutzer) throws Exception {
        if (Role.Reader.equals(nutzer.getNutzRole())) {
            Datenbank.giveDatenbankPermissionByUser("reader", "reader");
            addToMygamesButton.setDisable(true);
            addToMygamesButton.setVisible(false);
            kommentarTextField.setDisable(true);
            kommentarButton.setDisable(true);
            myGamesHyperLink.setVisible(false);
        } else if (Role.ReadWriter.equals(nutzer.getNutzRole())) {
            Datenbank.giveDatenbankPermissionByUser("readWriter", "readwriter");
        } else if (Role.Admin.equals(nutzer.getNutzRole())) {
            Datenbank.giveDatenbankPermissionByUser("admin", "admin");
            usersHyperLink.setVisible(true);
            myGamesHyperLink.setDisable(true);
        }
    }

    private void setSpielGenres(List<Genre> genres) {
        if (!genres.isEmpty()) {
            for (Genre g : genres) {
                Label label = new Label(g.getGeName());
                label.setFont(new Font("Arial", 12));
                label.setStyle("-fx-text-fill: #6e92b5; -fx-padding: 3; -fx-font-weight: bold");
                spielGenresFlowPane.getChildren().add(label);
            }
        } else {
            Label label = new Label("Spiel hat keine Genre");
            label.setFont(new Font("Arial", 12));
            label.setStyle("-fx-text-fill: #000; -fx-padding: 5px; -fx-font-weight: bold");
            spielGenresFlowPane.getChildren().add(label);
        }
    }

    private void setSpielKommentare(List<Kommentar> kommentare) {
        if (!kommentare.isEmpty()) {
            for (Kommentar k : kommentare) {
                String cssLayout = "-fx-border-color: #283863;\n" +
                        "-fx-border-insets: 5;\n" +
                        "-fx-border-width: 1;\n" +
                        "-fx-padding: 3;";

                VBox vBox = new VBox(4.0); // Gap between controls
                vBox.setAlignment(Pos.CENTER_LEFT);
                Separator line = new Separator();

                HBox hBox = new HBox(8.0);
                hBox.setAlignment(Pos.CENTER_LEFT);

                Label username = new Label(Datenbank.nutzerRepo.getNutzerByNr(k.getKommNutzer()).getNutzLogin());
                username.setFont(Font.font("System", 12));
                username.setStyle("-fx-text-fill: #374952; -fx-font-weight: bold");
                username.setTextAlignment(TextAlignment.LEFT);
                username.setWrapText(true);
                hBox.getChildren().add(username);
                if (k.getKommNutzer().equals(Main.getInstance().getLoggedCustomer().getNutzNr())) {
                    username.setPrefWidth(300);
                    username.setMaxWidth(Region.USE_PREF_SIZE);
                    username.setMinWidth(Region.USE_PREF_SIZE);

                    Button update = new Button("bearbeiten");
                    update.getStyleClass().add("in-button");
                    update.setPrefWidth(120);
                    update.setOnAction((event -> {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("view/dialog/kommentar/KommentarEditDialog.fxml"));
                            AnchorPane page = loader.load();

                            Stage dialogStage = new Stage();
                            dialogStage.setTitle("Kommentar bearbeiten");
                            dialogStage.initModality(Modality.WINDOW_MODAL);
                            dialogStage.initOwner(warningStage);
                            dialogStage.setScene(new Scene(page));

                            KommentarEditDialogController controller = loader.getController();
                            controller.setEditedKommentar(k);
                            controller.setStage(dialogStage);
                            dialogStage.showAndWait();
                            kommentarListUpdate(currentSpiel.getSpNr());
                        } catch (IOException e) {
                            e.printStackTrace();
                            alert(e.getMessage());
                        }
                    }));

                    Button delete = new Button("löschen");
                    delete.getStyleClass().add("in-button-red");
                    delete.setPrefWidth(100);
                    delete.setOnAction(event -> {
                        Alert alert = new Alert(
                                Alert.AlertType.NONE,
                                "Kommentar löschen?",
                                ButtonType.YES,
                                ButtonType.NO
                        );
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.YES) {
                            try {
                                Datenbank.kommentarRepo.deleteKommentar(k.getKommNr());
                                alert("Kommentar ist gelöscht");
                                kommentarListUpdate(currentSpiel.getSpNr());
                            } catch (SQLException e) {
                                e.printStackTrace();
                                alert(e.getMessage());
                            }
                        }
                    });
                    hBox.getChildren().addAll(update, delete);
                }

                Label text = new Label(k.getKommText());
                text.setFont(Font.font("System", 12));
                text.setTextAlignment(TextAlignment.LEFT);
                text.setWrapText(false);
                text.setMaxWidth(Region.USE_PREF_SIZE);
                text.setPrefHeight(Region.USE_COMPUTED_SIZE);


                vBox.getChildren().addAll(hBox, line, text);
                vBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                vBox.setStyle(cssLayout);

                kommentarVBox.getChildren().add(vBox);
            }
        } else {
            Label promtText = new Label("Keine Kommentare");
            promtText.setFont(Font.font("System", 12));
            promtText.setStyle("-fx-text-fill: #939393;");
            promtText.setTextAlignment(TextAlignment.CENTER);
            kommentarVBox.getChildren().add(promtText);
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

    private void kommentarListUpdate(Integer spNr) {
        kommentarVBox.getChildren().clear();
        setSpielKommentare(Objects.requireNonNull(Datenbank.kommentarRepo.getKommentarsBySpNr(spNr).stream().findFirst().orElse(null)));
    }

    private boolean checkIfNutzerHatSpiel(Spiel spiel) {
        if (!currentNutzerSpiels.isEmpty()) {
            for (Spiel s : currentNutzerSpiels) {
                if (s.getSpNr().equals(spiel.getSpNr())) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private void goToAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminPane.fxml"));
            AnchorPane admin = loader.load();
            mainBorderPane.setCenter(admin);
            mainBorderPane.setRight(null);
        } catch (IOException e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
    }
}
