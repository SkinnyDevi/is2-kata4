package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.MovieTitle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieTitleConverter {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String YEAR = "year";
    public static final String DURATION = "duration";

    public static List<MovieTitle> from(ResultSet resultSet) throws SQLException {
        List<MovieTitle> titles = new ArrayList<>();
        while (resultSet.next()) {
            titles.add(titleFrom(resultSet));
        }
        return titles;
    }

    private static MovieTitle titleFrom(ResultSet resultSet) throws SQLException {
        return new MovieTitle(
                resultSet.getString(ID),
                resultSet.getString(TITLE),
                resultSet.getInt(YEAR),
                resultSet.getInt(DURATION)
        );
    }
}