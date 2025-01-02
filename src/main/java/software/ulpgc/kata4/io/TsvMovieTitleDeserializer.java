package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.MovieTitle;

public class TsvMovieTitleDeserializer implements MovieTitleDeserializer {
    @Override
    public MovieTitle deserialize(String text) {
        return deserialize(text.split("\t"));
    }

    private MovieTitle deserialize(String[] fields) {
        return new MovieTitle(
                fields[0],
                fields[2],
                toInt(fields[5]),
                toInt(fields[7])
        );
    }

    private int toInt(String field) {
        try {
            return Integer.parseInt(field);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}