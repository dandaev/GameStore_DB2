package sample.repository;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.*;

public class Datenbank {
    public static NutzerRepo nutzerRepo;
    public static SpielRepo spielRepo;
    public static KategorieRepo kategorieRepo;
    public static PublisherRepo publisherRepo;
    public static GenreRepo genreRepo;
    public static KommentarRepo kommentarRepo;

    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String name;
    private Connection connection;

    public Datenbank(String host, String port, String username, String password, String name) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    private void getConnection() throws SQLException {
        DriverManager.registerDriver(new SQLServerDriver());
        String url = "jdbc:sqlserver://" + host + ":" + port +
                ";databaseName=" + name + ";user=" + username +";password=" + password + ";";
        connection = DriverManager.getConnection(url);
    }

    public ResultSet getByQuery(String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    public void setQuery(String query) throws SQLException{
        connection.prepareStatement(query).execute();
    }

    public static void giveDatenbankPermissionByUser(String username, String password) throws SQLException {
        Datenbank datenbank = new Datenbank("127.0.0.1", "1433", username,password,"GameStore");
        datenbank.getConnection();
        nutzerRepo =  new NutzerRepo(datenbank);
        spielRepo = new SpielRepo(datenbank);
        kategorieRepo = new KategorieRepo(datenbank);
        publisherRepo = new PublisherRepo(datenbank);
        genreRepo = new GenreRepo(datenbank);
        kommentarRepo = new KommentarRepo(datenbank);
    }

}
