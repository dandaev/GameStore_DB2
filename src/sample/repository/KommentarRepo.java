package sample.repository;

import sample.model.Genre;
import sample.model.Kommentar;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KommentarRepo {

    private final Datenbank datenbank;

    public KommentarRepo(Datenbank datenbank) {
        this.datenbank = datenbank;
    }

    public Optional<List<Kommentar>> getKommentarsBySpNr(Integer spielNr) {
        List<Kommentar> kommentars = new ArrayList<>();
        String query = "SELECT [KommNr]\n" +
                "      ,[KommText]\n" +
                "      ,[KommSpiel]\n" +
                "      ,[KommNutzer]\n" +
                "  FROM [GameStore].[dbo].[Kommentar]\n" +
                "  WHERE [KommSpiel] = '"+spielNr+"';";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                Integer KommNr = result.getInt("KommNr");
                String KommText = result.getString("KommText");
                Integer KommSpiel = result.getInt("KommSpiel");
                Integer KommNutzer = result.getInt("KommNutzer");
                kommentars.add(new Kommentar(KommNr, KommText, KommSpiel,KommNutzer));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(kommentars);
    }

    public void addKommentar(String kommText, Integer kommSpiel, Integer kommNutzer) throws SQLException {
        String query = "INSERT INTO [dbo].[Kommentar]\n" +
                "           ([KommText]\n" +
                "           ,[KommSpiel]\n" +
                "           ,[KommNutzer])\n" +
                "     VALUES\n" +
                "           ('"+kommText+"'\n" +
                "           ,"+kommSpiel+"\n" +
                "           ,"+kommNutzer+")";
        datenbank.setQuery(query);
    }

    public void updateKommentar(String kommText, Integer kommNr) throws SQLException {
        String query = "UPDATE [dbo].[Kommentar]\n" +
                "   SET [KommText] = '"+kommText+"'\n" +
                " WHERE [KommNr] = "+kommNr+"";
        datenbank.setQuery(query);
    }

    public void deleteKommentar(Integer kommNr) throws SQLException {
        String query = "DELETE FROM [dbo].[Kommentar]\n" +
                       "WHERE [KommNr] = "+kommNr+"";
        datenbank.setQuery(query);
    }

    //XML
    public void getAllKommentariesXML() throws FileNotFoundException {
        String query = "SELECT (SELECT [Kommentar].[KommNr] as ID,\n" +
                "                [Kommentar].[KommText] as KOMMENTARTEXT,\n" +
                "                [Spiel].[SpName] as SPIEL,\n" +
                "                [Nutzer].[NutzLogin] as NUTZER\n" +
                "                FROM [GameStore].[dbo].[Kommentar] as KOMMENTAR\n" +
                "                LEFT JOIN [Spiel] ON [Kommentar].[KommSpiel] = [Spiel].[SpNr]\n" +
                "                LEFT JOIN [Nutzer] ON [Kommentar].[KommNutzer] = [Nutzer].[NutzNr]\n" +
                "                FOR XML PATH('KOMMENTAR'), TYPE, ELEMENTS , ROOT('KOMMENTARS')) as KommentarXml ";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                SQLXML xml = result.getSQLXML("KommentarXml");
                String values = xml.getString();
                PrintWriter out = new PrintWriter("D:/DE-Study/Sommersemester(2021)/DB2/Project/GameStore_DB2/src/resources/xml/kommentars.xml");
                out.println(values);
                out.flush();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
