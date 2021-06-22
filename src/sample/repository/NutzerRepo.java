package sample.repository;

import sample.model.Nutzer;
import sample.model.Role;
import sample.model.Spiel;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NutzerRepo {
    private final Datenbank datenbank;

    public NutzerRepo(Datenbank datenbank) {
        this.datenbank = datenbank;
    }

    public Optional<List<Nutzer>> getAllNutzers() {
        List<Nutzer> nutzers = new ArrayList<>();
        String query = "SELECT [NutzNr]\n" +
                "      ,[NutzName]\n" +
                "      ,[NutzLogin]\n" +
                "      ,[NutzPasswort]\n" +
                "      ,[NutzRole]\n" +
                "  FROM [GameStore].[dbo].[Nutzer]";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                Integer NutzNr = result.getInt("NutzNr");
                String NutzName = result.getString("NutzName");
                String NutzLogin = result.getString("NutzLogin");
                String NutzPasswort = result.getString("NutzPasswort");
                Role NutzRole = null;
                switch (result.getString("NutzRole")) {
                    case "Reader":
                        NutzRole = Role.Reader;
                        break;
                    case "ReadWriter":
                        NutzRole = Role.ReadWriter;
                        break;
                    case "Admin":
                        NutzRole = Role.Admin;
                }
                nutzers.add(new Nutzer(NutzNr, NutzName, NutzLogin, NutzPasswort, NutzRole));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.of(nutzers);
    }


    public Nutzer getNutzerByUsername(String username) {
        Nutzer nutzer = new Nutzer();
        String query = "SELECT [NutzNr]\n" +
                "      ,[NutzName]\n" +
                "      ,[NutzLogin]\n" +
                "      ,[NutzPasswort]\n" +
                "      ,[NutzRole]\n" +
                "  FROM [GameStore].[dbo].[Nutzer]\n" +
                "  Where [NutzLogin] = '"+username+"'";
        try {
            ResultSet resultSet = datenbank.getByQuery(query);
            if (!resultSet.next()){
                throw new SQLException("Login not found "+username);
            }
            nutzer.setNutzNr(resultSet.getInt("NutzNr"));
            nutzer.setNutzName(resultSet.getString("NutzName"));
            nutzer.setNutzLogin(resultSet.getString("NutzLogin"));
            nutzer.setNutzPasswort(resultSet.getString("NutzPasswort"));
            Role NutzRole = null;
            switch (resultSet.getString("NutzRole")) {
                case "Reader":
                    NutzRole = Role.Reader;
                    break;
                case "ReadWriter":
                    NutzRole = Role.ReadWriter;
                    break;
                case "Admin":
                    NutzRole = Role.Admin;
                    break;
            }
            nutzer.setNutzRole(NutzRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return nutzer;
    }

    public Nutzer getNutzerByNr(Integer nr) {
        Nutzer nutzer = new Nutzer();
        String query = "SELECT [NutzNr]\n" +
                "      ,[NutzName]\n" +
                "      ,[NutzLogin]\n" +
                "      ,[NutzPasswort]\n" +
                "      ,[NutzRole]\n" +
                "  FROM [GameStore].[dbo].[Nutzer]\n" +
                "  Where [NutzNr] = "+nr+"";
        try {
            ResultSet resultSet = datenbank.getByQuery(query);
            if (!resultSet.next()){
                throw new SQLException("Login not found "+nr);
            }
            nutzer.setNutzNr(resultSet.getInt("NutzNr"));
            nutzer.setNutzName(resultSet.getString("NutzName"));
            nutzer.setNutzLogin(resultSet.getString("NutzLogin"));
            nutzer.setNutzPasswort(resultSet.getString("NutzPasswort"));
            Role NutzRole = null;
            switch (resultSet.getString("NutzRole")) {
                case "Reader":
                    NutzRole = Role.Reader;
                    break;
                case "ReadWriter":
                    NutzRole = Role.ReadWriter;
                    break;
                case "Admin":
                    NutzRole = Role.Admin;
                    break;
            }
            nutzer.setNutzRole(NutzRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(throwables.getMessage());
            return null;
        }
        return nutzer;
    }

    public void addNutzer(String NutzName, String NutzLogin, String NutzPasswort, Role NutzRole ) throws SQLException {
        String query = "INSERT INTO [dbo].[Nutzer]\n" +
                "           ([NutzName]\n" +
                "           ,[NutzLogin]\n" +
                "           ,[NutzPasswort]\n" +
                "           ,[NutzRole])\n" +
                "     VALUES\n" +
                "           ('"+NutzName+"'\n" +
                "           ,'"+NutzLogin+"'\n" +
                "           ,'"+NutzPasswort+"'\n" +
                "           ,'"+NutzRole.toString()+"')";
        datenbank.setQuery(query);
    }

    public void updateNutzer(Integer nutzerNr, String NutzName, String NutzLogin, String NutzPasswort, Role NutzRole) throws SQLException {
        String query = "UPDATE [dbo].[Nutzer]\n" +
                "   SET [NutzName] = '"+NutzName+"'\n" +
                "      ,[NutzLogin] = '"+NutzLogin+"'\n" +
                "      ,[NutzPasswort] = '"+NutzPasswort+"'\n" +
                "      ,[NutzRole] = '"+NutzRole+"'\n" +
                " WHERE [NutzNr] = "+nutzerNr+"";
        datenbank.setQuery(query);
    }

    public void deleteNutzer(Integer nutzNr) throws SQLException {
        deleteAllSpielsForNutzer(nutzNr);
        deleteAllKommentarsForNutzer(nutzNr);
        String query = "DELETE FROM [dbo].[Nutzer]\n" +
                "WHERE [NutzNr] = "+nutzNr+"";
        datenbank.setQuery(query);
    }

    public void getAllNutzersXML() throws FileNotFoundException {
        String query = "SELECT (SELECT [NutzNr] as ID\n" +
                "      ,[NutzName] as NAME\n" +
                "      ,[NutzLogin] as LOGIN\n" +
                "      ,[NutzPasswort] as PASSWORD\n" +
                "      ,[NutzRole] as ROLE\n" +
                "  FROM [GameStore].[dbo].[Nutzer]" +
                "  FOR XML PATH('NUTZER'), TYPE, ELEMENTS , ROOT('NUTZERS')) as NutzersXml";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                SQLXML xml= result.getSQLXML("NutzersXml");
                String values = xml.getString();
                PrintWriter out = new PrintWriter("D:/DE-Study/Sommersemester(2021)/DB2/Project/GameStore_DB2/src/resources/xml/nutzers.xml");
                out.println(values);
                out.flush();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Spiel Methods-----------------------------------------------------------------------
    public void addSpielForNutzer(Integer nutzerNr, Integer spielNr) throws SQLException {
        String query = "INSERT INTO [dbo].[NutzerSpiels]\n" +
                "           ([NutzerNr]\n" +
                "           ,[SpielNr])\n" +
                "     VALUES\n" +
                "           ("+nutzerNr+"\n" +
                "           ,"+spielNr+"\n)" ;
        datenbank.setQuery(query);
    }

    public void deleteSpielForNutzer(Integer nutzerNr, Integer spielNr) throws SQLException {
        String query = "DELETE FROM [dbo].[NutzerSpiels]\n" +
                "WHERE [NutzerNr] = "+nutzerNr+" AND [SpielNr] = "+spielNr+" ;";
        datenbank.setQuery(query);
    }

    public void deleteAllSpielsForNutzer(Integer nutzerNr) throws SQLException{
        String query = "DELETE FROM [dbo].[NutzerSpiels]\n" +
                "WHERE [NutzerNr] = "+nutzerNr+";";
        datenbank.setQuery(query);
    }

    public void deleteAllKommentarsForNutzer(Integer nutzerNr) throws SQLException{
        String query = "DELETE FROM [dbo].[Kommentar]\n" +
                "WHERE [KommNutzer] = "+nutzerNr+";";
        datenbank.setQuery(query);
    }
}
