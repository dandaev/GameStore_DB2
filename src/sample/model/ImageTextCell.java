package sample.model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import sample.repository.Datenbank;

import java.text.NumberFormat;
import java.util.Locale;

public class ImageTextCell extends ListCell<Spiel> {
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.GERMANY);

    @Override
    protected void updateItem(Spiel item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null); // doesn't show anything
        } else {
            // create the cell
            HBox hbox = new HBox(8.0); // Gap between controls
            hbox.setAlignment(Pos.CENTER_LEFT);

            // set cover image
            ImageView coverImageView = new ImageView(new Image("@../../resources/images/games/"+item.getSpImage()));
            coverImageView.setPreserveRatio(false);
            coverImageView.setFitHeight(32.0);
            coverImageView.setFitWidth(52.0);

            // set text
            Label title = new Label(item.getSpName());
            title.setFont(Font.font("System", 12));
            title.setTextAlignment(TextAlignment.LEFT);
            title.setWrapText(true);
            title.setMaxWidth(USE_PREF_SIZE);
            title.setPrefHeight(USE_COMPUTED_SIZE);

            Label platform = new Label(Datenbank.kategorieRepo.getKategorieByKatNr(item.getPublisherNr()).getKatName());
            platform.setFont(Font.font("System", 11));
            platform.setTextAlignment(TextAlignment.LEFT);

            Label price = new Label();
            String currencyPrice = currencyFormatter.format(item.getSpPreis());
            price.setText(currencyPrice);
            price.setFont(Font.font("System", FontWeight.BOLD, 12));

            VBox vb = new VBox(2.0);
            VBox mainVb = new VBox(5.0);


            vb.getChildren().addAll(platform,price);

            hbox.getChildren().addAll(coverImageView, vb);
            mainVb.getChildren().addAll(title,hbox);
            setGraphic(mainVb);
            setPrefWidth(USE_PREF_SIZE);
            setPrefHeight(USE_COMPUTED_SIZE);
        }
    }
}
