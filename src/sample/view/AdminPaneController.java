package sample.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.model.*;
import sample.repository.Datenbank;
import sample.view.dialog.game.GameCreateDialogController;
import sample.view.dialog.game.GameEditDialogController;
import sample.view.dialog.genre.GenreCreateDialogController;
import sample.view.dialog.genre.GenreEditDialogController;
import sample.view.dialog.kategorie.KategorieCreateDialogController;
import sample.view.dialog.kategorie.KategorieEditDialogController;
import sample.view.dialog.publisher.PublisherCreateDialogController;
import sample.view.dialog.publisher.PublisherEditDialogController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;


public class AdminPaneController {

    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.GERMANY);

    @FXML
    private VBox spielsVBox;
    @FXML
    private VBox publisherVBox;
    @FXML
    private VBox kategorieVBOX;
    @FXML
    private VBox genresVBox;
    @FXML
    private Button exportButton;

    @FXML
    public void initialize(){
        installSpiels();
        installPublishers();
        installKategories();
        installGenres();
    }

    @FXML
    public void exportSpielsXmlHandle(){
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "Table Spiels exportieren?",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES){
            try {
                Datenbank.spielRepo.getAllSpielsXML();
                Datenbank.kommentarRepo.getAllKommentariesXML();
                alert("Spiels und Kommentar exportiert, Bitte prüfen Sie file 'spiels.xml' und 'kommentars.xml' in /resources/xml");
            } catch (FileNotFoundException e){
                e.printStackTrace();
                alert(e.getMessage());
            }
        }
    }

    @FXML
    public void exportPublishersXmlHandle(){
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "Table Publisher exportieren?",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES){
            try {
                Datenbank.publisherRepo.getAllPublishersXML();
                alert("Publishers exportiert, Bitte prüfen Sie file 'publisher.xml' in /resources/xml");
            } catch (FileNotFoundException e){
                e.printStackTrace();
                alert(e.getMessage());
            }
        }
    }

    @FXML
    public void exportKategoriesXmlHandle(){
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "Table Kategories exportieren?",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES){
            try {
                Datenbank.kategorieRepo.getAllKategoriesXML();
                alert("Kategories exportiert, Bitte prüfen Sie file 'kategories.xml' in /resources/xml");
            } catch (FileNotFoundException e){
                e.printStackTrace();
                alert(e.getMessage());
            }
        }
    }

    @FXML
    public void exportGenresXmlHandle(){
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "Table Genres exportieren?",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES){
            try {
                Datenbank.genreRepo.getAllGenresXML();
                alert("Genres exportiert, Bitte prüfen Sie file 'genres.xml' in /resources/xml");
            } catch (FileNotFoundException e){
                e.printStackTrace();
                alert(e.getMessage());
            }
        }
    }

    @FXML
    public void handleSpielErstellen(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/dialog/game/GameCreateDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Spiel erstellen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(page));

            GameCreateDialogController controller = loader.getController();
            controller.setStage(dialogStage);
            dialogStage.showAndWait();
            installSpiels();
        }catch (IOException e){
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    @FXML
    public void handlePublisherErstellen(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/dialog/publisher/PublisherCreateDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Publisher erstellen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(page));

            PublisherCreateDialogController controller = loader.getController();
            controller.setStage(dialogStage);
            dialogStage.showAndWait();
            installPublishers();
        }catch (IOException e){
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    @FXML
    public void handleKategorieErstellen(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/dialog/kategori/KategorieCreateDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Kategorie erstellen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(page));

            KategorieCreateDialogController controller = loader.getController();
            controller.setStage(dialogStage);
            dialogStage.showAndWait();
            installKategories();
        }catch (IOException e){
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    @FXML
    public void handleGenreErstellen(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/dialog/genre/GenreCreateDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Genre erstellen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(page));

            GenreCreateDialogController controller = loader.getController();
            controller.setStage(dialogStage);
            dialogStage.showAndWait();
            installGenres();
        }catch (IOException e){
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    private void installSpiels(){
        spielsVBox.getChildren().clear();
        List<Spiel> spiels = Objects.requireNonNull(Datenbank.spielRepo.getAllSpiels().stream().findFirst().orElse(null));
        if (!spiels.isEmpty()) {
            for (Spiel spiel : spiels) {
                ImageView coverImageView = new ImageView(new Image("@../../resources/images/games/"+spiel.getSpImage()));
                coverImageView.setPreserveRatio(false);
                coverImageView.setFitHeight(52.0);
                coverImageView.setFitWidth(82.0);

                VBox vBox = new VBox(6.0);

                HBox hBox = new HBox(12.0);
                VBox vBox1 = new VBox(5.0);
                VBox vBox2 = new VBox(5.0);
                Label id = new Label(spiel.getSpNr().toString());
                Label name = new Label(spiel.getSpName());
                vBox1.getChildren().addAll(id,name);

                Label date = new Label(spiel.getPublikationsdatum());
                Label price = new Label(currencyFormatter.format(spiel.getSpPreis()));
                vBox2.getChildren().addAll(date,price);

                hBox.getChildren().addAll(coverImageView,vBox1,vBox2);

                HBox buttonHBox = new HBox(10.0);
                Button update = new Button("bearbeiten");
                update.getStyleClass().add("in-button");
                update.setPrefHeight(20);
                update.setOnAction((event -> {
                    try{
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("view/dialog/game/GameEditDialog.fxml"));
                        AnchorPane page = loader.load();

                        Stage dialogStage = new Stage();
                        dialogStage.setTitle("Publisher bearbeiten");
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        dialogStage.setScene(new Scene(page));

                        GameEditDialogController controller = loader.getController();
                        controller.setEditedSpiel(spiel);
                        controller.setStage(dialogStage);
                        dialogStage.showAndWait();
                        installPublishers();
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
                            "Spiel "+spiel.getSpName()+" löschen?",
                            ButtonType.YES,
                            ButtonType.NO
                    );
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES){
                        try {
                            Datenbank.spielRepo.deleteSpiel(spiel.getSpNr());
                            initialize();
                            alert("Spiel mit Name "+spiel.getSpName()+" ist gelöscht");
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
                spielsVBox.getChildren().add(vBox);
            }
        }
    }

    private void installPublishers(){
        publisherVBox.getChildren().clear();
        List<Publisher> publisherList = Objects.requireNonNull(Datenbank.publisherRepo.getAllPublishers().stream().findFirst().orElse(null));
        if (!publisherList.isEmpty()) {
            for (Publisher publisher : publisherList) {
                VBox vBox = new VBox(6.0);

                HBox hBox = new HBox(5.0);
                Label id = new Label(publisher.getPubNr().toString());
                Label name = new Label(publisher.getPublisherName());
                hBox.getChildren().addAll(id,name);

                HBox buttonHBox = new HBox(10.0);
                Button update = new Button("bearbeiten");
                update.getStyleClass().add("in-button");
                update.setPrefHeight(20);
                update.setOnAction((event -> {
                    try{
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("view/dialog/publisher/PublisherEditDialog.fxml"));
                        AnchorPane page = loader.load();

                        Stage dialogStage = new Stage();
                        dialogStage.setTitle("Publisher bearbeiten");
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        dialogStage.setScene(new Scene(page));

                        PublisherEditDialogController controller = loader.getController();
                        controller.setEditedPublisher(publisher);
                        controller.setStage(dialogStage);
                        dialogStage.showAndWait();
                        installPublishers();
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
                            "Publisher "+publisher.getPublisherName()+" löschen?",
                            ButtonType.YES,
                            ButtonType.NO
                    );
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES){
                        try {
                            Datenbank.publisherRepo.deletePublisher(publisher.getPubNr());
                            initialize();
                            alert("Publisher mit Name "+publisher.getPublisherName()+" ist gelöscht");
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
                publisherVBox.getChildren().add(vBox);
            }
        }
    }

    private void installKategories(){
        kategorieVBOX.getChildren().clear();
        List<Kategorie> kategories = Objects.requireNonNull(Datenbank.kategorieRepo.getAllKategories().stream().findFirst().orElse(null));
        if (!kategories.isEmpty()) {
            for (Kategorie kategorie : kategories) {
                VBox vBox = new VBox(6.0);

                HBox hBox = new HBox(5.0);
                Label id = new Label(kategorie.getKatNr().toString());
                Label name = new Label(kategorie.getKatName());
                hBox.getChildren().addAll(id,name);

                HBox buttonHBox = new HBox(10.0);
                Button update = new Button("bearbeiten");
                update.getStyleClass().add("in-button");
                update.setPrefHeight(20);
                update.setOnAction((event -> {
                    try{
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("view/dialog/kategorie/KategorieEditDialog.fxml"));
                        AnchorPane page = loader.load();

                        Stage dialogStage = new Stage();
                        dialogStage.setTitle("Kategorie bearbeiten");
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        dialogStage.setScene(new Scene(page));

                        KategorieEditDialogController controller = loader.getController();
                        controller.setEditedKategorie(kategorie);
                        controller.setStage(dialogStage);
                        dialogStage.showAndWait();
                        installKategories();
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
                            "Kategorie "+kategorie.getKatName()+" löschen?",
                            ButtonType.YES,
                            ButtonType.NO
                    );
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES){
                        try {
                            Datenbank.kategorieRepo.deleteKategorie(kategorie.getKatNr());
                            initialize();
                            alert("Kategorie mit Name "+kategorie.getKatName()+" ist gelöscht");
                        } catch (SQLException e){
                            e.printStackTrace();
                            alert(e.getMessage());
                        }
                    }
                });

                if (kategorie.getKatNr() == 5){
                    buttonHBox.getChildren().add(update);
                }
                else {
                    buttonHBox.getChildren().addAll(update, delete);
                }
                buttonHBox.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox,buttonHBox);
                vBox.getStyleClass().add("games-card");
                kategorieVBOX.getChildren().add(vBox);
            }
        }
    }

    private void installGenres(){
        genresVBox.getChildren().clear();
        List<Genre> genres = Objects.requireNonNull(Datenbank.genreRepo.getAllGenres().stream().findFirst().orElse(null));
        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                VBox vBox = new VBox(6.0);

                HBox hBox = new HBox(5.0);
                Label id = new Label(genre.getGeNr().toString());
                Label name = new Label(genre.getGeName());
                hBox.getChildren().addAll(id,name);

                HBox buttonHBox = new HBox(10.0);
                Button update = new Button("bearbeiten");
                update.getStyleClass().add("in-button");
                update.setPrefHeight(20);
                update.setOnAction((event -> {
                    try{
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("view/dialog/genre/GenreEditDialog.fxml"));
                        AnchorPane page = loader.load();

                        Stage dialogStage = new Stage();
                        dialogStage.setTitle("Genre bearbeiten");
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        dialogStage.setScene(new Scene(page));

                        GenreEditDialogController controller = loader.getController();
                        controller.setEditedGenre(genre);
                        controller.setStage(dialogStage);
                        dialogStage.showAndWait();
                        installGenres();
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
                            "Genre "+genre.getGeName()+" löschen?",
                            ButtonType.YES,
                            ButtonType.NO
                    );
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES){
                        try {
                            Datenbank.genreRepo.deleteGenre(genre.getGeNr());
                            initialize();
                            alert("Genre mit Name "+genre.getGeName()+" ist gelöscht");
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
                genresVBox.getChildren().add(vBox);
            }
        }
    }

    private void alert(String text) {
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                text,
                ButtonType.CLOSE
        );
        alert.setContentText(text);
        alert.showAndWait();
    }
}
