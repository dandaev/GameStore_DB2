package sample.repository;

import sample.model.Genre;
import sample.model.Kategorie;
import sample.model.Publisher;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenreRepo {
    private final Datenbank datenbank;

    public GenreRepo(Datenbank datenbank) {
        this.datenbank = datenbank;
    }

    public Optional<List<Genre>> getAllGenres(){
        List<Genre> genres = new ArrayList<>();
        String query = "SELECT [GeNr]\n" +
                "      ,[GeName]\n" +
                "  FROM [GameStore].[dbo].[Genre]";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                Integer GeNr = result.getInt("GeNr");
                String GeName = result.getString("GeName");
                genres.add(new Genre(GeNr, GeName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(genres);
    }

    public Genre getGenreByGeNr(Integer genreNr) {
        Genre genre = new Genre();
        String query = "SELECT [GeNr]\n" +
                "      ,[GeName]\n" +
                "  FROM [GameStore].[dbo].[Genre]\n" +
                "  Where [GeNr] = '"+genreNr+"'";
        try {
            ResultSet resultSet = datenbank.getByQuery(query);
            if (!resultSet.next()){
                throw new SQLException("Genre not found with ID: "+genreNr);
            }
            genre.setGeNr(resultSet.getInt("GeNr"));
            genre.setGeName(resultSet.getString("GeName"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(throwables.getMessage());
            return null;
        }

        return genre;
    }

    public Optional<List<Genre>> getGenresBySpNr(Integer spielNr) {
        List<Genre> genres = new ArrayList<>();
        String query = "SELECT [Genre].[GeNr],\n" +
                "\t   [Genre].[GeName]\n" +
                "  FROM [GameStore].[dbo].[SpielGenres]\n" +
                "  RIGHT JOIN [Genre] ON [SpielGenres].GenreNr = [Genre].GeNr\n" +
                "  WHERE [SpielGenres].SpielNr = '"+spielNr+"';";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                Integer GeNr = result.getInt("GeNr");
                String GeName = result.getString("GeName");
                genres.add(new Genre(GeNr, GeName));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return Optional.empty();
        }

        return Optional.of(genres);
    }

    public void addGenre(String genreName) throws SQLException {
        String query = "INSERT INTO [dbo].[Genre]\n" +
                "           ([GeName])\n" +
                "     VALUES\n" +
                "           ('"+genreName+"')";
        datenbank.setQuery(query);
    }

    public void updateGenre(Integer genreNr, String genreName) throws SQLException {
        String query = "UPDATE [dbo].[Genre]\n" +
                "   SET [GeName] = '"+genreName+"'\n" +
                " WHERE [GeNr] = "+genreNr+"";
        datenbank.setQuery(query);
    }

    public void deleteGenre(Integer genreNr) throws SQLException {
        deleteAllSpielsForGenre(genreNr);
        String query = "DELETE FROM [dbo].[Genre]\n" +
                "WHERE [GeNr] = "+genreNr+"";
        datenbank.setQuery(query);
    }

    //XML
    public void getAllGenresXML() throws FileNotFoundException {
        String query = "SELECT (SELECT [GeNr] as ID ,[GeName] as NAME\n" +
                "FROM [GameStore].[dbo].[Genre]\n" +
                "FOR XML PATH('GENRE'), TYPE, ELEMENTS , ROOT('GENRES')) as GenresXml";
        try {
            ResultSet result = datenbank.getByQuery(query);
            while (result.next()) {
                SQLXML xml= result.getSQLXML("GenresXml");
                String values = xml.getString();
                PrintWriter out = new PrintWriter("D:/DE-Study/Sommersemester(2021)/DB2/Project/GameStore_DB2/src/resources/xml/genres.xml");
                out.println(values);
                out.flush();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Spiel Methods-----------------------------------------------------------------------
    public void deleteAllSpielsForGenre(Integer genreNr) throws SQLException{
        String query = "DELETE FROM [dbo].[SpielGenres]\n" +
                "WHERE [GenreNr] = "+genreNr+";";
        datenbank.setQuery(query);
    }
}
