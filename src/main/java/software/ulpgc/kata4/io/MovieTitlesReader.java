package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.MovieTitle;

import java.util.List;

public interface MovieTitlesReader {
    List<MovieTitle> read();
}