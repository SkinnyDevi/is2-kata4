package software.ulpgc.kata4;

import software.ulpgc.kata4.control.ImportCommand;
import software.ulpgc.kata4.io.SqlLiteDBMovieTitleWriter;
import software.ulpgc.kata4.io.SqlLiteDBMovieTitlesReader;
import software.ulpgc.kata4.model.MovieTitle;
import software.ulpgc.kata4.view.HorizontalHistogramTitleStatisticDisplayer;
import software.ulpgc.kata4.view.MapHistogramDataFieldBuilder;
import software.ulpgc.kata4.view.StatisticDisplayer;

import java.io.File;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("./title.basics.tsv.gz");
        importDB(file);
        List<MovieTitle> titles = getMovieTitlesFromDB();
        titles.sort(Comparator.comparingInt(MovieTitle::year));
        Map<String, Integer> yearStats = getYearStats(titles);

        StatisticDisplayer histogram = new HorizontalHistogramTitleStatisticDisplayer(
                new MapHistogramDataFieldBuilder(yearStats),
                "Year Statistics Histogram"
        );
        histogram.display();
    }

    private static List<MovieTitle> getMovieTitlesFromDB() throws SQLException {
        return new SqlLiteDBMovieTitlesReader(getFileDB()).read();
    }

    private static File getFileDB() {
        return new File("movies.db");
    }

    private static void importDB(File file) {
        new ImportCommand(getImportedFile(file)).execute();;
    }

    private static FileImportable getImportedFile(File file) {
        return () -> file;
    }

    private static Map<String, Integer> getYearStats(List<MovieTitle> titles) {
        Map<String, Integer> stats = new LinkedHashMap<>();
        for (MovieTitle t: titles)
            stats.put(toString(t.year()), stats.getOrDefault(toString(t.year()), 0) + 1);

        return stats;
    }

    private static String toString(int i) {
        return String.valueOf(i);
    }
}