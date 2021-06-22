package sample.repository;

import sample.model.Publisher;
import sample.model.Spiel;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PublisherRepo {
    private final Datenbank datenbank;

    public PublisherRepo(Datenbank datenbank) {
        this.datenbank = datenbank;
    }

    public Optional<List<Publisher>> getAllPublishers(){
        List<Publisher> publishers = new ArrayList<>();
        String query = "SELECT [PubNr]\n" +
                "      ,[PublisherName]\n" +
                "  FROM [GameStore].[dbo].[Publisher]";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                Integer PubNr = result.getInt("PubNr");
                String PublisherName = result.getString("PublisherName");
                publishers.add(new Publisher(PubNr, PublisherName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(publishers);
    }

    public Publisher getPublisherByPubNr(Integer publisherNr) {
        Publisher publisher = new Publisher();
        String query = "SELECT [PubNr]\n" +
                "      ,[PublisherName]\n" +
                "  FROM [GameStore].[dbo].[Publisher]\n" +
                "  Where [PubNr] = '"+publisherNr+"'";
        try {
            ResultSet resultSet = datenbank.getByQuery(query);
            if (!resultSet.next()){
                throw new SQLException("Publisher not found with ID: "+publisherNr);
            }
            publisher.setPubNr(resultSet.getInt("PubNr"));
            publisher.setPublisherName(resultSet.getString("PublisherName"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(throwables.getMessage());
            return null;
        }

        return publisher;
    }

    public void addPublisher(String publisherName) throws SQLException {
        String query = "INSERT INTO [dbo].[Publisher]\n" +
                "           ([PublisherName])\n" +
                "     VALUES\n" +
                "           ('"+publisherName+"')";
        datenbank.setQuery(query);
    }

    public void updatePublisher(Integer pubNr, String pubName) throws SQLException {
        String query = "UPDATE [dbo].[Publisher]\n" +
                "   SET [PublisherName] = '"+pubName+"'\n" +
                " WHERE [PubNr] = "+pubNr+"";
        datenbank.setQuery(query);
    }

    public void deletePublisher(Integer pubNr) throws SQLException {
        deleteAllSpielsByPublisher(pubNr);
        String query = "DELETE FROM [dbo].[Publisher]\n" +
                "WHERE [PubNr] = "+pubNr+"";
        datenbank.setQuery(query);
    }

    //XML
    public void getAllPublishersXML() throws FileNotFoundException {
        String query = "SELECT (SELECT [PubNr] as ID\n" +
                "      ,[PublisherName] as NAME\n" +
                "  FROM [GameStore].[dbo].[Publisher]\n" +
                "FOR XML PATH('PUBLISHER'), TYPE, ELEMENTS , ROOT('PUBLISHERS')) as PublishersXml";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                SQLXML xml= result.getSQLXML("PublishersXml");
                String values = xml.getString();
                PrintWriter out = new PrintWriter("D:/DE-Study/Sommersemester(2021)/DB2/Project/GameStore_DB2/src/resources/xml/publishers.xml");
                out.println(values);
                out.flush();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Spiel Methods
    public void deleteAllSpielsByPublisher(Integer pubNr) throws SQLException{
        List<Integer> spiels = new ArrayList<>();
        String query = "SELECT [SpNr]\n" +
                "  FROM [GameStore].[dbo].[Spiels]\n" +
                "  WHERE PublisherNr = " + pubNr + ";";
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
                Datenbank.spielRepo.deleteSpiel(id);
            }
        }
    }

}
