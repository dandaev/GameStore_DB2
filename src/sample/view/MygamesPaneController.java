package sample.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import sample.Main;
import sample.model.Spiel;
import sample.repository.Datenbank;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class MygamesPaneController {

    @FXML
    private TilePane mygamesTilePane;

    private final int nutzerNr = Main.getInstance().getLoggedCustomer().getNutzNr();

    public void initialize(){
        loadMyGames(Objects.requireNonNull(Datenbank.spielRepo.getSpielsByNutzNr(nutzerNr).stream().findFirst().orElse(null)));
    }

    private void loadMyGames(List<Spiel> spielList){
        if(!spielList.isEmpty()){
            mygamesTilePane.getChildren().clear();
            for(Spiel spiel: spielList){
                VBox vBox = new VBox(5.0);
                ImageView coverImageView = new ImageView(new Image("@../../resources/images/games/" + spiel.getSpImage()));
                coverImageView.setFitHeight(70);
                coverImageView.setFitWidth(100);
                final Tooltip tooltip = new Tooltip("Download " + spiel.getSpName());
                Hyperlink downloadGame = new Hyperlink(spiel.getSpName());
                downloadGame.setContentDisplay(ContentDisplay.TOP);
                downloadGame.setGraphic(coverImageView);
                downloadGame.getStyleClass().add("library-tiles");
                Tooltip.install(downloadGame, tooltip);
                downloadGame.setCursor(Cursor.DEFAULT);
                downloadGame.setPrefWidth(100);

                Button delete = new Button("Löschen");
                delete.getStyleClass().add("in-button-red");
                delete.setOnAction((ActionEvent e) ->{
                    Alert alert = new Alert(
                            Alert.AlertType.NONE,
                            "Möchten Sie das "+spiel.getSpName()+" aus Mygames entfernen ?",
                            ButtonType.YES,
                            ButtonType.NO
                    );
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES){
                        try {
                            Datenbank.nutzerRepo.deleteSpielForNutzer(nutzerNr,spiel.getSpNr());
                            initialize();
                            alert("Das Spiel wurde erfolgreich aus MyGames entfernt");
                        } catch (SQLException throwables) {
                            alert(throwables.getMessage());
                            throwables.printStackTrace();
                        }
                    }
                });

                vBox.getChildren().addAll(delete,downloadGame);
                vBox.setAlignment(Pos.CENTER);
                vBox.getStyleClass().add("games-card");

                mygamesTilePane.getChildren().add(vBox);

                downloadGame.setOnAction((ActionEvent e) -> {
                    try {
                        Desktop.getDesktop().browse(new URI(spiel.getLinkZumDownload()));
                    } catch (URISyntaxException | IOException ex) {
                        alert("Spiel Link Probleme");
                    }
                });
            }
        }else {
            mygamesTilePane.getChildren().clear();
            Label noGames = new Label("Die Spiele, die Sie kaufen, werden in diesem Bereich angezeigt. Sie können Spiele im GameSpot Store finden.");
            noGames.setPadding(new Insets(10, 10, 10, 10));
            mygamesTilePane.getChildren().add(noGames);
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
