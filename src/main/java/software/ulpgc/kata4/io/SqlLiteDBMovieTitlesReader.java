package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.MovieTitle;

import java.io.File;
import java.sql.*;
import java.util.List;

public class SqlLiteDBMovieTitlesReader implements MovieTitlesReader {
    public static final String SELECT_SQL = """
            SELECT * FROM MOVIES
            """;
    private final Connection connection;
    private final PreparedStatement readPreparedStatement;

    public SqlLiteDBMovieTitlesReader(File fileDB) throws SQLException {
        this("jdbc:sqlite:" + fileDB.getAbsolutePath());
    }

    public SqlLiteDBMovieTitlesReader(String connection) throws SQLException {
        this.connection = DriverManager.getConnection(connection);
        this.readPreparedStatement = createReadStatement();
    }

    private PreparedStatement createReadStatement() throws SQLException {
        return this.connection.prepareStatement(SELECT_SQL);
    }

    @Override
    public List<MovieTitle> read() {
        try {
            ResultSet resultSet = this.readPreparedStatement.executeQuery();
            List<MovieTitle> converted = MovieTitleConverter.from(resultSet);
            this.connection.close();
            return converted;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}