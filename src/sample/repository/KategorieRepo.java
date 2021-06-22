package sample.repository;

import sample.model.Kategorie;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KategorieRepo {
    private final Datenbank datenbank;

    public KategorieRepo(Datenbank datenbank) {
        this.datenbank = datenbank;
    }

    public Optional<List<Kategorie>> getAllKategories(){
        List<Kategorie> kategories = new ArrayList<>();
        String query = "SELECT [KatNr]\n" +
                "      ,[KatName]\n" +
                "  FROM [GameStore].[dbo].[Kategorie]";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                Integer KatNr = result.getInt("KatNr");
                String KatName = result.getString("KatName");
                kategories.add(new Kategorie(KatNr, KatName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(kategories);
    }

    public Kategorie getKategorieByKatNr(Integer kategorieNr) {
        Kategorie kategorie = new Kategorie();
        String query = "SELECT [KatNr]\n" +
                "      ,[KatName]\n" +
                "  FROM [GameStore].[dbo].[Kategorie]\n" +
                "  Where [KatNr] = '"+kategorieNr+"'";
        try {
            ResultSet resultSet = datenbank.getByQuery(query);
            if (!resultSet.next()){
                throw new SQLException("Kategorie not found with ID: "+kategorieNr);
            }
            kategorie.setKatNr(resultSet.getInt("KatNr"));
            kategorie.setKatName(resultSet.getString("KatName"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(throwables.getMessage());
            return null;
        }

        return kategorie;
    }

    public void addKategorie(String KategorieName) throws SQLException {
        String query = "INSERT INTO [dbo].[Kategorie]\n" +
                "           ([KatName])\n" +
                "     VALUES\n" +
                "           ('"+KategorieName+"')";
        datenbank.setQuery(query);
    }

    public void updateKategorie(Integer katNr, String katName) throws SQLException {
        String query = "UPDATE [dbo].[Kategorie]\n" +
                "   SET [KatName] = '"+katName+"'\n" +
                " WHERE [KatNr] = "+katNr+"";
        datenbank.setQuery(query);
    }

    public void deleteKategorie(Integer katNr) throws SQLException {
        updateAllSpielsWithKategorie(katNr);
        String query = "DELETE FROM [dbo].[Kategorie]\n" +
                "WHERE [KatNr] = "+katNr+"";
        datenbank.setQuery(query);
    }

    //XML
    public void getAllKategoriesXML() throws FileNotFoundException {
        String query = "SELECT (SELECT [KatNr] as ID\n" +
                "      ,[KatName] as NAME\n" +
                "  FROM [GameStore].[dbo].[Kategorie]\n" +
                "FOR XML PATH('KATEGORIE'), TYPE, ELEMENTS , ROOT('KATEGORIES')) as KategoriesXml";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                SQLXML xml= result.getSQLXML("KategoriesXml");
                String values = xml.getString();
                PrintWriter out = new PrintWriter("D:/DE-Study/Sommersemester(2021)/DB2/Project/GameStore_DB2/src/resources/xml/kategories.xml");
                out.println(values);
                out.flush();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Spiel Methods
    public void updateAllSpielsWithKategorie(Integer katNr) throws SQLException{
        List<Integer> spiels = new ArrayList<>();
        String query = "SELECT [SpNr]\n" +
                "  FROM [GameStore].[dbo].[Spiels]\n" +
                "  WHERE KategorieNr = " + katNr + ";";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                Integer SpNr = result.getInt("SpNr");
                spiels.add(SpNr);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (!spiels.isEmpty()){
            for (Integer id : spiels){
                String query1 = "UPDATE [dbo].[Spiel]\n" +
                        "   SET [KategorieNr] = 5\n" +
                        " WHERE [SpNr] = "+id+"";
                datenbank.setQuery(query1);
            }
        }
    }
}
