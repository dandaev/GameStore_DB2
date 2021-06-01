package sample.repository;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.*;

public class Datenbank {
    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String name;

    public Datenbank(String host, String port, String username, String password, String name) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    private Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new SQLServerDriver());
        String url = "jdbc:sqlserver://" + host + ":" + port +
                ";databaseName=" + name + ";user=" + username +";password=" + password + ";";
        return DriverManager.getConnection(url);
    }

    public ResultSet getByQuery(String query) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(query);
        return preparedStatement.executeQuery();
    }
}
