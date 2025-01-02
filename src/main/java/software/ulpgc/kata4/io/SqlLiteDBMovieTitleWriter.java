package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.MovieTitle;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.sql.Types.INTEGER;
import static java.sql.Types.NVARCHAR;

public class SqlLiteDBMovieTitleWriter implements MovieTitleWriter{
    public static final String CREATE_TABLE_MOVIE_SQL = """
                     CREATE TABLE IF NOT EXISTS movies (
            id TEXT PRIMARY KEY,
            title TEXT NOT NULL,
            year INTEGER,
            duration INTEGER)""";
    public static final String INSERT_INTO_MOVIES_SQL = """
            INSERT INTO movies (id, title, year, duration)
            			VALUES (?, ?, ?, ?)""";
    private final Connection connection;
    private final PreparedStatement insertMovieTitlePreparedStatement;

    public SqlLiteDBMovieTitleWriter(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
        this.createTables();
        this.insertMovieTitlePreparedStatement = createInsertStatement();
    }

    public SqlLiteDBMovieTitleWriter(String connection) throws SQLException {
        this(DriverManager.getConnection(connection));
    }

    public static SqlLiteDBMovieTitleWriter open(File file) throws SQLException {
        return new SqlLiteDBMovieTitleWriter("jdbc:sqlite:" + file.getAbsolutePath());
    }

    private PreparedStatement createInsertStatement() throws SQLException {
        return this.connection.prepareStatement(INSERT_INTO_MOVIES_SQL);
    }

    private void createTables() throws SQLException {
        this.connection.createStatement().execute(CREATE_TABLE_MOVIE_SQL);
    }

    @Override
    public void write(MovieTitle movie) throws IOException {
        try {
            insertMovieStatementFor(movie).execute();
        } catch (SQLException e) {
            throw new IOException(e.getMessage());
        }
    }

    private PreparedStatement insertMovieStatementFor(MovieTitle movie) throws SQLException {
        insertMovieTitlePreparedStatement.clearParameters();
        createParametersFor(movie).forEach(this::defineStatementWith);
        return insertMovieTitlePreparedStatement;
    }

    private void defineStatementWith(StatementParameter statementParameter) {
        try {
            if (statementParameter.value() == null)
                insertMovieTitlePreparedStatement.setNull(
                        statementParameter.index, statementParameter.type
                );
            else
                insertMovieTitlePreparedStatement.setObject(
                        statementParameter.index, statementParameter.value
                );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private record StatementParameter(int index, Object value, int type) {}

    private List<StatementParameter> createParametersFor(MovieTitle movie) {
        return List.of(
                new StatementParameter(1, movie.id(), NVARCHAR),
                new StatementParameter(2, movie.title(), NVARCHAR),
                new StatementParameter(3, movie.year() != -1 ? movie.year() : null, INTEGER),
                new StatementParameter(4, movie.duration() != -1 ? movie.duration() : null, INTEGER)
        );
    }

    @Override
    public void close() throws Exception {
        connection.commit();
        connection.close();
    }
}