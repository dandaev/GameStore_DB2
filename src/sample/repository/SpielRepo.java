package sample.repository;

import sample.model.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpielRepo {
    private final Datenbank datenbank;

    public SpielRepo(Datenbank datenbank) {
        this.datenbank = datenbank;
    }

    public Optional<List<Spiel>> getAllSpiels() {
        List<Spiel> spiels = new ArrayList<>();
        String query = "SELECT [SpNr]\n" +
                "      ,[SpName]\n" +
                "      ,[Beschriebung]\n" +
                "      ,[Publikationsdatum]\n" +
                "      ,[PublisherNr]\n" +
                "      ,[KategorieNr]\n" +
                "      ,[SpPreis]\n" +
                "      ,[SpImage]\n" +
                "      ,[LinkZumDownload]\n" +
                "  FROM [GameStore].[dbo].[Spiel]";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                Integer SpNr = result.getInt("SpNr");
                String SpName = result.getString("SpName");
                String Beschriebung = result.getString("Beschriebung");
                String Publikationsdatum = result.getString("Publikationsdatum");
                Integer PublisherNr = result.getInt("PublisherNr");
                Integer KategorieNr = result.getInt("KategorieNr");
                Double SpPreis = result.getDouble("SpPreis");
                String SpImage = result.getString("SpImage");
                String linkZumDownload = result.getString("LinkZumDownload");
                Spiel spiel = new Spiel(SpNr, SpName, Beschriebung, Publikationsdatum, PublisherNr, KategorieNr, SpPreis, SpImage);
                spiel.setLinkZumDownload(linkZumDownload);
                spiels.add(spiel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(spiels);
    }

    public Optional<List<Spiel>> getSpielsByNutzNr(Integer nutzerNr) {
        List<Spiel> spiels = new ArrayList<>();
        String query = "SELECT [Spiel].[SpNr],\n" +
                "\t   [Spiel].[SpName],\n" +
                "\t   [Spiel].[Beschriebung],\n" +
                "\t   [Spiel].[Publikationsdatum],\n" +
                "\t   [Spiel].[PublisherNr],\n" +
                "\t   [Spiel].[KategorieNr],\n" +
                "\t   [Spiel].[SpPreis],\n" +
                "\t   [Spiel].[SpImage],\n" +
                "\t   [Spiel].[LinkZumDownload]\n" +
                "  FROM [GameStore].[dbo].[NutzerSpiels]\n" +
                "  RIGHT JOIN [Spiel] ON [NutzerSpiels].SpielNr = [Spiel].[SpNr]\n" +
                "  WHERE [NutzerSpiels].NutzerNr = " + nutzerNr + ";";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                Integer SpNr = result.getInt("SpNr");
                String SpName = result.getString("SpName");
                String Beschriebung = result.getString("Beschriebung");
                String Publikationsdatum = result.getString("Publikationsdatum");
                Integer PublisherNr = result.getInt("PublisherNr");
                Integer KategorieNr = result.getInt("KategorieNr");
                Double SpPreis = result.getDouble("SpPreis");
                String SpImage = result.getString("SpImage");
                String LinkZumDownload = result.getString("LinkZumDownload");
                Spiel spiel = new Spiel(SpNr, SpName, Beschriebung, Publikationsdatum, PublisherNr, KategorieNr, SpPreis, SpImage);
                spiel.setLinkZumDownload(LinkZumDownload);
                spiels.add(spiel);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(spiels);
    }

    public void addSpiel(String spielName, String spielBeschriebung,
                          String spielPublikationsdatum, Integer publisherNr, Integer kategorieNr,
                          double spielPreis, String spielImage, String linkZumDownload ) throws SQLException {
        String query = "INSERT INTO [dbo].[Spiel]\n" +
                "           ([SpName]\n" +
                "           ,[Beschriebung]\n" +
                "           ,[Publikationsdatum]\n" +
                "           ,[PublisherNr]\n" +
                "           ,[KategorieNr]\n" +
                "           ,[SpPreis]\n" +
                "           ,[SpImage]\n" +
                "           ,[LinkZumDownload])\n" +
                "     VALUES\n" +
                "           ('"+spielName+"'\n" +
                "           ,'"+spielBeschriebung+"'\n" +
                "           ,'"+spielPublikationsdatum+"'\n" +
                "           ,"+publisherNr+"\n" +
                "           ,"+kategorieNr+"\n" +
                "           ,"+spielPreis+"\n" +
                "           ,'"+spielImage+"'\n" +
                "           ,'"+linkZumDownload+"')";
        datenbank.setQuery(query);
    }

    public void updateSpiel(Integer spielNr, String spielName, String spielBeschriebung,
                                String spielPublikationsdatum, Integer publisherNr, Integer kategorieNr,
                                    double spielPreis, String spielImage, String linkZumDownload) throws SQLException {
        String query = "UPDATE [dbo].[Spiel]\n" +
                "   SET [SpName] = '"+spielName+"'\n" +
                "      ,[Beschriebung] = '"+spielBeschriebung+"'\n" +
                "      ,[Publikationsdatum] = '"+spielPublikationsdatum+"'\n" +
                "      ,[PublisherNr] = "+publisherNr+"\n" +
                "      ,[KategorieNr] = "+kategorieNr+"\n" +
                "      ,[SpPreis] = "+spielPreis+"\n" +
                "      ,[SpImage] = '"+spielImage+"'\n" +
                "      ,[LinkZumDownload] = '"+linkZumDownload+"'\n" +
                " WHERE [SpNr] = "+spielNr+"";
        datenbank.setQuery(query);
    }

    public void deleteSpiel(Integer spielNr) throws SQLException {
        deleteAllGenresForSpiel(spielNr);
        deleteAllNutzersForSpiel(spielNr);
        deleteAllKommentarsForSpiel(spielNr);
        String query = "DELETE FROM [dbo].[Spiel]\n" +
                "WHERE [SpNr] = "+spielNr+"";
        datenbank.setQuery(query);
    }

    //XML
    public void getAllSpielsXML() throws FileNotFoundException {
        String query = "SELECT (SELECT [Spiel].[SpNr] as ID,\n" +
                "                [Spiel].[SpName] as NAME,\n" +
                "                [Spiel].[Beschriebung] as BESCHREIBUNG,\n" +
                "                [Spiel].[Publikationsdatum] as PUBLIKATIONSDATUM,\n" +
                "                [Publisher].[PublisherName] as PUBLISHER,\n" +
                "                [Kategorie].[KatName] as KATEGORIE,\n" +
                "                [Spiel].[SpPreis] as PREIS,\n" +
                "                [Spiel].[SpImage] as IMAGELINK,\n" +
                "                [Spiel].[LinkZumDownload] as LINKZUMDOWNLOAD\n" +
                "                FROM [GameStore].[dbo].[Spiel] as SPIEL\n" +
                "                LEFT JOIN [Publisher] ON [Spiel].PublisherNr = [Publisher].[PubNr]\n" +
                "                LEFT JOIN [Kategorie] ON [Spiel].KategorieNr = [Kategorie].[KatNr]\n" +
                "                FOR XML PATH('SPIEL'), TYPE, ELEMENTS , ROOT('SPIELS')) as SpielXml \n";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                SQLXML xml= result.getSQLXML("SpielXml");
                String values = xml.getString();
                PrintWriter out = new PrintWriter("D:/DE-Study/Sommersemester(2021)/DB2/Project/GameStore_DB2/src/resources/xml/spiels.xml");
                out.println(values);
                out.flush();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //Not Spiel Methods ------------------------------------------------------

    public void deleteAllGenresForSpiel(Integer spielNr) throws SQLException{
        String query = "DELETE FROM [dbo].[SpielGenres]\n" +
                "WHERE [SpielNr] = "+spielNr+";";
        datenbank.setQuery(query);
    }

    public void deleteAllNutzersForSpiel(Integer spielNr) throws SQLException{
        String query = "DELETE FROM [dbo].[NutzerSpiels]\n" +
                "WHERE [SpielNr] = "+spielNr+";";
        datenbank.setQuery(query);
    }

    public void deleteAllKommentarsForSpiel(Integer spielNr) throws SQLException{
        String query = "DELETE FROM [dbo].[Kommentar]\n" +
                "WHERE [KommSpiel] = "+spielNr+";";
        datenbank.setQuery(query);
    }

    public void deleteGenreForSpiel(Integer spielNr, Integer genreNr) throws SQLException{
        String query = "DELETE FROM [dbo].[SpielGenres]\n" +
                "WHERE [SpielNr] = "+spielNr+" AND [GenreNr] = "+genreNr+";";
        datenbank.setQuery(query);
    }

    public void addGenreForSpiel(Integer spielNr, Integer genreNr) throws SQLException{
        String query = "INSERT INTO [dbo].[SpielGenres]\n" +
                "           ([SpielNr]\n" +
                "           ,[GenreNr])\n" +
                "     VALUES\n" +
                "           ("+spielNr+"\n" +
                "           ,"+genreNr+")";
        datenbank.setQuery(query);
    }


}
