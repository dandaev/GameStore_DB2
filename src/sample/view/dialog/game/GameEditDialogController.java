package sample.view.dialog.game;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.model.*;
import sample.repository.Datenbank;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class GameEditDialogController {
    private Stage stage;
    private Spiel editedSpiel;

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
    private ComboBox<Genre> genresComboBox;
    @FXML
    private VBox genresVBox;
    @FXML
    private VBox kommentareVBox;

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleGenreAddButton() {
        List<Genre> genres = Objects.requireNonNull(Datenbank.genreRepo.getGenresBySpNr(editedSpiel.getSpNr()).stream().findFirst().orElse(null));
        if (!ifContains(genres)){
            try {
                Datenbank.spielRepo.addGenreForSpiel(editedSpiel.getSpNr(),genresComboBox.getSelectionModel().getSelectedItem().getGeNr());
                installGenres(editedSpiel);
                alert("Genre erfolgreich hinzugefügt");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                alert(throwables.getMessage());
            }
        }
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
                Datenbank.spielRepo.updateSpiel(
                        editedSpiel.getSpNr(),nameTextField.getText(),
                        beschreibungTextField.getText(),dateTextField.getText(),
                        publisherComboBox.getSelectionModel().getSelectedItem().getPubNr(),
                        kategorieComboBox.getSelectionModel().getSelectedItem().getKatNr(),
                        Double.parseDouble(preisTextField.getText()),imageTextField.getText(),
                        linkTextField.getText());
                alert("Spiel bearbeited!");
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

    public void setEditedSpiel(Spiel editedSpiel) {
        this.editedSpiel = editedSpiel;
        nameTextField.setText(editedSpiel.getSpName());
        dateTextField.setText(editedSpiel.getPublikationsdatum());
        preisTextField.setText(editedSpiel.getSpPreis().toString());
        imageTextField.setText(editedSpiel.getSpImage());
        linkTextField.setText(editedSpiel.getLinkZumDownload());
        beschreibungTextField.setText(editedSpiel.getBeschreibung());

        List<Publisher> publishers = Objects.requireNonNull(Datenbank.publisherRepo.getAllPublishers().stream().findFirst().orElse(null));
        publisherComboBox.getItems().addAll(publishers);
        publisherComboBox.converterProperty();
        publisherComboBox.getSelectionModel().select(Datenbank.publisherRepo.getPublisherByPubNr(editedSpiel.getPublisherNr()));

        List<Kategorie> kategories = Objects.requireNonNull(Datenbank.kategorieRepo.getAllKategories().stream().findFirst().orElse(null));
        kategorieComboBox.getItems().addAll(kategories);
        kategorieComboBox.converterProperty();
        kategorieComboBox.getSelectionModel().select(Datenbank.kategorieRepo.getKategorieByKatNr(editedSpiel.getKategorieNr()));

        List<Genre> genres = Objects.requireNonNull(Datenbank.genreRepo.getAllGenres().stream().findFirst().orElse(null));
        genresComboBox.getItems().addAll(genres);
        genresComboBox.converterProperty();
        genresComboBox.getSelectionModel().select(0);
        installGenres(editedSpiel);
        installKommentare(editedSpiel);
    }

    private void installGenres(Spiel spiel) {
        genresVBox.getChildren().clear();
        List<Genre> genres = Objects.requireNonNull(Datenbank.genreRepo.getGenresBySpNr(spiel.getSpNr()).stream().findFirst().orElse(null));
        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                VBox vBox = new VBox(6.0);

                HBox hBox = new HBox(5.0);
                Label id = new Label(genre.getGeNr().toString());
                Label name = new Label(genre.getGeName());
                hBox.getChildren().addAll(id, name);

                HBox buttonHBox = new HBox(10.0);
                Button delete = new Button("löschen");
                delete.getStyleClass().add("in-button-red");
                delete.setPrefHeight(20);
                delete.setOnAction(event -> {
                    Alert alert = new Alert(
                            Alert.AlertType.NONE,
                            "Genre " + genre.getGeName() + " löschen?",
                            ButtonType.YES,
                            ButtonType.NO
                    );
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES) {
                        try {
                            Datenbank.spielRepo.deleteGenreForSpiel(spiel.getSpNr(), genre.getGeNr());
                            installGenres(editedSpiel);
                            alert("Genre mit Name " + genre.getGeName() + " ist aus dem Spiel " + spiel.getSpName() + " gelöscht");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            alert(e.getMessage());
                        }
                    }
                });

                buttonHBox.getChildren().addAll(delete);
                buttonHBox.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox, buttonHBox);
                vBox.getStyleClass().add("games-card");
                genresVBox.getChildren().add(vBox);
            }
        }
    }

    private void installKommentare(Spiel spiel) {
        kommentareVBox.getChildren().clear();
        List<Kommentar> kommentars = Objects.requireNonNull(Datenbank.kommentarRepo.getKommentarsBySpNr(spiel.getSpNr()).stream().findFirst().orElse(null));
        if (!kommentars.isEmpty()) {
            for (Kommentar k : kommentars) {
                VBox vBox = new VBox(4.0); // Gap between controls
                vBox.setAlignment(Pos.CENTER_LEFT);

                HBox hBox = new HBox(8.0);
                hBox.setAlignment(Pos.CENTER_LEFT);

                Label username = new Label(Datenbank.nutzerRepo.getNutzerByNr(k.getKommNutzer()).getNutzLogin());
                username.setFont(Font.font("System", 12));
                username.setStyle("-fx-text-fill: #374952; -fx-font-weight: bold");
                username.setTextAlignment(TextAlignment.LEFT);
                username.setWrapText(true);
                hBox.getChildren().add(username);
                username.setPrefWidth(300);
                username.setMaxWidth(Region.USE_PREF_SIZE);
                username.setMinWidth(Region.USE_PREF_SIZE);


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
                            installKommentare(editedSpiel);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            alert(e.getMessage());
                        }
                    }
                });

                hBox.getChildren().addAll(delete);
                hBox.setAlignment(Pos.CENTER_RIGHT);

                Label text = new Label(k.getKommText());
                text.setFont(Font.font("System", 12));
                text.setTextAlignment(TextAlignment.LEFT);
                text.setWrapText(false);
                text.setMaxWidth(Region.USE_PREF_SIZE);
                text.setPrefHeight(Region.USE_COMPUTED_SIZE);


                vBox.getChildren().addAll(hBox, text);
                vBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                vBox.getStyleClass().add("games-card");

                kommentareVBox.getChildren().add(vBox);
            }
        } else {
            Label promtText = new Label("Keine Kommentare");
            promtText.setFont(Font.font("System", 12));
            promtText.setStyle("-fx-text-fill: #939393;");
            promtText.setTextAlignment(TextAlignment.CENTER);
            kommentareVBox.getChildren().add(promtText);
        }
    }

    private boolean ifContains(List<Genre> genres){
        if (!genres.isEmpty()){
            for (Genre genre: genres){
                return genre.getGeNr().equals(genresComboBox.getSelectionModel().getSelectedItem().getGeNr());
            }
            return false;
        }else {
            return false;
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
