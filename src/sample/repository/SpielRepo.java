package sample.repository;

import sample.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpielRepo {
    private final Datenbank datenbank;

    public SpielRepo(Datenbank datenbank) {
        this.datenbank = datenbank;
    }

    public Optional<List<Spiel>> getAllSpiels(){
        List<Spiel> spiels = new ArrayList<>();
        String query = "";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                Integer SpNr = result.getInt("SpNr");
                String SpName = result.getString("SpName");
                String Beschreibung = result.getString("Beschreibung");
                String Publikationsdatum = result.getString("Publikationsdatum");
                Integer PublisherNr = result.getInt("PublisherNr");
                Integer KategorieNr = result.getInt("KategorieNr");
                spiels.add(new Spiel(SpNr, SpName, Beschreibung, Publikationsdatum, PublisherNr, KategorieNr));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.of(spiels);
    }
}
